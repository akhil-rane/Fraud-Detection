package com.floatinvoice.business.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.floatinvoice.messages.FraudInvoiceDtls;
import com.floatinvoice.messages.ListMsg;
import com.floatinvoice.messages.LoanDtlsMsg;

public class JdbcFraudInvoiceInfoDao implements FraudInvoiceInfoDao {

	
	private NamedParameterJdbcTemplate jdbcTemplate;


	public JdbcFraudInvoiceInfoDao(){}
	
	
	public JdbcFraudInvoiceInfoDao(DataSource dataSource){
		this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}
	
	@Override
	public ListMsg<FraudInvoiceDtls> findFraudInvoicesBySupplier(int orgId) {
		
		final String sql = " SELECT II.INVOICE_START_DT, II.INVOICE_END_DT,"
				+ " II.AMOUNT, II.INVOICE_NO, II.DESCRIPTION, II.PURCHASE_ORDER_NO, II.INSERT_DT, CLI.EMAIL, "
				+ " BUYERORG.ACRONYM AS BUYER, SUPPLIERORG.ACRONYM AS SME, FTC.FRAUD_TEST_NAME, II.REF_ID "
				+ " FROM FRAUD_INVOICES FI "
				+ " JOIN INVOICE_INFO II "
				+ " ON II.INVOICE_ID = FI.FRAUD_INVOICE_ID "
				+ " JOIN FRAUD_INVOICE_TEST_DTLS FITD "
				+ " ON FI.ID = FITD.FRAUD_ID "
				+ " JOIN FRAUD_TEST_CASES FTC "
				+ " ON FTC.FRAUD_TEST_ID = FITD.FRAUD_TEST_ID "
				+ " JOIN ORGANIZATION_INFO BUYERORG "
				+ " ON BUYERORG.COMPANY_ID = II.BUYER_ID "
				+ " JOIN ORGANIZATION_INFO SUPPLIERORG "
				+ " ON SUPPLIERORG.COMPANY_ID = II.COMPANY_ID "
				+ " JOIN CLIENT_LOGIN_INFO CLI "
				+ " ON CLI.USER_ID = II.USER_ID "
				+ " WHERE FI.SUPPLIER_ID = :orgId ";
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("orgId", orgId);
		List<FraudInvoiceDtls> lst = jdbcTemplate.query(sql, params, /*new FraudInvoiceRowMapper()*/ new ResultSetExtractor<List<FraudInvoiceDtls>>(){

			@Override
			public List<FraudInvoiceDtls> extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				
				List<FraudInvoiceDtls> resultList = new LinkedList<>();
				Map<String, FraudInvoiceDtls> resultMap = new LinkedHashMap<>();
				String invoiceRefId = null;
				while(rs.next()){
					invoiceRefId = rs.getString("REF_ID");
					FraudInvoiceDtls row = null;
					if(!resultMap.containsKey(invoiceRefId)){
						row = new FraudInvoiceDtls();
						resultList.add(row);
						resultMap.put(invoiceRefId, row);
						List<String> fraudTestNames = new LinkedList<>();
						row.getFraudTestName().addAll(fraudTestNames);
						row.setAmount(rs.getDouble("AMOUNT"));
						row.setStartDt(rs.getDate("INVOICE_START_DT"));
						row.setEndDt(rs.getDate("INVOICE_END_DT"));
						row.setInvoiceNo(rs.getString("INVOICE_NO"));
						row.setDesc(rs.getString("DESCRIPTION"));
						row.setInvoicePONo(rs.getString("PURCHASE_ORDER_NO"));
						row.setInsertDt(rs.getDate("INSERT_DT"));
						row.setEmail(rs.getString("EMAIL"));
						row.setSme(rs.getString("SME"));
						row.setSmeCtpy(rs.getString("BUYER"));
						row.setRefId(rs.getString("REF_ID"));
					}else{
						row = resultMap.get(invoiceRefId);
					}
					row.getFraudTestName().add(rs.getString("FRAUD_TEST_NAME"));
				}
				return resultList;
			}
			
		});
		return new ListMsg<>(lst);
	}
	
	
	@Override
	public ListMsg<FraudInvoiceDtls> findAllFraudInvoices() {
		
		final String sql = " SELECT II.INVOICE_START_DT, II.INVOICE_END_DT,"
				+ " II.AMOUNT, II.INVOICE_NO, II.DESCRIPTION, II.PURCHASE_ORDER_NO, II.INSERT_DT, II.REF_ID, CLI.EMAIL, "
				+ " BUYERORG.ACRONYM AS BUYER, SUPPLIERORG.ACRONYM AS SME, NULL AS FRAUD_TEST_NAME "
				+ " FROM FRAUD_INVOICES FI "
				+ " JOIN INVOICE_INFO II "
				+ " ON II.INVOICE_ID = FI.FRAUD_INVOICE_ID "
				+ " JOIN FRAUD_INVOICE_TEST_DTLS FITD "
				+ " ON FI.ID = FITD.FRAUD_ID "
				+ " JOIN FRAUD_TEST_CASES FTC "
				+ " ON FTC.FRAUD_TEST_ID = FITD.FRAUD_TEST_ID "
				+ " JOIN ORGANIZATION_INFO BUYERORG "
				+ " ON BUYERORG.COMPANY_ID = II.BUYER_ID "
				+ " JOIN ORGANIZATION_INFO SUPPLIERORG "
				+ " ON SUPPLIERORG.COMPANY_ID = II.COMPANY_ID "
				+ " JOIN CLIENT_LOGIN_INFO CLI "
				+ " ON CLI.USER_ID = II.USER_ID ";
		
		List<FraudInvoiceDtls> lst = jdbcTemplate.query(sql, new ResultSetExtractor<List<FraudInvoiceDtls>>(){

			@Override
			public List<FraudInvoiceDtls> extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				
				List<FraudInvoiceDtls> resultList = new LinkedList<>();
				Map<String, FraudInvoiceDtls> resultMap = new LinkedHashMap<>();
				String invoiceRefId = null;
				while(rs.next()){
					invoiceRefId = rs.getString("REF_ID");
					FraudInvoiceDtls row = null;
					if(!resultMap.containsKey(invoiceRefId)){
						row = new FraudInvoiceDtls();
						resultList.add(row);
						resultMap.put(invoiceRefId, row);
						List<String> fraudTestNames = new LinkedList<>();
						row.getFraudTestName().addAll(fraudTestNames);
						row.setAmount(rs.getDouble("AMOUNT"));
						row.setStartDt(rs.getDate("INVOICE_START_DT"));
						row.setEndDt(rs.getDate("INVOICE_END_DT"));
						row.setInvoiceNo(rs.getString("INVOICE_NO"));
						row.setDesc(rs.getString("DESCRIPTION"));
						row.setInvoicePONo(rs.getString("PURCHASE_ORDER_NO"));
						row.setInsertDt(rs.getDate("INSERT_DT"));
						row.setEmail(rs.getString("EMAIL"));
						row.setSme(rs.getString("SME"));
						row.setSmeCtpy(rs.getString("BUYER"));
						row.setRefId(rs.getString("REF_ID"));
					}else{
						row = resultMap.get(invoiceRefId);
					}
					row.getFraudTestName().add(rs.getString("FRAUD_TEST_NAME"));
				}
				return resultList;
			}
			
		});
		return new ListMsg<>(lst);
	}

}
