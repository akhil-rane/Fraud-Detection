package com.floatInvoice.fraudDetection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.floatinvoice.messages.FraudInvoiceDtls;

public class WorkerThread implements Callable<Integer> {

	DispatcherKey taskKey;
	private NamedParameterJdbcTemplate jdbcTemplate;
	
	
	public WorkerThread(){}
	
	public WorkerThread(NamedParameterJdbcTemplate jdbcTemplate, DispatcherKey taskKey){
		this.jdbcTemplate = jdbcTemplate;
		this.taskKey = taskKey;
	}
	
	
	@Override
	public Integer call() throws Exception {
		
		System.out.println(String.format(" Current Thread - %s, servicing -> buyerOrgId = %d and supplierOrgId = %d ", 
				Thread.currentThread().getName(), taskKey.getBuyerId(), taskKey.getSupplierId()));
		List<FraudInvoiceDtls> list = null ;
		try {
			list = fetchWorkAssignments();
			FraudDetection fDetection = new FraudDetection();
			List<FraudTestResults> resultList = fDetection.doFraudTests(list);
			if(resultList != null && resultList.size() > 0)
				persistFraudTestResults(resultList);
			dequeueProcessedInvoices(list);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}finally{
			System.out.println( " Removing " + taskKey );
			FraudDetectionPollster.assignmentQueue.remove(taskKey);
		}
		return list == null? 0 : list.size();
	}

	private List<FraudInvoiceDtls> fetchWorkAssignments() {
		int buyerId = taskKey.getBuyerId();
		int supplierId = taskKey.getSupplierId();
		final String sql = " SELECT II.INVOICE_START_DT, II.INVOICE_END_DT, II.AMOUNT, II.INVOICE_NO, "
				+ " II.PURCHASE_ORDER_NO, II.REF_ID, II.INSERT_DT, II.USER_ID, II.DESCRIPTION, FDQ.RANK, FDQ.BUYER_ID, FDQ.SUPPLIER_ID "
				+ " FROM INVOICE_INFO II "
				+ "JOIN FRAUD_DETECTION_QUEUE FDQ "
				+ "ON II.INVOICE_ID = FDQ.FRAUD_INVOICE_ID "
				+ "WHERE FDQ.BUYER_ID =:buyerId AND FDQ.SUPPLIER_ID=:supplierId AND FDQ.STATUS=0";
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("buyerId", buyerId);
		params.addValue("supplierId", supplierId);
		@SuppressWarnings("unchecked")
		List<FraudInvoiceDtls> list = jdbcTemplate.query(sql, params, new FraudDetectionRowMapper());
		return list;
	}

	private int dequeueProcessedInvoices( final List<FraudInvoiceDtls> list ){
		List<Integer> ids = new ArrayList<>();
		for(FraudInvoiceDtls fiDtls : list){
			ids.add(fiDtls.getFraudId());
		}
		final String sql = "UPDATE FRAUD_DETECTION_QUEUE FDQ SET FDQ.STATUS = 1 WHERE FDQ.RANK IN (:IDS)";
		MapSqlParameterSource paramMap = new MapSqlParameterSource();
		paramMap.addValue("IDS", ids);
		int rowCount = jdbcTemplate.update(sql, paramMap);
		System.out.println(String.format("Dequeued records %d", rowCount));
		return rowCount;
	}
	
	private int persistFraudTestResults(final List<FraudTestResults> fraudTestResultList){
		int rowCount = 0;
		for( FraudTestResults tmp: fraudTestResultList){
			final String sql = "INSERT INTO FRAUD_INVOICES FI "
					+ "(FRAUD_INVOICE_ID, FRAUD_INVOICE_NO, BUYER_ID, SUPPLIER_ID, INSERT_DT, UPDATE_DT, FRAUD_INVOICE_REF_ID) "
					+ " VALUES (:fraudInvoiceId, :fraudInvoiceNo, :buyerId, :supplierId, :insertDt, :updateDt, :invoiceRefId)";
			MapSqlParameterSource paramMap = new MapSqlParameterSource();
			paramMap.addValue("fraudInvoiceId", tmp.getInvoiceDtls().getInvoiceId());
			paramMap.addValue("fraudInvoiceNo", tmp.getInvoiceDtls().getInvoiceNo());
			paramMap.addValue("buyerId", tmp.getInvoiceDtls().getBuyerId());
			paramMap.addValue("supplierId", tmp.getInvoiceDtls().getSupplierId());
			paramMap.addValue("insertDt", new Timestamp(System.currentTimeMillis()));
			paramMap.addValue("updateDt", new Timestamp(System.currentTimeMillis()));
			paramMap.addValue("invoiceRefId", tmp.getInvoiceDtls().getRefId());
			rowCount = jdbcTemplate.update(sql, paramMap);
			final List<Integer> lst = tmp.getFraudTestIds();
			final String fraudTestDtlsSql = "INSERT INTO FRAUD_INVOICE_TEST_DTLS (FRAUD_TEST_ID) VALUES (?)  ";
			jdbcTemplate.getJdbcOperations().batchUpdate(fraudTestDtlsSql, new BatchPreparedStatementSetter() {
				@Override
				public int getBatchSize() {
					return lst.size();
				}
				@Override
				public void setValues(PreparedStatement ps, int idx)
						throws SQLException {
					ps.setInt(1, lst.get(idx));
				}
			});
		}
		return rowCount;
	}
	
	private class FraudDetectionRowMapper implements RowMapper<FraudInvoiceDtls>{
		@Override
		public FraudInvoiceDtls mapRow(ResultSet rs, int arg1)
				throws SQLException {
			FraudInvoiceDtls result = new FraudInvoiceDtls();
			result.setStartDt(rs.getDate("INVOICE_START_DT"));
			result.setEndDt(rs.getDate("INVOICE_END_DT"));
			result.setAmount(rs.getDouble("AMOUNT"));
			result.setInvoiceNo(rs.getString("INVOICE_NO"));
			result.setInvoicePONo(rs.getString("PURCHASE_ORDER_NO"));
			result.setRefId(rs.getString("REF_ID"));
			result.setInsertDt(rs.getDate("INSERT_DT"));
			result.setUserId(rs.getInt("USER_ID"));
			result.setDesc(rs.getString("DESCRIPTION"));
			result.setFraudId(rs.getInt("RANK"));
			result.setBuyerId(rs.getInt("BUYER_ID"));
			result.setSupplierId(rs.getInt("SUPPLIER_ID"));
			return result;
		}
	}
}
