package com.floatinvoice.business.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.sql.DataSource;

import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.lob.LobCreator;
import org.springframework.jdbc.support.lob.LobHandler;
import org.springframework.stereotype.Repository;

import com.floatinvoice.common.InvoiceCounterPartyObject;
import com.floatinvoice.common.UUIDGenerator;
import com.floatinvoice.messages.BaseMsg;
import com.floatinvoice.messages.FileDetails;
import com.floatinvoice.messages.UploadMessage;

@Repository
public class JdbcInvoiceFileUploadDao implements InvoiceFileUploadDao {

	
	private NamedParameterJdbcTemplate jdbcTemplate;
	private LobHandler lobHandler;
	private OrgReadDao orgReadDao;
	
	final static String sql = "INSERT INTO FILE_STORE (FILE_NAME, FILE_BYTES, INSERT_DT, COMPANY_ID, USER_ID, REF_ID, REQUEST_ID, SOURCE_APP) "
			+ "values (?, ?, ?, ?, ?, ?, ?, ?)";
	
	public JdbcInvoiceFileUploadDao() {
	}
	
	public JdbcInvoiceFileUploadDao(  DataSource dataSource, LobHandler lobHandler, OrgReadDao orgReadDao ) {
		 jdbcTemplate = new NamedParameterJdbcTemplate( dataSource );
		 this.lobHandler = lobHandler;
		 this.orgReadDao = orgReadDao;
	}
	
	private class InvoiceInfoBean{
		
		private Date invoiceDt;
		private Date dueDt;
		private Double amount;
		private String invoiceNo;
		private String custName;
		private String description;
		private String poNumber;
		
		public Date getInvoiceDt() {
			return invoiceDt;
		}
		public void setInvoiceDt(Date invoiceDt) {
			this.invoiceDt = invoiceDt;
		}
		public Date getDueDt() {
			return dueDt;
		}
		public void setDueDt(Date dueDt) {
			this.dueDt = dueDt;
		}
		public Double getAmount() {
			return amount;
		}
		public void setAmount(Double amount) {
			this.amount = amount;
		}
		public String getInvoiceNo() {
			return invoiceNo;
		}
		public void setInvoiceNo(String invoiceNo) {
			this.invoiceNo = invoiceNo;
		}
		public String getCustName() {
			return custName;
		}
		public void setCustName(String custName) {
			this.custName = custName;
		}
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
		public String getPoNumber() {
			return poNumber;
		}
		public void setPoNumber(String poNumber) {
			this.poNumber = poNumber;
		}
		
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void uploadInvoiceRecords(Map<String, List<Object>> invoiceDtMap, final int orgId, final int fileId){
		
    	LinkedList<Date> invoiceDt = (LinkedList) invoiceDtMap.get("INVOICE_DATE");   
    	LinkedList<Date> dueDt = (LinkedList) invoiceDtMap.get("DUE_DATE");	
    	LinkedList<Double> amt = (LinkedList) invoiceDtMap.get("AMOUNT");	
    	LinkedList<String> invoiceNo = (LinkedList) invoiceDtMap.get("INVOICE_NO");		
    	LinkedList<String> purchaseOrderNo = (LinkedList) invoiceDtMap.get("PURCHASE_ORDER_NO");					
	   	LinkedList<String> custName = (LinkedList) invoiceDtMap.get("CUSTOMER_NAME");	
    	custName.remove("CUSTOMER_NAME");
    	LinkedList<String> desc = (LinkedList) invoiceDtMap.get("DESCRIPTION");
		final List<InvoiceInfoBean> lst = new LinkedList<>();
		int size = invoiceDt.size();
		for(int i =0; i<size; i++){
			InvoiceInfoBean bean = new InvoiceInfoBean();
			bean.setAmount(amt.get(i));
			bean.setCustName(custName.get(i));
			bean.setDescription(desc.get(i));
			bean.setDueDt(dueDt.get(i));
			bean.setInvoiceDt(invoiceDt.get(i));
			bean.setInvoiceNo(invoiceNo.get(i));
			bean.setPoNumber(purchaseOrderNo.get(i));
			lst.add(bean);			
		}
		String batchSql = " INSERT INTO INVOICE_INFO (INVOICE_START_DT, INVOICE_END_DT, AMOUNT, INVOICE_NO, COMPANY_ID, FILE_ID, BUYER_NAME, "
				+ " BUYER_ID, BUYER_APPROVAL, INSERT_DT, DESCRIPTION, REF_ID, REQUEST_ID, USER_ID, SOURCE_APP, PURCHASE_ORDER_NO, IS_ELIGIBLE) "
				+ " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
		jdbcTemplate.getJdbcOperations().batchUpdate(batchSql, new BatchPreparedStatementSetter() {	
			
			List<InvoiceCounterPartyObject> approvalRequiredList = new LinkedList<>(); 
			List<InvoiceCounterPartyObject> approvalNotRequiredList = new LinkedList<>(); 

			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				InvoiceInfoBean iib = (InvoiceInfoBean) lst.get(i);
				int buyerOrgId = (int) orgReadDao.findOrgId(iib.getCustName().trim()).get("COMPANY_ID");
				InvoiceCounterPartyObject parties = new InvoiceCounterPartyObject(orgId /*smeOrgId*/, buyerOrgId);
				boolean cptyApprovalRequired = false;
				if(approvalRequiredList.contains(parties)){
					cptyApprovalRequired = true;
				}else{
					if(approvalNotRequiredList.contains(parties)){
						cptyApprovalRequired = false;
					}else{
						final String sql = " SELECT COUNT(*) FROM SME_BUYER_APPROVAL_MAP M WHERE M.SME_ORG_ID = :smeOrgId "
								+ "AND M.BUYER_ORG_ID = :buyerOrgId ";
						MapSqlParameterSource params = new MapSqlParameterSource();
						params.addValue("smeOrgId", orgId);
						params.addValue("buyerOrgId", buyerOrgId);
						int result = jdbcTemplate.queryForObject(sql, params, Integer.class);
						if(result == 0){
							approvalNotRequiredList.add(parties);
						}else{
							cptyApprovalRequired = true;
							approvalRequiredList.add(parties);
						}
					}
				}
				ps.setTimestamp(1, new Timestamp(iib.getInvoiceDt().getTime()));				
				ps.setTimestamp(2, new Timestamp(iib.getDueDt().getTime()));
				ps.setDouble(3, iib.getAmount());
				ps.setString(4, iib.getInvoiceNo());
				ps.setLong(5, orgId);
				ps.setLong(6, fileId);
				ps.setString(7, iib.getCustName());
				ps.setInt(8, buyerOrgId);
				ps.setString(9, cptyApprovalRequired ? "Y": "N");
				ps.setTimestamp(10, new Timestamp(System.currentTimeMillis()));	
				ps.setString(11, iib.getDescription());
				ps.setString(12, UUIDGenerator.newRefId());
				ps.setString(13, UUID.randomUUID().toString());
				ps.setInt(14, 1);
				ps.setInt(15, 0);
				ps.setString(16, iib.getPoNumber());
				ps.setInt(17, cptyApprovalRequired ? 2:0);
				
			}			
			@Override
			public int getBatchSize() {
				return lst.size();
			}
		});
		
		
	}
	
	@Override
	public BaseMsg fileUpload(final UploadMessage msg) throws Exception {
		Map<String, Object> orgInfo = orgReadDao.findOrgId(msg.getSmeAcronym());
		final int orgId = (int) orgInfo.get("COMPANY_ID");
		final LobCreator lobCreator = lobHandler.getLobCreator();
		final byte [] bytes = msg.getFile().getBytes();
		jdbcTemplate.getJdbcOperations().update( new PreparedStatementCreator() {			
			@Override
			public PreparedStatement createPreparedStatement(Connection conn)
					throws SQLException {

				final PreparedStatement ps = conn.prepareStatement(sql);
				try {
					ps.setString(1, msg.getFileName());
					lobCreator.setBlobAsBytes(ps, 2, bytes);
					ps.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
					ps.setInt(4, orgId);
					ps.setInt(5, 1);			
					ps.setString(6, UUIDGenerator.newRefId());
					ps.setString(7, UUID.randomUUID().toString());
					ps.setInt(8, 0);
				} catch (SQLException e) {
					throw e;
				}
				return ps;
			}
		});
		lobCreator.close();
		BaseMsg response = new BaseMsg();	
		response.addInfoMsg("File uploaded successfully", HttpStatus.OK.value());
		return response;
	}

	@Override
	public List<FileDetails> pollEligibleFiles() {

		List<FileDetails> result = new LinkedList<>();
		final String sql = "SELECT * FROM FILE_STORE WHERE ENQ_FLAG=0 LIMIT 10"; 
		List<Map<String, Object>> fileList = jdbcTemplate.query(sql, new ColumnMapRowMapper());
		
		for (Map<String, Object> fileObject : fileList){		
			FileDetails fDetails = new FileDetails();
			fDetails.setFileId((int)fileObject.get("FILE_ID"));
			fDetails.setFileName((String)fileObject.get("FILE_NAME"));
			fDetails.setFileBytes((byte [])fileObject.get("FILE_BYTES"));
			fDetails.setCompanyId((int)fileObject.get("COMPANY_ID"));
			fDetails.setUserId( (int)fileObject.get("USER_ID"));
			result.add(fDetails);
		}
		return result;
	}

	@Override
	public int markFileProcessingAsComplete(FileDetails fileDetails) {
		final String sql = "UPDATE FILE_STORE SET ENQ_FLAG = 1 WHERE FILE_ID = :fileId";
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("fileId", fileDetails.getFileId());
		int row = jdbcTemplate.update(sql, params);
		return row;
	}


	

}
