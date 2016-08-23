package com.floatinvoice.business.dao;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.floatinvoice.common.BankAccountType;
import com.floatinvoice.common.LoanStatus;
import com.floatinvoice.common.PaymentStatus;
import com.floatinvoice.common.UUIDGenerator;
import com.floatinvoice.common.UserContext;
import com.floatinvoice.messages.BankDtlsMsg;
import com.floatinvoice.messages.BaseMsg;
import com.floatinvoice.messages.LoanDtlsMsg;
import com.floatinvoice.messages.LoanInstallmentsDtlsMsg;

public class JdbcBankInfoDao implements BankInfoDao {
	
	private OrgReadDao orgReadDao;
	private InvoiceInfoDao invoiceInfoDao;
	private NamedParameterJdbcTemplate jdbcTemplate;


	public JdbcBankInfoDao(){
		
	}
	
	public JdbcBankInfoDao( OrgReadDao orgReadDao, InvoiceInfoDao invoiceInfoDao, DataSource dataSource ){
		this.orgReadDao = orgReadDao;
		this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
		this.invoiceInfoDao = invoiceInfoDao;
	}
	
	@Override
	public List<BankDtlsMsg> fetchBankDetails(int orgId) {
		final String sql = "SELECT * FROM SME_BANK_INFO WHERE SME_ORG_ID = :smeOrgId";
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("smeOrgId", orgId);
		return jdbcTemplate.query(sql, params, new BankRowMapper());
	}
	
	private class BankRowMapper implements RowMapper<BankDtlsMsg>{
		@Override
		public BankDtlsMsg mapRow(ResultSet rs, int arg1) throws SQLException {
			BankDtlsMsg bankDtls = new BankDtlsMsg();
			bankDtls.setAcctType( BankAccountType.fromCode(rs.getInt("ACCT_TYPE")).getText());
			bankDtls.setBankAcctNo(rs.getString("BANK_ACCT_NO"));
			bankDtls.setBankName(rs.getString("BANK_NAME"));
			bankDtls.setBranchName(rs.getString("BRANCH_NAME"));
			bankDtls.setIfscCode(rs.getString("IFSC_CODE"));
			bankDtls.setRefId(rs.getString("BANK_REF_ID"));
			return bankDtls;
		}
	}

	@Override
	public BaseMsg saveBankInfo(BankDtlsMsg bankDetails) {
		BaseMsg msg = null;
		Map<String, Object> map = orgReadDao.findOrgId(bankDetails.getAcro());
		int orgId = (Integer) map.get("company_id");
		final String sql = "INSERT INTO SME_BANK_INFO(BANK_ACCT_NO, BANK_NAME, IFSC_CODE, BRANCH_NAME, SME_ORG_ID, ACCT_TYPE, BANK_REF_ID, CREATED_BY, CREATE_DT)"
				+ " VALUES (:bankAcctNo, :bankName, :ifscCode, :branchName, :smeOrgId, :acctType, :bankRefId, :createdBy, :createDt)";
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("bankAcctNo", bankDetails.getBankAcctNo());
		params.addValue("bankName", bankDetails.getBankName());
		params.addValue("ifscCode", bankDetails.getIfscCode());
		params.addValue("branchName", bankDetails.getBranchName());
		params.addValue("smeOrgId", orgId);
		params.addValue("acctType", BankAccountType.valueOf(bankDetails.getAcctType()).getCode());
		params.addValue("bankRefId", UUIDGenerator.newRefId());
		params.addValue("createdBy", UserContext.getUserName());
		params.addValue("createDt", new Timestamp(System.currentTimeMillis()));
		int row = jdbcTemplate.update(sql, params);
		if (row == 1){
			msg = new BaseMsg();
			msg.addInfoMsg("Bank account saved successfully", HttpStatus.OK.value());
		}
		return msg;
	}

	@Override
	public BaseMsg approveLoan(LoanDtlsMsg loanDtlsMsg) {
		BaseMsg msg = null;
		Map<String, Object> smeMap = orgReadDao.findOrgId(loanDtlsMsg.getSmeAcro());
		int smeOrgId = (Integer) smeMap.get("company_id");
		
		Map<String, Object> financierMap = orgReadDao.findOrgId(loanDtlsMsg.getFinancierAcro());
		int financierOrgId = (Integer) financierMap.get("company_id");
		
		Map<String, Object> invoiceInfoMap =invoiceInfoDao.findInvoicePoolByRefId(loanDtlsMsg.getPoolRefId());
		final int poolId = (int) invoiceInfoMap.get("POOL_ID");
		final String sql = "INSERT INTO LOAN_INFO(LOAN_REF_ID, LOAN_STATUS, LOAN_AMT, LOAN_DISPATCH_DT, LOAN_CLOSE_DT, SME_ORG_ID, FINANCIER_ORG_ID, "
				+ " CREATE_DT, CREATED_BY, INVOICE_POOL_ID )"
				+ " VALUES (:loanRefId, :loanStatus, :loanAmt, :loanDispatchDt, :loanCloseDt, :smeOrgId, :financierOrgId, :createDt, :createdBy, :poolId)";
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("loanRefId", UUIDGenerator.newRefId());
		params.addValue("loanStatus", LoanStatus.ACTIVE.getCode());
		params.addValue("loanAmt", loanDtlsMsg.getLoanAmt());
		params.addValue("loanDispatchDt", loanDtlsMsg.getLoanDispatchDt());
		params.addValue("loanCloseDt", loanDtlsMsg.getLoanCloseDt());
		params.addValue("smeOrgId", smeOrgId);
		params.addValue("financierOrgId", financierOrgId);
		params.addValue("createdBy", UserContext.getUserName());
		params.addValue("createDt", new Timestamp(System.currentTimeMillis()));
		params.addValue("poolId", poolId);
		int row = jdbcTemplate.update(sql, params);
		if (row == 1){
			msg = new BaseMsg();
		}
		return msg;
	}

	@Override
	public BaseMsg payLoanInstallment(LoanInstallmentsDtlsMsg installment) {
		String loanRefId = installment.getLoanRefId();
		Map<String, Object> smeMap = orgReadDao.findOrgId(installment.getSmeAcro());
		int smeOrgId = (Integer) smeMap.get("company_id");
		final String sqlStr = "SELECT LOAN_ID, LOAN_AMT FROM LOAN_INFO WHERE LOAN_REF_ID = :loanRefId and SME_ORG_ID = :smeOrgId";
		MapSqlParameterSource paramMap = new MapSqlParameterSource();
		paramMap.addValue("loanRefId", loanRefId);
		paramMap.addValue("smeOrgId", smeOrgId);
		Map<String, Object> result = jdbcTemplate.queryForObject(sqlStr, paramMap, new ColumnMapRowMapper());
		int loanId = (int) result.get("LOAN_ID");
		
		BigDecimal loanAmt = (BigDecimal) result.get("LOAN_AMT");
		if( loanAmt.compareTo(BigDecimal.valueOf(installment.getAmt())) == -1 ){
			throw new IllegalArgumentException("Installment amount exceeds loan amount");
		}
		BaseMsg msg = null;
		final String sql = "INSERT INTO LOAN_INSTALLMENT(LOAN_ID, PAYMENT_DUE_DT, PAID_DT, FEES, PAID_AMOUNT, PAID_BY, STATUS)"
				+ " VALUES (:loanId, :paymentDueDt, :paidDt, :fees, :paidAmt, :paidBy, :status)";
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("loanId", loanId);
		params.addValue("paymentDueDt", installment.getPaymentDueDt());
		params.addValue("paidDt", installment.getPaidDt());
		params.addValue("fees", installment.getFees());
		params.addValue("paidAmt", installment.getAmt());
		params.addValue("paidBy", UserContext.getUserName());
		params.addValue("status", PaymentStatus.QUEUED.getCode());
		int row = jdbcTemplate.update(sql, params);
		if (row == 1){
			msg = new BaseMsg();
		}
		return msg;
	
	}

	@Override
	public List<LoanDtlsMsg> viewInstallments(int orgId, String loanRefId) {
		final String allInstallmentSql = " SELECT LI.LOAN_REF_ID, LI.LOAN_AMT, LI.LOAN_STATUS, SME.ACRONYM AS SME, FINANCIER.ACRONYM AS FINANCIER "
				+ " LI.LOAN_DISPATCH_DT, LI.LOAN_CLOSE_DT, LI.CREATE_DT, LN_INST.PAYMENT_DUE_DT, LN_INST.PAID_DT, LN_INST.FEES, LN_INST.PAID_AMOUNT "
				+ " FROM LOAN_INFO LI "
				+ " JOIN LOAN_INSTALLMENT LN_INST "
				+ "  ON LI.LOAN_ID = LN_INST.LOAN_ID "
				+ " JOIN ORGANIZATION_INFO SME "
				+ "  ON LI.SME_ORG_ID = SME.COMPANY_ID "
				+ " JOIN ORGANIZATION_INFO FINANCIER "
				+ "  ON LI.FINANCIER_ORG_ID = FINANCIER.COMPANY_ID "
				+ " WHERE LI.SME_ORG_ID = :smeOrgId";
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("smeOrgId", orgId);
		return jdbcTemplate.query(allInstallmentSql, new LoanInstallmentRowMapper());
	}
	
	private class LoanInstallmentRowMapper implements RowMapper<LoanDtlsMsg>{

		LoanDtlsMsg result = null;
		List<String> loanRefIds = new LinkedList<>();
		@Override
		public LoanDtlsMsg mapRow(ResultSet rs, int arg1)
				throws SQLException {
			String loanRefId = rs.getString("LOAN_REF_ID");
			if(!loanRefIds.contains(loanRefId)){
				loanRefIds.add(loanRefId);
				result = new LoanDtlsMsg();
				List<LoanInstallmentsDtlsMsg> installments = new LinkedList<>();
				result.getEmis().addAll(installments);
				result.setLoanRefId(rs.getString("LOAN_REF_ID"));
				result.setLoanAmt(rs.getDouble("LOAN_AMT"));
				result.setLoanStatus(LoanStatus.fromCode(rs.getInt("LOAN_STATUS")).getText());
				result.setFinancierAcro(rs.getString("FINANCIER"));
				result.setSmeAcro(rs.getString("SME"));
			}
			LoanInstallmentsDtlsMsg installment = new LoanInstallmentsDtlsMsg();
			installment.setAmt(rs.getDouble("PAID_AMOUNT"));
			installment.setFees(rs.getDouble("FEES"));
			installment.setPaymentDueDt(rs.getDate("PAYMENT_DUE_DT"));
			installment.setPaidDt(rs.getDate("PAID_DT"));
			result.getEmis().add(installment);
			
			return result;
		}
		
	}

	@Override
	public List<LoanDtlsMsg> viewActiveLoans(int smeOrgId) {
		final String sql = "SELECT LI.LOAN_ID, LI.LOAN_DISPATCH_DT, LI.LOAN_CLOSE_DT, LI.LOAN_REF_ID, LI.LOAN_AMT, LI.INVOICE_POOL_ID, FIN.ACRONYM, "
				+ " IFC.INTEREST_RATE, IFC.LOAN_PERIOD, LOAN_INST.STATUS, LOAN_INST.INSTALLMENT_ID, LOAN_INST.PAID_AMOUNT, LOAN_INST.FEES, "
				+ " LOAN_INST.PAID_DT, LOAN_INST.PAYMENT_DUE_DT FROM "
				+ " LOAN_INFO LI "
				+ " JOIN ORGANIZATION_INFO FIN "
				+ " ON LI.FINANCIER_ORG_ID = FIN.COMPANY_ID "
				+ " JOIN INVOICE_FINANCIER_CANDIDATES IFC "
				+ " ON IFC.INVOICE_POOL_ID = LI.INVOICE_POOL_ID AND IFC.FINANCIER_ID = LI.FINANCIER_ORG_ID  "
				+ " LEFT JOIN LOAN_INSTALLMENT LOAN_INST "
				+ " ON LI.LOAN_ID = LOAN_INST.LOAN_ID "
				+ " WHERE LI.SME_ORG_ID = :smeOrgId";
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("smeOrgId", smeOrgId);
		return jdbcTemplate.query(sql, params, new ResultSetExtractor<List<LoanDtlsMsg>>() {

			@Override
			public List<LoanDtlsMsg> extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				List<LoanDtlsMsg> resultList = new LinkedList<>();
				Map<String, LoanDtlsMsg> resultMap = new LinkedHashMap<>();
				String loanRefId = null;
				while(rs.next()){
					loanRefId = rs.getString("LOAN_REF_ID");
					LoanDtlsMsg row = null;
					if(!resultMap.containsKey(loanRefId)){
						row = new LoanDtlsMsg();
						resultList.add(row);
						resultMap.put(loanRefId, row);
					}else{
						row = resultMap.get(loanRefId);
					}
					List<LoanInstallmentsDtlsMsg> installments = new LinkedList<>();
					row.getEmis().addAll(installments);
					row.setFinancierAcro(rs.getString("ACRONYM"));
					row.setInterestRate(rs.getDouble("INTEREST_RATE"));
					row.setLoanDispatchDt(rs.getDate("LOAN_DISPATCH_DT"));
					row.setLoanCloseDt(rs.getDate("LOAN_CLOSE_DT"));
					row.setLoanRefId(rs.getString("LOAN_REF_ID"));
					row.setLoanAmt(rs.getDouble("LOAN_AMT"));
					row.setLoanId(rs.getInt("LOAN_ID"));
					row.setLoanPeriod(rs.getInt("LOAN_PERIOD"));
					if( rs.getDate("PAYMENT_DUE_DT") != null ){
						LoanInstallmentsDtlsMsg installment = new LoanInstallmentsDtlsMsg();
						installment.setFees(rs.getDouble("FEES"));
						installment.setPaidDt(rs.getDate("PAID_DT"));
						installment.setStatus(PaymentStatus.fromCode(rs.getInt("STATUS")).getText());
						installment.setLoanInstallmentId(rs.getInt("INSTALLMENT_ID"));
						installment.setAmt(rs.getDouble("PAID_AMOUNT"));
						installment.setPaymentDueDt(rs.getDate("PAYMENT_DUE_DT"));
						row.getEmis().add(installment);
					}
				}
				return resultList;
			}
			
		});
	}

	/*private class LoanDtlsRowMapper implements RowMapper<LoanDtlsMsg>{
		Map<String, LoanDtlsMsg> resultMap = new LinkedHashMap<>();
		LoanDtlsMsg row = null;
		@Override
		public LoanDtlsMsg mapRow(ResultSet rs, int arg1) throws SQLException {
			String loanRefId = rs.getString("LOAN_REF_ID");
			if(!resultMap.containsKey(loanRefId)){
				row = new LoanDtlsMsg();
				resultMap.put(loanRefId, row);
			}else{
				row = resultMap.get(loanRefId);
			}
			List<LoanInstallmentsDtlsMsg> installments = new LinkedList<>();
			row.getEmis().addAll(installments);
			row.setFinancierAcro(rs.getString("ACRONYM"));
			row.setInterestRate(rs.getDouble("INTEREST_RATE"));
			row.setLoanDispatchDt(rs.getDate("LOAN_DISPATCH_DT"));
			row.setLoanCloseDt(rs.getDate("LOAN_CLOSE_DT"));
			row.setLoanRefId(rs.getString("LOAN_REF_ID"));
			row.setLoanAmt(rs.getDouble("LOAN_AMT"));
			row.setLoanId(rs.getInt("LOAN_ID"));
			row.setLoanPeriod(rs.getInt("LOAN_PERIOD"));
			if( rs.getDate("PAYMENT_DUE_DT") != null ){
				LoanInstallmentsDtlsMsg installment = new LoanInstallmentsDtlsMsg();
				installment.setFees(rs.getDouble("FEES"));
				installment.setPaidDt(rs.getDate("PAID_DT"));
				installment.setStatus(PaymentStatus.fromCode(rs.getInt("STATUS")).getText());
				installment.setLoanInstallmentId(rs.getInt("INSTALLMENT_ID"));
				installment.setAmt(rs.getDouble("PAID_AMOUNT"));
				installment.setPaymentDueDt(rs.getDate("PAYMENT_DUE_DT"));
				row.getEmis().add(installment);
			}
			return row;
		}
	}*/
}
