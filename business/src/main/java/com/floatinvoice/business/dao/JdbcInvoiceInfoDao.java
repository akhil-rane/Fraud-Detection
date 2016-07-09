package com.floatinvoice.business.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.floatinvoice.common.InvoiceAcctStatus;
import com.floatinvoice.common.InvoiceFinancierMapStatus;
import com.floatinvoice.common.InvoicePoolStatus;
import com.floatinvoice.common.InvoiceStatus;
import com.floatinvoice.common.LoanStatus;
import com.floatinvoice.common.UUIDGenerator;
import com.floatinvoice.common.UserContext;
import com.floatinvoice.messages.BaseMsg;
import com.floatinvoice.messages.InvoiceAccountInfoMsg;
import com.floatinvoice.messages.InvoiceDtlsMsg;
import com.floatinvoice.messages.ListMsg;
import com.floatinvoice.messages.LoanDtlsMsg;

@Repository
public class JdbcInvoiceInfoDao implements InvoiceInfoDao {

	private NamedParameterJdbcTemplate jdbcTemplate;
	private OrgReadDao orgReadDao;

	public static final double MAX_POOL_AMOUNT = 300000.00;

	public JdbcInvoiceInfoDao(){

	}

	public JdbcInvoiceInfoDao( DataSource dataSource, OrgReadDao orgReadDao ){		
		jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
		this.orgReadDao = orgReadDao;
	}

	@Override
	public Map<String, Object> findInvoicePoolByRefId(String poolRefId){
		Map<String, Object> result = new HashMap<>();
		final String sql = "SELECT * FROM INVOICE_POOL WHERE POOL_REF_ID = :poolRefId";
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("poolRefId", poolRefId);
		result = jdbcTemplate.queryForObject(sql, params, new ColumnMapRowMapper());
		return result;
	}
	
	@Override
	public ListMsg<InvoiceDtlsMsg> fetchAllNewInvoices(String smeAcronym) {
		final String sql = " SELECT * FROM INVOICE_INFO II, ORGANIZATION_INFO ORG WHERE "
				+ "II.COMPANY_ID = ORG.COMPANY_ID AND ORG.ACRONYM = :smeAcronym "
				+ " AND II.IS_ELIGIBLE=0 "
				+ " AND II.INVOICE_ID NOT IN (SELECT INC.INVOICE_ID FROM INVOICE_POOL_CANDIDATE INC )";
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("smeAcronym", smeAcronym);
		List<InvoiceDtlsMsg> list = jdbcTemplate.query(sql, map, new InvoiceInfoRowMapper());
		ListMsg<InvoiceDtlsMsg> result = new ListMsg<>(list);
		if (list.size() > 0 )
			result.setCount(list.size());
		return result;
	}


	private class InvoiceInfoRowMapper implements RowMapper<InvoiceDtlsMsg>{

		@Override
		public InvoiceDtlsMsg mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			InvoiceDtlsMsg result = new InvoiceDtlsMsg();
			result.setAmount(rs.getDouble("amount"));
			result.setDesc(rs.getString("description"));
			result.setSmeCtpy(rs.getString("buyer_name"));
			result.setStartDt(rs.getDate("invoice_start_dt"));
			result.setEndDt(rs.getDate("invoice_end_dt"));
			result.setStatus(InvoiceStatus.ACTIVE.getStatus());
			result.setSme(rs.getString("acronym"));
			result.setInvoiceNo(rs.getString("invoice_no"));
			result.setRefId( rs.getString("ref_id"));
			result.setInvoicePONo( rs.getString("purchase_order_no"));
			return result;
		}

	}

	@Override
	public ListMsg<InvoiceDtlsMsg> fetchInvoicesAvailableForBanks(int bankOrgId) {

	
		final String newSql = " SELECT SMEORG.ACRONYM AS SMEACRO, BUYERORG.ACRONYM AS BUYERACRO, "
				+ " IP.POOL_REF_ID, IP.INVOICE_POOL_AMT, IP.POOL_ID, SBAM.SME_ORG_ID, SBAM.BUYER_ORG_ID "
				+ " FROM INVOICE_NOTIFICATION_LIST INL "
				+ " JOIN INVOICE_POOL IP "
				+ " ON INL.INVOICE_POOL_ID = IP.POOL_ID AND IP.POOL_STATUS = 1 "
				
				+ " JOIN ORGANIZATION_INFO SMEORG "
				+ " ON IP.SUPPLIER_ID = SMEORG.COMPANY_ID "
				+ " JOIN ORGANIZATION_INFO BUYERORG "
				+ " ON IP.BUYER_ID = BUYERORG.COMPANY_ID "
				+ " LEFT JOIN SME_BUYER_APPROVAL_MAP SBAM "
				+ " ON SBAM.SME_ORG_ID = SMEORG.COMPANY_ID AND SBAM.BUYER_ORG_ID = BUYERORG.COMPANY_ID "				
				+ " WHERE INL.FINANCIER_ID = :bankOrgId AND IP.IS_BID_OPEN = 'Y' AND "
				+ " IP.POOL_ID NOT IN (SELECT IFC.INVOICE_POOL_ID FROM INVOICE_FINANCIER_CANDIDATES IFC WHERE IFC.FINANCIER_ID = :bankOrgId) "
				+ " ORDER BY SMEORG.ACRONYM, BUYERORG.ACRONYM";
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("bankOrgId", bankOrgId);
		List<InvoiceDtlsMsg> list = jdbcTemplate.query(newSql, map, new FinancierInvoiceViewRowMapper());
		ListMsg<InvoiceDtlsMsg> result = new ListMsg<>(list);
		if (list.size() > 0 )
			result.setCount(list.size());
		return result;
	}



	private class FinancierInvoiceViewRowMapper implements RowMapper<InvoiceDtlsMsg>{

		@Override
		public InvoiceDtlsMsg mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			InvoiceDtlsMsg result = new InvoiceDtlsMsg();
			/*result.setAmount(rs.getDouble("amount"));
			result.setDesc(rs.getString("description"));
			*/
			result.setSmeCtpy(rs.getString("BUYERACRO"));
			/*result.setStartDt(rs.getDate("invoice_start_dt"));
			result.setEndDt(rs.getDate("invoice_end_dt"));
			result.setStatus(InvoiceStatus.ACTIVE.getStatus());
			*/
			result.setSme(rs.getString("SMEACRO"));
//			result.setInvoiceNo(rs.getString("invoice_no"));
			result.setPoolRefId(rs.getString("POOL_REF_ID"));
			result.setTotPoolAmt(rs.getDouble("INVOICE_POOL_AMT"));
			result.setInvoicePoolNo(rs.getString("POOL_ID"));
			if(rs.getInt("SME_ORG_ID") > 0 && rs.getInt("BUYER_ORG_ID") > 0){
				result.setBuyerApproved(true);
			}
			
			return result;
		}

	} 

	@Override
	public Integer creditInvoice(String refId) throws Exception {
		int result = 0;
		//  Put the invoice in the  invoice pool candidate to become part of eligible candidate
		final String sql = "SELECT * FROM INVOICE_INFO II WHERE II.REF_ID = :refId AND II.IS_ELIGIBLE=0";
		MapSqlParameterSource paramMap = new MapSqlParameterSource();
		paramMap.addValue("refId", refId);
		Map<String, Object> resultMap = jdbcTemplate.queryForObject(sql, paramMap, new ColumnMapRowMapper());
		if (resultMap.size() == 1)
			result = resultMap.size();
		int invoiceId = (int) resultMap.get("invoice_id");
		final String sqlIPC = " INSERT INTO INVOICE_POOL_CANDIDATE (INVOICE_ID, INSERT_DT, REF_ID) VALUES (:invoiceId, :insertDt, :refId)";
		MapSqlParameterSource sqlIPCparamMap = new MapSqlParameterSource();
		sqlIPCparamMap.addValue("invoiceId", invoiceId);
		sqlIPCparamMap.addValue("insertDt", new Timestamp(System.currentTimeMillis()));
		sqlIPCparamMap.addValue("refId", UUIDGenerator.newRefId());
		result = jdbcTemplate.update(sqlIPC, sqlIPCparamMap);
		
		if(result > 0 ){
			final String sqlUpdateII = " UPDATE INVOICE_INFO II SET II.IS_ELIGIBLE = 1 WHERE II.REF_ID = :refId";
			MapSqlParameterSource sqlUpdateIIparamMap = new MapSqlParameterSource();
			sqlUpdateIIparamMap.addValue("refId", refId);
			jdbcTemplate.update(sqlUpdateII, sqlUpdateIIparamMap);
		}
		
		return result;
	}


	/*public Integer notifyLenders(String refId) throws Exception { // Old credit invoice
		int result = 0;
		final String sql = "SELECT * FROM INVOICE_INFO II WHERE II.REF_ID = :refId";
		MapSqlParameterSource paramMap = new MapSqlParameterSource();
		paramMap.addValue("refId", refId);
		Map<String, Object> resultMap = jdbcTemplate.queryForObject(sql, paramMap, new ColumnMapRowMapper());
		if (resultMap.size() == 1)
			result = resultMap.size();

		int invoiceId = (int) resultMap.get("invoice_id");
		Date invoiceDt = (Date) resultMap.get("invoice_start_dt");
		Date dueDt =  (Date) resultMap.get("invoice_end_dt");
		Calendar cal = Calendar.getInstance();
		cal.setTime(invoiceDt);
		cal.add(Calendar.DATE, 3);
		Date expirationDt = cal.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		String dateFormatted = sdf.format(expirationDt);
		List<Integer> financierOrgIds = orgReadDao.findAllFinancierOrgIds();
		final List<InvoiceNotificationBean> lst = new LinkedList<>();
		for(int financierOrgId : financierOrgIds){
			InvoiceNotificationBean bean = new InvoiceNotificationBean();
			bean.setExpirationDt(sdf.parse(dateFormatted));
			bean.setFinancierId(financierOrgId);
			bean.setInvoiceDt(invoiceDt);
			bean.setInvoiceId(invoiceId);
			lst.add(bean);
		}
		String batchSql = " INSERT INTO INVOICE_NOTIFICATION_LIST (INVOICE_ID, FINANCIER_ID, INSERT_DT, EXPIRATION_DT) "
				+ "VALUES (?,?,?,?)";
		jdbcTemplate.getJdbcOperations().batchUpdate(batchSql, new BatchPreparedStatementSetter() {			
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				InvoiceNotificationBean inb = (InvoiceNotificationBean) lst.get(i);
				ps.setInt(1, inb.getInvoiceId());
				ps.setInt(2, inb.getFinancierId());			
				ps.setTimestamp(3, new Timestamp(inb.getInvoiceDt().getTime()));	
				ps.setTimestamp(4, new Timestamp(inb.getExpirationDt().getTime()));				

			}			
			@Override
			public int getBatchSize() {
				return lst.size();
			}
		});
		return result;
	}*/

	@Override
	public void processInvoicePoolsInCompleteState() throws Exception{
		
		final String sql = " SELECT ID, POOL_ID FROM INVOICE_POOL_QUEUE WHERE ENQ_FLAG = 0";
		List<Map<String, Object>> resultList = jdbcTemplate.query(sql, new ColumnMapRowMapper());
		List<Integer> poolIds = new LinkedList<>();
		List<Integer> ids = new LinkedList<>();
		
		for(Map<String, Object> tmp : resultList){
			ids.add((Integer)tmp.get("ID"));
			poolIds.add((Integer)tmp.get("POOL_ID"));
		}
		int count = notifyLenders(poolIds);
		if(count > 0){
			dequeueCompletedInvoicePools(ids);
		}
	}
	
	public Integer dequeueCompletedInvoicePools(List<Integer> ids) throws Exception {
		final String dequeueSql = " UPDATE INVOICE_POOL_QUEUE SET ENQ_FLAG = 1 WHERE ID IN (:ids)";
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("ids", ids);
		return jdbcTemplate.update(dequeueSql, params);
	}
	
	public Integer notifyLenders(List<Integer> poolIds) throws Exception { // Old credit invoice
		List<Integer> financierOrgIds = orgReadDao.findAllFinancierOrgIds();
		final List<InvoiceNotificationBean> lst = new LinkedList<>();
		for( int invoicePoolId : poolIds){
			for(int financierOrgId : financierOrgIds){
				InvoiceNotificationBean bean = new InvoiceNotificationBean();
				bean.setExpirationDt(new Timestamp(System.currentTimeMillis()));
				bean.setFinancierId(financierOrgId);
				bean.setInvoiceDt(new Timestamp(System.currentTimeMillis()));
				bean.setInvoicePoolId(invoicePoolId);
				lst.add(bean);
			}
		}
		String batchSql = " INSERT INTO INVOICE_NOTIFICATION_LIST (INVOICE_POOL_ID, FINANCIER_ID, INSERT_DT, EXPIRATION_DT) "
				+ "VALUES (?,?,?,?)";
		int result[] = jdbcTemplate.getJdbcOperations().batchUpdate(batchSql, new BatchPreparedStatementSetter() {			
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				InvoiceNotificationBean inb = (InvoiceNotificationBean) lst.get(i);
				ps.setInt(1, inb.getInvoicePoolId());
				ps.setInt(2, inb.getFinancierId());			
				ps.setTimestamp(3, new Timestamp(inb.getInvoiceDt().getTime()));	
				ps.setTimestamp(4, new Timestamp(inb.getExpirationDt().getTime()));				

			}			
			@Override
			public int getBatchSize() {
				return lst.size();
			}
		});
		return result == null ? 0 : result.length;
	}
	
	
	
	private class InvoiceNotificationBean{
		private Integer invoicePoolId;
		private Date invoiceDt;
		private Date expirationDt;
		private int financierId;


		public Integer getInvoicePoolId() {
			return invoicePoolId;
		}
		public void setInvoicePoolId(Integer invoicePoolId) {
			this.invoicePoolId = invoicePoolId;
		}
		public Date getInvoiceDt() {
			return invoiceDt;
		}
		public void setInvoiceDt(Date invoiceDt) {
			this.invoiceDt = invoiceDt;
		}
		public Date getExpirationDt() {
			return expirationDt;
		}
		public void setExpirationDt(Date expirationDt2) {
			this.expirationDt = expirationDt2;
		}
		public int getFinancierId() {
			return financierId;
		}
		public void setFinancierId(int financierId) {
			this.financierId = financierId;
		}
	}

	@Override
	public ListMsg<InvoiceDtlsMsg> fetchFundedInvoices(int orgId) {
		/*final String sql = "SELECT IFC.REF_ID, IFC.INTEREST_RATE, IFC.LOAN_PERIOD, II.BUYER_NAME, II.AMOUNT, II.INVOICE_NO, "
				+ " II.INVOICE_START_DT, II.INVOICE_END_DT, OI.ACRONYM FROM INVOICE_INFO II "
				+ "	JOIN INVOICE_FINANCIER_CANDIDATES IFC "
				+ " ON II.INVOICE_ID = IFC.INVOICE_ID "
				+ " JOIN ORGANIZATION_INFO OI "
				+ " ON OI.COMPANY_ID = IFC.FINANCIER_ID "
				+ " JOIN ORGANIZATION_INFO SME "
				+ " ON SME.COMPANY_ID = II.COMPANY_ID "
				+ " WHERE II.COMPANY_ID = :orgId ORDER BY II.INVOICE_ID" ;
		*/

		final String sql = " SELECT IFC.HAIR_CUT, IFC.LOAN_AMT_OFFERED, IFC.REF_ID, IFC.INTEREST_RATE, IFC.LOAN_PERIOD, SME.ACRONYM AS BUYER_NAME, IP.INVOICE_POOL_AMT, IP.POOL_REF_ID, IP.POOL_ID, "
				+ " OI.ACRONYM "
				+ " FROM INVOICE_FINANCIER_CANDIDATES IFC "
				+ " JOIN INVOICE_POOL IP "
				+ " 	ON IFC.INVOICE_POOL_ID = IP.POOL_ID AND IP.POOL_STATUS = 1 AND IP.IS_BID_OPEN='Y' "
				+ " JOIN ORGANIZATION_INFO OI "
				+ "     ON OI.COMPANY_ID = IFC.FINANCIER_ID "
				+ " JOIN ORGANIZATION_INFO SME "
				+ "     ON SME.COMPANY_ID = IP.SUPPLIER_ID "
				+ " WHERE IP.SUPPLIER_ID = :orgId ORDER BY IP.POOL_ID ";
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("orgId", orgId);
		List<InvoiceDtlsMsg> list = jdbcTemplate.query(sql, map, new FundedInvoiceViewRowMapper());
		ListMsg<InvoiceDtlsMsg> result = new ListMsg<>(list);
		if (list.size() > 0 )
			result.setCount(list.size());
		return result;
	}


	private class FundedInvoiceViewRowMapper implements RowMapper<InvoiceDtlsMsg>{
		@Override
		public InvoiceDtlsMsg mapRow(ResultSet rs, int idx)
				throws SQLException {
			InvoiceDtlsMsg result = new InvoiceDtlsMsg();
			result.setLoanAmtOffer(rs.getDouble("LOAN_AMT_OFFERED"));
			result.setRefId(rs.getString("REF_ID"));
			result.setInterestRate(rs.getDouble("INTEREST_RATE"));
			result.setLoanPeriod(rs.getInt("LOAN_PERIOD"));
			result.setSmeCtpy(rs.getString("BUYER_NAME"));
			result.setTotPoolAmt(rs.getDouble("INVOICE_POOL_AMT"));
			result.setInvoicePoolNo(rs.getString("POOL_ID"));
	/*		result.setInvoiceNo(rs.getString("INVOICE_NO"));
			result.setStartDt(rs.getDate("INVOICE_START_DT"));
			result.setEndDt(rs.getDate("INVOICE_END_DT"));*/
			result.setFinancier(rs.getString("ACRONYM"));
			result.setPoolRefId(rs.getString("POOL_REF_ID"));
			result.setHairCut(rs.getString("HAIR_CUT"));
			return result;
		}
	}


	@Override
	public ListMsg<InvoiceDtlsMsg> fetchPendingInvoices(int orgId) {
	/*	final String sql = "SELECT II.REF_ID, II.BUYER_NAME, II.AMOUNT, II.INVOICE_NO, II.INVOICE_START_DT, II.INVOICE_END_DT, OI.ACRONYM FROM INVOICE_INFO II "
				+ "	JOIN INVOICE_NOTIFICATION_LIST INL "
				+ " ON II.INVOICE_ID = INL.INVOICE_ID "
				+ " JOIN ORGANIZATION_INFO OI "
				+ " ON OI.COMPANY_ID = INL.FINANCIER_ID "
				+ " JOIN ORGANIZATION_INFO SME "
				+ " ON SME.COMPANY_ID = II.COMPANY_ID "				
				+ " WHERE II.COMPANY_ID = :orgId "
				+ " AND (INL.INVOICE_ID, INL.FINANCIER_ID) NOT IN (SELECT INVOICE_ID, FINANCIER_ID FROM INVOICE_FINANCIER_CANDIDATES ) ORDER BY II.INVOICE_ID" ;
*/		
		final String sql = "SELECT IP.POOL_REF_ID, SME_CPTY.ACRONYM AS BUYER_NAME, IP.INVOICE_POOL_AMT, IP.POOL_ID, OI.ACRONYM "
				+ " FROM INVOICE_NOTIFICATION_LIST INL "
				+ "	JOIN INVOICE_POOL IP "
				+ " ON IP.POOL_ID = INL.INVOICE_POOL_ID AND IP.POOL_STATUS = 1 AND IP.IS_BID_OPEN='Y' "
				+ " JOIN ORGANIZATION_INFO OI "
				+ " ON OI.COMPANY_ID = INL.FINANCIER_ID "
				+ " JOIN ORGANIZATION_INFO SME "
				+ " ON SME.COMPANY_ID = IP.SUPPLIER_ID "
				+ " JOIN ORGANIZATION_INFO SME_CPTY "
				+ " ON SME_CPTY.COMPANY_ID = IP.BUYER_ID "
				+ " WHERE IP.SUPPLIER_ID = :orgId "
				+ " AND (INL.INVOICE_POOL_ID, INL.FINANCIER_ID) NOT IN (SELECT INVOICE_POOL_ID, FINANCIER_ID FROM INVOICE_FINANCIER_CANDIDATES ) "
				+ " ORDER BY IP.POOL_ID" ;

		
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("orgId", orgId);
		List<InvoiceDtlsMsg> list = jdbcTemplate.query(sql, map, new PendingInvoiceViewRowMapper());
		ListMsg<InvoiceDtlsMsg> result = new ListMsg<>(list);
		if (list.size() > 0 )
			result.setCount(list.size());
		return result;
	}

	private class PendingInvoiceViewRowMapper implements RowMapper<InvoiceDtlsMsg>{
		@Override
		public InvoiceDtlsMsg mapRow(ResultSet rs, int idx)
				throws SQLException {
			InvoiceDtlsMsg result = new InvoiceDtlsMsg();
			result.setPoolRefId(rs.getString("POOL_REF_ID"));
			//result.setRefId(rs.getString("REF_ID"));
			result.setSmeCtpy(rs.getString("BUYER_NAME"));
			result.setTotPoolAmt(rs.getDouble("INVOICE_POOL_AMT"));
			result.setInvoicePoolNo(rs.getString("POOL_ID"));
			//result.setAmount(rs.getDouble("AMOUNT"));
			//result.setInvoiceNo(rs.getString("INVOICE_NO"));
			//result.setStartDt(rs.getDate("INVOICE_START_DT"));
			//result.setEndDt(rs.getDate("INVOICE_END_DT"));
			result.setFinancier(rs.getString("ACRONYM"));
			return result;
		}
	}

	@Override
	public BaseMsg bidInvoice(InvoiceDtlsMsg msg, int financierOrgId) throws Exception {
		BaseMsg resultBaseMsg = null;
		String refId = msg.getRefId();
		final String sql = " SELECT IP.POOL_ID FROM INVOICE_POOL IP "
				+ " WHERE IP.POOL_REF_ID = :refId ";
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("refId",refId);
		Map<String, Object> result = jdbcTemplate.queryForObject(sql, params, new ColumnMapRowMapper());
		int invoicePoolId = (Integer) result.get("POOL_ID");
		
		String hairCut = msg.getHairCut();
		int hairCutPerCent = Integer.valueOf(hairCut);
		Double invoiceTotPoolAmt = msg.getTotPoolAmt();
		Double loanAmtOffered = invoiceTotPoolAmt - (((invoiceTotPoolAmt ) * hairCutPerCent)/100);
		
		final String insertSql = "INSERT INTO INVOICE_FINANCIER_CANDIDATES (INVOICE_POOL_ID, FINANCIER_ID, INTEREST_RATE, LOAN_PERIOD, REF_ID, REQUEST_ID, UPDATE_BY,"
				+ " SOURCE_APP, HAIR_CUT, LOAN_AMT_OFFERED)"
				+ " VALUES (:invoicePoolId,:financierOrgId,:interestRate,:loanPeriod,:refId, :reqId, :updateBy, :source, :hairCut, :loanAmtOffered)";

		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("invoicePoolId", invoicePoolId);
		paramMap.put("financierOrgId", financierOrgId);
		paramMap.put("interestRate", msg.getInterestRate());
		paramMap.put("loanPeriod", msg.getLoanPeriod());
		paramMap.put("refId", UUIDGenerator.newRefId());
		paramMap.put("reqId", UserContext.getRequestId());
		paramMap.put("updateBy", UserContext.getUserName());		
		paramMap.put("source", 0);		
		paramMap.put("hairCut", msg.getHairCut());
		paramMap.put("loanAmtOffered", loanAmtOffered);
		int row = jdbcTemplate.update(insertSql, paramMap);

		if(row == 1){
			resultBaseMsg = new BaseMsg();
		}

		return resultBaseMsg;
	}

	@Override
	public void manageInvoicePools() {

		// 1) Poll new invoice candidates (invoice ids)		
		final String pollNewInvoiceSql = " SELECT II.COMPANY_ID, II.BUYER_ID, II.INVOICE_ID, II.INVOICE_END_DT, II.AMOUNT FROM INVOICE_INFO II "
				+ " JOIN INVOICE_POOL_CANDIDATE IPC "
				+ " ON II.INVOICE_ID = IPC.INVOICE_ID "
				+ " WHERE CANDIDATE_STATUS = 0";
		List<InvoicePoolMapping> invoiceCandidateList = jdbcTemplate.query(pollNewInvoiceSql, new InvoicePoolCandidateRowMapper());
		Map<InvoicePoolKey, List<InvoicePoolMapping>> candidateMap = new LinkedHashMap<>();
		for( InvoicePoolMapping tmp: invoiceCandidateList){
			InvoicePoolKey iPKey = new InvoicePoolKey(tmp.getBuyerId(), tmp.getSupplierId());
			if(!candidateMap.containsKey(iPKey)){
				List<InvoicePoolMapping> lst = new LinkedList<>();
				lst.add(tmp);
				candidateMap.put(iPKey, lst);
			}else{
				candidateMap.get(iPKey).add(tmp);
			}
		}
		if(candidateMap.size() > 0){
			// 2) Get list of all incomplete invoice pools and check if this invoice can be included in any existing invoice pools
			final String incompleteInvoicePoolSql = " SELECT IP.SUPPLIER_ID, IP.BUYER_ID, IP.INVOICE_POOL_AMT, II.INVOICE_END_DT, IP.POOL_ID "
					+ " FROM INVOICE_POOL IP "
					+ " JOIN INVOICE_POOL_MEMBERS IPM "
					+ " ON IP.POOL_ID = IPM.INVOICE_POOL_ID "
					+ " JOIN INVOICE_INFO II "
					+ " ON II.INVOICE_ID = IPM.INVOICE_ID "
					+ " WHERE IP.POOL_STATUS = 0 ORDER BY IP.POOL_ID ";
			List<InvoicePoolMapping> incompleteInvoicePoolList = jdbcTemplate.query(incompleteInvoicePoolSql, new InvoicePoolMemberRowMapper());
			Map<InvoicePoolKey, Map<InvoicePool, List<InvoicePoolMapping>>> poolMap = new LinkedHashMap<>();
			for( InvoicePoolMapping tmp: incompleteInvoicePoolList){
				InvoicePoolKey iPKey = new InvoicePoolKey(tmp.getBuyerId(), tmp.getSupplierId());
				InvoicePool iPool = new InvoicePool(tmp.getPoolId(), iPKey);
				if(!poolMap.containsKey(iPKey)){
					List<InvoicePoolMapping> lst = new LinkedList<>();
					lst.add(tmp);
					Map<InvoicePool, List<InvoicePoolMapping>> tmpMap = new LinkedHashMap<>();
					tmpMap.put(iPool, lst);
					poolMap.put(iPKey, tmpMap);
				}else{
					Map<InvoicePool, List<InvoicePoolMapping>> tmpMap = poolMap.get(iPKey);
					List<InvoicePoolMapping> tmpList = new LinkedList<>();
					tmpList.add(tmp);
					if(!tmpMap.containsKey(iPool)){
						tmpMap.put(iPool, tmpList);
					}else{
						tmpMap.get(iPool).add(tmp);
					}
				}
			}		

			// 2.1 -> Loop through the candidate list and compare it with every incomplete pool in the poolMap
			for(Map.Entry<InvoicePoolKey, List<InvoicePoolMapping>> tmpcandidateMap : candidateMap.entrySet()){
				InvoicePoolKey tmpCandidateMapPoolKey = tmpcandidateMap.getKey();
				if(poolMap.containsKey(tmpCandidateMapPoolKey)){
					List<InvoicePoolMapping> tmpcandidateMapValue = tmpcandidateMap.getValue();
					Map<InvoicePool, List<InvoicePoolMapping>> eligibleCandidatePools = poolMap.get(tmpCandidateMapPoolKey);
					for(Map.Entry<InvoicePool, List<InvoicePoolMapping>> entry : eligibleCandidatePools.entrySet()){
						if(tmpcandidateMapValue.size()==0){
							break;
						}
						InvoicePool entryKey = entry.getKey();
						List<InvoicePoolMapping> entryValues = entry.getValue();
						double poolAmount = entryValues.get(0).getPoolAmount();
						int poolId = entryKey.getTmpPoolId();
						int poolStatus = entryValues.get(0).getPoolStatus();
						Map<Integer, List<Integer>> invoiceMap = new LinkedHashMap<>();
						final List<Integer> invoiceIds = new LinkedList<>();
						double newAmount = 0.0;
						for(Iterator<InvoicePoolMapping> iterator = tmpcandidateMapValue.iterator(); iterator.hasNext();){
							InvoicePoolMapping tmpcandidateMapping = iterator.next();
							double invoiceAmount = tmpcandidateMapping.getAmount();
							newAmount = invoiceAmount + poolAmount;
							final int invoiceId = tmpcandidateMapping.getInvoiceId();
							/*if( poolStatus == InvoicePoolStatus.COMPLETE.getCode() ){
								break;
								
								// create a new pool id
								//poolId = createNewInvoicePool(tmpcandidateMapping);
								//poolStatus = InvoicePoolStatus.INCOMPLETE.getCode();
							}*/
							if( newAmount <= MAX_POOL_AMOUNT ){
								if(!invoiceMap.containsKey(poolId)){
									invoiceMap.put(poolId, invoiceIds);
									invoiceIds.add(invoiceId);
									iterator.remove();
								}else{
									invoiceIds.add(invoiceId);
									iterator.remove();
								}
								poolAmount = newAmount;
								if(  newAmount == MAX_POOL_AMOUNT  ){
									// insert all the invoice ids into the pool
									insertRecordsAsPoolMembers(poolId, invoiceIds);
									updatePoolStatus(poolAmount, poolId, InvoicePoolStatus.COMPLETE.getCode());	
									dequeueInvoiceCandidates(invoiceIds);
									invoiceMap.clear();
									invoiceIds.clear();
									poolAmount = 0.0;
									poolStatus = InvoicePoolStatus.COMPLETE.getCode();
									break;
								}						
							}else{
									// Park this condition for now.
							}								
						}
						// After iterating through the candidate values;commit eligible candidates to the pool 
						if(invoiceMap.size() > 0){
							insertRecordsAsPoolMembers(poolId, invoiceIds);
							updatePoolStatus(poolAmount, poolId, InvoicePoolStatus.INCOMPLETE.getCode());	
							dequeueInvoiceCandidates(invoiceIds);
							invoiceMap.clear();
						}
					}
					// No eligible pool left in the current incomplete list of pools
					if(tmpcandidateMapValue.size() > 0){
						// Create a new Pool 
						insertRecordsAsPoolMembers(createNewInvoicePool(tmpcandidateMapValue.get(0)), 
								Arrays.asList(tmpcandidateMapValue.get(0).getInvoiceId()));
						dequeueInvoiceCandidates( Arrays.asList(tmpcandidateMapValue.get(0).getInvoiceId()) );
					
					}
				}else{ // if the supplier and buyer combination doesnt exist with a incomplete pool status
					// Create a new Pool				
					//createNewInvoicePool(tmpcandidateMap.getValue().get(0));
					insertRecordsAsPoolMembers(createNewInvoicePool(tmpcandidateMap.getValue().get(0)), 
							Arrays.asList(tmpcandidateMap.getValue().get(0).getInvoiceId()));
					dequeueInvoiceCandidates( Arrays.asList(tmpcandidateMap.getValue().get(0).getInvoiceId()) );
				}
			}
		}
		/*		for( InvoicePoolMapping tmpInvoicePoolCandidate : invoiceCandidateList){			
			InvoicePoolKey ipKeyOuter = new InvoicePoolKey(tmpInvoicePoolCandidate.getBuyerId(), tmpInvoicePoolCandidate.getSupplierId());
			for( Map.Entry<InvoicePoolKey, List<InvoicePoolMapping>> incompletPoolMap : poolMap.entrySet()){
				InvoicePoolKey tmpIncompleteInvoicePool = incompletPoolMap.getKey();
				InvoicePoolKey ipKeyInner = new InvoicePoolKey(tmpIncompleteInvoicePool.getBuyerId(), tmpIncompleteInvoicePool.getSupplierId());
				if(ipKeyInner.equals(ipKeyOuter)){
					double poolAmount = incompletPoolMap.getValue().get(0).getPoolAmount();
					double invoiceAmount = tmpInvoicePoolCandidate.getAmount();
					double newAmount = invoiceAmount + poolAmount;
					if( newAmount <= MAX_POOL_AMOUNT ){
						// Refactor as update existing pool
						final int poolId = incompletPoolMap.getValue().get(0).getPoolId();
						final int invoiceId = tmpInvoicePoolCandidate.getInvoiceId();

						//Add the invoice in the pool
						//update the loan amount value in invoice_pool
						//update the status of candidate to complete
						//check if the amount is exactly equal to MAX_POOL_AMOUNT
						// if so, mark the pool as complete and notify the investor
						final String invoicePoolSql = " INSERT INTO INVOICE_POOL_MEMBERS (INVOICE_POOL_ID, INVOICE_ID) VALUES ( ?, ? )";
						jdbcTemplate.getJdbcOperations().batchUpdate(invoicePoolSql, new BatchPreparedStatementSetter() {
							@Override
							public int getBatchSize() {
								return 1;
							}
							@Override
							public void setValues(PreparedStatement ps, int idx)
									throws SQLException {
								ps.setInt(1, poolId);
								ps.setInt(2, invoiceId);
							}
						});
						if( newAmount == MAX_POOL_AMOUNT){
							final String updateSqlInvoicePool = " UPDATE INVOICE_POOL SET INVOICE_POOL_AMT=:";
						}else{

						}

					}else{
						//Create a new pool
						//Add the invoice into that pool
						//Check the amount value against MAX_POOL_AMT
						  // if less mark the pool as complete
						//Update the status of candidate as complete
					}
				}
			}
		}*/

		/*	final String sql = " SELECT COMPANY_ID, BUYER_ID, INVOICE_ID, INVOICE_END_DT, AMOUNT FROM INVOICE_INFO WHERE INVOICE_END_DT > SYSDATE() "
				+ " AND INVOICE_END_DT < DATE_ADD(SYSDATE(), INTERVAL 4 MONTH) "
				+ " AND INVOICE_STATUS = 0 "
				+ " ORDER BY COMPANY_ID, BUYER_ID, INVOICE_ID, INVOICE_END_DT, AMOUNT ";
		List<InvoicePoolMapping> list = jdbcTemplate.query(sql, new InvoicePoolRowMapper());
		Map<InvoicePoolKey, List<InvoicePoolMapping>> poolMap2 = new LinkedHashMap<>();
		 *//*	for( InvoicePoolMapping tmp: list){
			InvoicePoolKey iPKey = new InvoicePoolKey(tmp.getBuyerId(), tmp.getSupplierId());
			if(!poolMap.containsKey(iPKey)){
				List<InvoicePoolMapping> lst = new LinkedList<>();
				lst.add(tmp);
				poolMap.put(iPKey, lst);
			}else{
				poolMap.get(iPKey).add(tmp);
			}
		}*/
		/*for(Map.Entry<InvoicePoolKey, List<InvoicePoolMapping>> entry : poolMap.entrySet()){			
			List<InvoicePoolMapping> poolMembers = entry.getValue();
			Map<InvoicePool, List<Integer>> poolInfo = processInvoicesToCreatePool(poolMembers);
			persistInvoicePools(poolInfo);
		}*/
	}


	private void dequeueInvoiceCandidates( List<Integer> invoiceIds){
		final String sql = " UPDATE INVOICE_POOL_CANDIDATE SET CANDIDATE_STATUS = 1 WHERE INVOICE_ID IN (:invoiceIds)";
		MapSqlParameterSource paramMap = new MapSqlParameterSource();
		paramMap.addValue("invoiceIds", invoiceIds);
		jdbcTemplate.update(sql, paramMap);
	}

	private void updatePoolStatus(double poolAmount, int poolId, int poolStatus) {
		final String updateSqlInvoicePool = "UPDATE INVOICE_POOL SET INVOICE_POOL_AMT = :invoicePoolAmt, POOL_STATUS = :poolStatus "
				+ " WHERE POOL_ID = :poolId";
		MapSqlParameterSource paramMap = new MapSqlParameterSource();
		paramMap.addValue("invoicePoolAmt", poolAmount);
		paramMap.addValue("poolStatus", poolStatus);
		paramMap.addValue("poolId", poolId);
		jdbcTemplate.update(updateSqlInvoicePool, paramMap);
		if(poolStatus == InvoicePoolStatus.COMPLETE.getCode()){
			final String insertSql = " INSERT INTO INVOICE_POOL_QUEUE (POOL_ID) VALUES (:poolId)"; 
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("poolId", poolId);
			jdbcTemplate.update(insertSql, params);
		}
	}


	private int createNewInvoicePool(InvoicePoolMapping tmpcandidateMapping) {
		final String invoicePoolSql = " INSERT INTO INVOICE_POOL (SUPPLIER_ID, BUYER_ID, POOL_STATUS, POOL_REF_ID, INVOICE_POOl_AMT) VALUES "
				+ " (:supplierId, :buyerId, :poolStatus, :poolRefId, :poolAmt) ";		
		KeyHolder keyHolder = new GeneratedKeyHolder();
		MapSqlParameterSource paramMap = new MapSqlParameterSource();
		paramMap.addValue("supplierId", tmpcandidateMapping.getSupplierId());
		paramMap.addValue("buyerId", tmpcandidateMapping.getBuyerId());
		paramMap.addValue("poolStatus", InvoicePoolStatus.INCOMPLETE.getCode());
		paramMap.addValue("poolRefId", UUIDGenerator.newRefId());
		paramMap.addValue("poolAmt", tmpcandidateMapping.getAmount());
		jdbcTemplate.update( invoicePoolSql, paramMap, keyHolder);			
		int poolId = keyHolder.getKey().intValue();
		return poolId ;
	}

	private void insertRecordsAsPoolMembers(final int poolId,
			final List<Integer> invoiceIds) {
		final String invoicePoolSql = " INSERT INTO INVOICE_POOL_MEMBERS (INVOICE_POOL_ID, INVOICE_ID) VALUES ( ?, ? )";


		jdbcTemplate.getJdbcOperations().batchUpdate(invoicePoolSql, new BatchPreparedStatementSetter() {
			@Override
			public int getBatchSize() {
				return invoiceIds.size();
			}
			@Override
			public void setValues(PreparedStatement ps, int idx)
					throws SQLException {
				ps.setInt(1, poolId);
				ps.setInt(2, invoiceIds.get(idx));
			}
		});
	}

	private void persistInvoicePools(Map<InvoicePool, List<Integer>> poolInfo){
		MapSqlParameterSource paramMap = null;
		final String invoicePoolSql = " INSERT INTO INVOICE_POOL (SUPPLIER_ID, BUYER_ID, POOL_STATUS, POOL_REF_ID, INVOICE_POOl_AMT) VALUES "
				+ " (:supplierId, :buyerId, :poolStatus, :poolRefId, :poolAmt) ";		
		final String invoicePoolIdSql = " SELECT LAST_INSERT_ID() AS ID ";		
		final String batchInvoiceSql = " INSERT INTO INVOICE_POOL_MEMBERS (INVOICE_POOL_ID, INVOICE_ID) VALUES ( ?, ? )";
		final String batchInvoiceUpdateSql = " UPDATE INVOICE_INFO SET INVOICE_STATUS = :invoiceStatus WHERE INVOICE_ID IN (:ids)";
		MapSqlParameterSource updateParamMap = null;
		for( Map.Entry<InvoicePool, List<Integer>> entry : poolInfo.entrySet()){
			InvoicePool invoicePool = entry.getKey();
			InvoicePoolKey iPKey = invoicePool.getInvoicePoolKey();
			paramMap = new MapSqlParameterSource();
			paramMap.addValue("supplierId", iPKey.getSupplierId());
			paramMap.addValue("buyerId", iPKey.getBuyerId());
			paramMap.addValue("poolStatus", InvoiceStatus.ACTIVE.getCode());
			paramMap.addValue("poolRefId", UUIDGenerator.newRefId());
			paramMap.addValue("poolAmt", invoicePool.getInvoicePoolAmount());
			jdbcTemplate.update(invoicePoolSql, paramMap);			
			final int idValue = jdbcTemplate.query(invoicePoolIdSql, new ResultSetExtractor<Integer>() {
				@Override
				public Integer extractData(ResultSet rs) throws SQLException,
				DataAccessException {
					return rs.getInt("ID");
				}
			});
			final List<Integer> invoiceIds = entry.getValue();
			jdbcTemplate.getJdbcOperations().batchUpdate(batchInvoiceSql, new BatchPreparedStatementSetter() {
				@Override
				public int getBatchSize() {
					return invoiceIds.size();
				}
				@Override
				public void setValues(PreparedStatement ps, int idx)
						throws SQLException {
					ps.setInt(1, idValue);
					ps.setInt(2, invoiceIds.get(idx));
				}
			});
			updateParamMap = new MapSqlParameterSource();
			updateParamMap.addValue("invoiceStatus", InvoiceStatus.POOLED.getCode());
			updateParamMap.addValue("ids", invoiceIds);
			jdbcTemplate.update(batchInvoiceUpdateSql, updateParamMap);			
		}		
	}


	private Map<InvoicePool, List<Integer>> processInvoicesToCreatePool(List<InvoicePoolMapping> poolMembers){
		Map<InvoicePool, List<Integer>> invoicePoolInfo = new LinkedHashMap<>();
		double tmpAmount = 0.0;
		int tmpPoolId = 1;
		for(InvoicePoolMapping record : poolMembers){
			tmpAmount = tmpAmount + record.getAmount();
			InvoicePoolKey poolKey = new InvoicePoolKey(record.getBuyerId(), record.getSupplierId());
			InvoicePool ip = new InvoicePool(tmpPoolId, poolKey);
			if (tmpAmount < MAX_POOL_AMOUNT){
				ip.setInvoicePoolAmount(tmpAmount);
				if(!invoicePoolInfo.containsKey(ip)){
					List<Integer> lst = new LinkedList<>();
					lst.add(record.getInvoiceId());
					invoicePoolInfo.put(ip, lst);					
				}else{ // Same pool - as the aggregate amount is less than the maximum pool amount. Add the invoice id and continue
					invoicePoolInfo.get(ip).add(record.getInvoiceId());
				}
			}else{ // New Pool for the same supplier and buyer; Reset the tmpPoolId and tmpAmount
				tmpAmount = record.getAmount();
				tmpPoolId = tmpPoolId + 1;
				ip = new InvoicePool(tmpPoolId, poolKey);
				ip.setInvoicePoolAmount(tmpAmount);
				List<Integer> lst = new LinkedList<>();
				lst.add(record.getInvoiceId());
				invoicePoolInfo.put(ip, lst);
			}
		}
		return invoicePoolInfo;
	}

	private class InvoicePoolMemberRowMapper implements RowMapper<InvoicePoolMapping>{

		@Override
		public InvoicePoolMapping mapRow(ResultSet rs, int idx)
				throws SQLException {
			InvoicePoolMapping result = new InvoicePoolMapping();
			result.setSupplierId(rs.getInt("SUPPLIER_ID"));
			result.setBuyerId(rs.getInt("BUYER_ID"));
			result.setPoolAmount(rs.getDouble("INVOICE_POOL_AMT"));
			result.setPoolId(rs.getInt("POOL_ID"));
			result.setEndDt(rs.getDate("INVOICE_END_DT"));
			return result;
		}
	}

	private class InvoicePoolCandidateRowMapper implements RowMapper<InvoicePoolMapping>{
		@Override
		public InvoicePoolMapping mapRow(ResultSet rs, int idx)
				throws SQLException {
			InvoicePoolMapping result = new InvoicePoolMapping();
			result.setSupplierId(rs.getInt("COMPANY_ID"));
			result.setBuyerId(rs.getInt("BUYER_ID"));
			result.setAmount(rs.getDouble("AMOUNT"));
			result.setInvoiceId(rs.getInt("INVOICE_ID"));
			result.setEndDt(rs.getDate("INVOICE_END_DT"));
			return result;
		}
	}

	@Override
	public ListMsg<InvoiceDtlsMsg> fetchInvoicePoolDtls(int orgId,
			String poolRefId) {
		
		final String sql = " SELECT II.PURCHASE_ORDER_NO, II.REF_ID, II.DESCRIPTION, II.INVOICE_START_DT, II.INVOICE_END_DT, II.AMOUNT, II.BUYER_NAME, II.INVOICE_NO, SME.ACRONYM FROM INVOICE_POOL IP "
				+ " JOIN INVOICE_POOL_MEMBERS IPM "
				+ " ON IP.POOL_ID = IPM.INVOICE_POOL_ID "
				+ " JOIN INVOICE_INFO II "
				+ " ON II.INVOICE_ID = IPM.INVOICE_ID "
				+ " JOIN ORGANIZATION_INFO SME "
				+ " ON SME.COMPANY_ID = II.COMPANY_ID "
				+ " WHERE IP.POOL_REF_ID = :poolRefId";
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("poolRefId", poolRefId);
		List<InvoiceDtlsMsg> list = jdbcTemplate.query(sql, map, new InvoiceInfoRowMapper());
		return new ListMsg<>(list);
	}

	@Override
	public BaseMsg createInvoiceAccount(InvoiceAccountInfoMsg msg) {
		BaseMsg responseMsg = null;
		Map<String, Object> map = orgReadDao.findOrgId(msg.getAcro());
		int orgId = (Integer) map.get("company_id");
		final String sql = "INSERT INTO FI_ACCOUNT (FI_ACCT_REF_ID, CREATE_DT, SME_ORG_ID, ACCT_STATUS, CREATED_BY) VALUES "
				+ "( :fiAcctRefId, :createDt, :smeOrgId, :acctStatus, :createdBy )";
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("fiAcctRefId", UUIDGenerator.newRefId());
		params.addValue("createDt", new Timestamp(System.currentTimeMillis()));
		params.addValue("smeOrgId", orgId);
		params.addValue("acctStatus", InvoiceAcctStatus.ACTIVE.getCode());
		params.addValue("createdBy", UserContext.getUserName());
		int row = jdbcTemplate.update(sql, params);
		if(row == 1){
			responseMsg = new BaseMsg();
		}
		return responseMsg;
	}

	@Override
	public ListMsg<InvoiceDtlsMsg> fetchPaidInvoicePools(String acronym) throws Exception {
		
		Map<String, Object> smeMap = orgReadDao.findOrgId(acronym);
		int smeOrgId = (Integer) smeMap.get("company_id");		
		final String sql = "SELECT IP.POOL_REF_ID, SME_CPTY.ACRONYM AS BUYER_NAME, IP.INVOICE_POOL_AMT, OI.ACRONYM AS FINANCIER, LI.LOAN_REF_ID "
				+ " FROM INVOICE_POOL IP "
				+ "	JOIN LOAN_INFO LI "
				+ " ON IP.POOL_ID = LI.INVOICE_POOL_ID AND LI.LOAN_STATUS = 2 "
				+ " JOIN ORGANIZATION_INFO OI "
				+ " ON OI.COMPANY_ID = LI.FINANCIER_ORG_ID "
				+ " JOIN ORGANIZATION_INFO SME "
				+ " ON SME.COMPANY_ID = IP.SUPPLIER_ID "
				+ " JOIN ORGANIZATION_INFO SME_CPTY "
				+ " ON SME_CPTY.COMPANY_ID = IP.BUYER_ID "
				+ " WHERE IP.SUPPLIER_ID = :orgId "
				+ " ORDER BY IP.POOL_ID" ;
		MapSqlParameterSource paramMap = new MapSqlParameterSource();
		paramMap.addValue("orgId", smeOrgId);
		List<InvoiceDtlsMsg> list = jdbcTemplate.query(sql, paramMap, new PaidInvoiceViewRowMapper());
		return new ListMsg<>(list);
	}
	
	private class PaidInvoiceViewRowMapper implements RowMapper<InvoiceDtlsMsg>{
		@Override
		public InvoiceDtlsMsg mapRow(ResultSet rs, int idx)
				throws SQLException {
			InvoiceDtlsMsg result = new InvoiceDtlsMsg();
			result.setPoolRefId(rs.getString("POOL_REF_ID"));
			result.setSmeCtpy(rs.getString("BUYER_NAME"));
			result.setTotPoolAmt(rs.getDouble("INVOICE_POOL_AMT"));
			result.setFinancier(rs.getString("FINANCIER"));
			result.setLoanRefId(rs.getString("LOAN_REF_ID"));
			return result;
		}
	}

	@Override
	public Map<String, Object> findInfoByFinancierCandidateRefId( String candidateRefId){
		final String invoiceInfoSql = " SELECT IP.POOL_REF_ID, IFC.INVOICE_POOL_ID, IFC.CANDIDATE_ID, IFC.FINANCIER_ID FROM INVOICE_FINANCIER_CANDIDATES IFC "
				+ " JOIN INVOICE_POOL IP "
				+ "  ON IP.POOL_ID = IFC.INVOICE_POOL_ID "
				+ " WHERE IFC.REF_ID = :candidateRefId ";
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("candidateRefId", candidateRefId);
		Map<String, Object> map = jdbcTemplate.queryForObject(invoiceInfoSql, params, new ColumnMapRowMapper());
		return map;
	}
	
	public void notifyInvoiceBidsClosed( int poolId ){
		final String nonBidedFinanciers = " UPDATE INVOICE_POOL IP SET IS_BID_OPEN = 'N' WHERE IP.POOL_ID = :poolId ";
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("poolId", poolId);
		jdbcTemplate.update(nonBidedFinanciers, params);
		
		
	}

	@Override
	public void mapInvoiceToFinancierForFunding(int poolId, int candidateId,
			int financierId) {

		final String sql = "INSERT INTO INVOICE_FINANCIER_MAP (CANDIDATE_ID, INVOICE_POOL_ID, STATUS, INSERT_DT, "
				+ " REF_ID, REQUEST_ID, USER_ID, SOURCE_APP) VALUES (:candidateId, :invoicePoolId, :status, :insertDt, "
				+ " :refId, :reqId, :userId, :source)";		
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("candidateId", candidateId);
		params.addValue("invoicePoolId", poolId);
		params.addValue("status", InvoiceFinancierMapStatus.LINKED.getCode());
		params.addValue("insertDt", new Timestamp(System.currentTimeMillis()));
		params.addValue("refId", UUIDGenerator.newRefId());
		params.addValue("reqId", UserContext.getRequestId());
		params.addValue("userId", UserContext.getUserName());
		params.addValue("source", UserContext.getSourceApp());
		jdbcTemplate.update(sql, params);
		
	}

	@Override
	public ListMsg<InvoiceDtlsMsg> fetchAcceptedBidBySME(String acronym)
			throws Exception {
		Map<String, Object> map = orgReadDao.findOrgId(acronym);
		int financierOrgId = (Integer) map.get("company_id");
		final String sql = " SELECT IP.POOL_REF_ID, IP.POOL_ID, IP.INVOICE_POOL_AMT, SME.ACRONYM AS SMEACRO, BUYER.ACRONYM AS BUYERACRO, IFC.LOAN_AMT_OFFERED, "
				+ " IFC.INTEREST_RATE, IFC.LOAN_PERIOD "
				+ " FROM INVOICE_FINANCIER_MAP IFM JOIN "
				+ " INVOICE_POOL IP "
				+ " ON IFM.INVOICE_POOL_ID = IP.POOL_ID "
				+ " JOIN INVOICE_FINANCIER_CANDIDATES IFC "
				+ " ON IFM.CANDIDATE_ID = IFC.CANDIDATE_ID "				
				+ " JOIN ORGANIZATION_INFO SME "
				+ " ON SME.COMPANY_ID = IP.SUPPLIER_ID "
				+ " JOIN ORGANIZATION_INFO BUYER "
				+ " ON BUYER.COMPANY_ID = IP.BUYER_ID "
				+ " WHERE IFC.FINANCIER_ID = :financierOrgId AND "
				+ " IP.POOL_ID NOT IN (SELECT INVOICE_POOL_ID FROM LOAN_INFO LI WHERE LI.FINANCIER_ORG_ID = :financierOrgId ) ";
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("financierOrgId", financierOrgId);
		List<InvoiceDtlsMsg> lst = jdbcTemplate.query(sql, params, new FinancierLoanApprovalViewRowMapper());
		return new ListMsg<>(lst);
	}
	
	private class FinancierLoanApprovalViewRowMapper implements RowMapper<InvoiceDtlsMsg>{

		@Override
		public InvoiceDtlsMsg mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			InvoiceDtlsMsg result = new InvoiceDtlsMsg();
			result.setLoanAmtOffer(rs.getDouble("LOAN_AMT_OFFERED"));
			result.setInterestRate(rs.getDouble("INTEREST_RATE"));
			/*result.setDesc(rs.getString("description"));
			*/
			result.setSmeCtpy(rs.getString("BUYERACRO"));
			result.setSme(rs.getString("SMEACRO"));
			result.setPoolRefId(rs.getString("POOL_REF_ID"));
			result.setTotPoolAmt(rs.getDouble("INVOICE_POOL_AMT"));
			result.setInvoicePoolNo(rs.getString("POOL_ID"));
			result.setLoanPeriod(rs.getInt("LOAN_PERIOD"));
			return result;
		}

	}

	@Override
	public ListMsg<LoanDtlsMsg> viewFundedInvoicesByFinancier( int orgId ) {
		final String sql = " SELECT LI.LOAN_ID, LI.LOAN_REF_ID, LI.LOAN_STATUS, LI.LOAN_AMT, LI.LOAN_DISPATCH_DT, SME.ACRONYM, IP.POOL_REF_ID, IP.POOL_ID, "
				+ " LI.LOAN_CLOSE_DT FROM LOAN_INFO LI "
				+ " JOIN ORGANIZATION_INFO SME "
				+ " ON SME.COMPANY_ID = LI.SME_ORG_ID "
				+ " JOIN INVOICE_POOL IP "
				+ " ON IP.POOL_ID = LI.INVOICE_POOL_ID "
				+ " WHERE LI.FINANCIER_ORG_ID = :financierOrgId order by LI.SME_ORG_ID ";
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("financierOrgId", orgId);
		List<LoanDtlsMsg> lst = jdbcTemplate.query(sql, params, new FinancierLoanListViewRowMapper());
		return new ListMsg<>(lst);
	} 
	
	private class FinancierLoanListViewRowMapper implements RowMapper<LoanDtlsMsg>{

		@Override
		public LoanDtlsMsg mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			LoanDtlsMsg result = new LoanDtlsMsg();
			result.setLoanId(rs.getInt("LOAN_ID"));
			result.setRefId(rs.getString("LOAN_REF_ID"));
			result.setLoanAmt(rs.getDouble("LOAN_AMT"));
			result.setLoanDispatchDt(rs.getDate("LOAN_DISPATCH_DT"));
			result.setLoanCloseDt(rs.getDate("LOAN_CLOSE_DT"));
			result.setPoolId(rs.getString("POOL_ID"));
			result.setLoanStatus(LoanStatus.fromCode(rs.getInt("LOAN_STATUS")).getText());
			result.setPoolRefId(rs.getString("POOL_REF_ID"));
			result.setSmeAcro(rs.getString("ACRONYM"));
			return result;
		}

	}

	@Override
	public ListMsg<InvoiceDtlsMsg> fetchPendingInvoicesForApproval(
			int buyerOrgId) {
		
		final String sql = " SELECT II.AMOUNT, II.DESCRIPTION, BUYERORG.ACRONYM AS buyer_name, "
				+ " II.INVOICE_START_DT, II.INVOICE_END_DT, SME.ACRONYM, II.INVOICE_NO, II.REF_ID, II.PURCHASE_ORDER_NO "
				+ " FROM INVOICE_INFO II "
				+ "JOIN ORGANIZATION_INFO BUYERORG "
				+ "ON II.BUYER_ID = BUYERORG.COMPANY_ID "
				+ "JOIN  ORGANIZATION_INFO SME "
				+ "ON II.COMPANY_ID = SME.COMPANY_ID "
				+ "AND BUYERORG.COMPANY_ID = :buyerOrgId "
				+ "AND II.IS_ELIGIBLE = 2 " // Value 2 implies Pending Approval 
				+ "ORDER BY II.COMPANY_ID "; 
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("buyerOrgId", buyerOrgId);
		List<InvoiceDtlsMsg> list = jdbcTemplate.query(sql, map, new InvoiceInfoRowMapper());
		return new ListMsg<>(list);		
	}

	@Override
	public int managePendingInvoices(String invoiceRefId, int action) {
		
		final String sql = "UPDATE INVOICE_INFO II SET II.IS_ELIGIBLE = :action WHERE II.REF_ID = :refId";
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("action", action);
		map.addValue("refId", invoiceRefId);
		int result = jdbcTemplate.update(sql, map);
		return result;
	}

	@Override
	public ListMsg<InvoiceDtlsMsg> viewBuyerApprovedInvoices(int buyerOrgId) {
		final String sql = " SELECT II.AMOUNT, II.DESCRIPTION, BUYERORG.ACRONYM AS buyer_name, "
				+ " II.INVOICE_START_DT, II.INVOICE_END_DT, SME.ACRONYM, II.INVOICE_NO, II.REF_ID, II.PURCHASE_ORDER_NO "
				+ " FROM INVOICE_INFO II "
				+ "JOIN ORGANIZATION_INFO BUYERORG "
				+ "ON II.BUYER_ID = BUYERORG.COMPANY_ID "
				+ "JOIN  ORGANIZATION_INFO SME "
				+ "ON II.COMPANY_ID = SME.COMPANY_ID "
				+ "AND BUYERORG.COMPANY_ID = :buyerOrgId "
				+ "AND II.IS_ELIGIBLE = 0 "
				+ "AND II.BUYER_APPROVAL = 'Y' "
				+ "ORDER BY II.COMPANY_ID "; 
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("buyerOrgId", buyerOrgId);
		List<InvoiceDtlsMsg> list = jdbcTemplate.query(sql, map, new InvoiceInfoRowMapper());
		return new ListMsg<>(list);		
	}

	@Override
	public ListMsg<InvoiceDtlsMsg> viewBuyerRejectedInvoices(int buyerOrgId) {
		final String sql = " SELECT II.AMOUNT, II.DESCRIPTION, BUYERORG.ACRONYM AS buyer_name, "
				+ " II.INVOICE_START_DT, II.INVOICE_END_DT, SME.ACRONYM, II.INVOICE_NO, II.REF_ID, II.PURCHASE_ORDER_NO "
				+ " FROM INVOICE_INFO II "
				+ "JOIN ORGANIZATION_INFO BUYERORG "
				+ "ON II.BUYER_ID = BUYERORG.COMPANY_ID "
				+ "JOIN  ORGANIZATION_INFO SME "
				+ "ON II.COMPANY_ID = SME.COMPANY_ID "
				+ "AND BUYERORG.COMPANY_ID = :buyerOrgId "
				+ "AND II.IS_ELIGIBLE = 3 "
				+ "ORDER BY II.COMPANY_ID "; 
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("buyerOrgId", buyerOrgId);
		List<InvoiceDtlsMsg> list = jdbcTemplate.query(sql, map, new InvoiceInfoRowMapper());
		return new ListMsg<>(list);		
	}

	@Override
	public ListMsg<InvoiceDtlsMsg> viewBuyerAllegedInvoices(int buyerOrgId) {
		final String sql = " SELECT II.AMOUNT, II.DESCRIPTION, BUYERORG.ACRONYM AS buyer_name, "
				+ " II.INVOICE_START_DT, II.INVOICE_END_DT, SME.ACRONYM, II.INVOICE_NO, II.REF_ID, II.PURCHASE_ORDER_NO "
				+ " FROM INVOICE_INFO II "
				+ "JOIN ORGANIZATION_INFO BUYERORG "
				+ "ON II.BUYER_ID = BUYERORG.COMPANY_ID "
				+ "JOIN  ORGANIZATION_INFO SME "
				+ "ON II.COMPANY_ID = SME.COMPANY_ID "
				+ "AND BUYERORG.COMPANY_ID = :buyerOrgId "
				+ "AND II.IS_ELIGIBLE = 1 "
				+ "AND II.BUYER_APPROVAL = 'Y' "
				+ "ORDER BY II.COMPANY_ID "; 
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("buyerOrgId", buyerOrgId);
		List<InvoiceDtlsMsg> list = jdbcTemplate.query(sql, map, new InvoiceInfoRowMapper());
		return new ListMsg<>(list);	
	}

}
