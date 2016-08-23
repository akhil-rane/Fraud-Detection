package com.floatinvoice.business.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import com.floatinvoice.common.RegistrationStatusEnum;
import com.floatinvoice.common.UserContext;
import com.floatinvoice.messages.BaseMsg;
import com.floatinvoice.messages.RegistrationStep2CorpDtlsMsg;
import com.floatinvoice.messages.RegistrationStep3UserPersonalDtlsMsg;

@Transactional
public class JdbcRegistrationDao implements RegistrationDao {

	private NamedParameterJdbcTemplate jdbcTemplate;

	public JdbcRegistrationDao(){
		
	}
	
	public JdbcRegistrationDao( DataSource dataSource ){
		jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}
	
	private boolean updateRegistrationStatus(String userEmail, int statusCode){
		boolean rowUpdated = false;
		final String sql = "UPDATE CLIENT_LOGIN_INFO SET REGISTRATION_STATUS = :status where EMAIL = :email";
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("email", userEmail);
		paramMap.put("status", statusCode);
		int row = jdbcTemplate.update(sql, paramMap);
		if (row == 1) {
			rowUpdated = true;
		}
		return rowUpdated;
	}
	
	@Override
	public BaseMsg registerSignInInfo(String userEmail, String password,
			String confirmedPassword, int regCode) {
		BaseMsg baseMsg = null;
		final String sql = "INSERT INTO CLIENT_LOGIN_INFO ( EMAIL, PASSWORD, INSERT_DT, REGISTRATION_STATUS) VALUES "
				+ "(:userEmail, :passwd, :insertDt, :regStatus)";
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("userEmail", userEmail);
		paramMap.put("passwd", password);
		paramMap.put("insertDt", new Date());
		paramMap.put("regStatus", regCode);
		int row = jdbcTemplate.update(sql, paramMap);
		if(row == 1) {
			baseMsg = new BaseMsg();
		}
		return baseMsg;
	}

	@Override
	public BaseMsg registerOrgInfo(RegistrationStep2CorpDtlsMsg msg) {
		BaseMsg baseMsg = null;
		final String sql = "INSERT INTO ORGANIZATION_INFO (ACRONYM, COMPANY_NAME, STREET, CITY, STATE, ZIP_CODE, COUNTRY, PHONE_NO, INSERT_DT, UPDATE_DT, UPDATE_BY, CREATED_BY, ORG_TYPE)"
				+ " VALUES (:acro, :companyName, :street, :city, :state, :zipCode, 'INDIA', :phoneNo, :insertDt, :updateDt, :updateBy, :createdBy, :orgType)";
		
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("acro", msg.getAcronym());
		paramMap.put("companyName", msg.getCompName());
		paramMap.put("street", msg.getStreet());
		paramMap.put("city", msg.getCity());
		paramMap.put("state", msg.getState());
		paramMap.put("zipCode", msg.getZipCode());
		paramMap.put("phoneNo", msg.getPhoneNo());		
		paramMap.put("insertDt", new Date());		
		paramMap.put("updateDt", new Date());		
		paramMap.put("updateBy", UserContext.getUserName());		
		paramMap.put("createdBy", UserContext.getUserName());		
		paramMap.put("orgType", msg.getOrgType());		
		int row = jdbcTemplate.update(sql, paramMap);
		if(row == 1){
			
			final String uoMapSql = "INSERT INTO USER_ORGANIZATION_MAP (USER_ID, COMPANY_ID, INSERT_DT) "
					+ " VALUES ( (SELECT USER_ID FROM CLIENT_LOGIN_INFO WHERE EMAIL = :email),"
					+ " (SELECT COMPANY_ID FROM ORGANIZATION_INFO WHERE ACRONYM = :acro), "
					+ " SYSDATE()) ";
			
			Map<String, Object> map = new HashMap<>();
			map.put("email", UserContext.getUserName());
			map.put("acro", msg.getAcronym());
			int nestedRow = jdbcTemplate.update(uoMapSql, map);
			if( nestedRow == 1 && updateRegistrationStatus(UserContext.getUserName(), RegistrationStatusEnum.ORG.getCode())){
				baseMsg = new BaseMsg();
			}
		}		
		return baseMsg;		
	}

	@Override
	public BaseMsg registerUserBankInfo(RegistrationStep3UserPersonalDtlsMsg msg) {
		BaseMsg baseMsg = null;
		String userIdSql = "SELECT USER_ID FROM CLIENT_LOGIN_INFO WHERE EMAIL = :email";
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("email", UserContext.getUserName());
		Map<String, Object> userIdSqlResult = jdbcTemplate.queryForObject(userIdSql, params, new ColumnMapRowMapper());
		final String sql = "INSERT INTO SME_USER_INFO (USER_ID, BANK_ACCOUNT_NO, BANK_IFSC_CODE, BANK_NAME, DIRECTOR_FNAME, DIRECTOR_LNAME, PAN_CARD_NO, AADHAR_CARD_ID)"
				+ " VALUES (:userId, :bankAcctNo, :IFSCCd, :bankName, :dirFName, :dirLName, :panCard, :aadharId)";
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("userId", userIdSqlResult.get("USER_ID"));
		paramMap.put("bankAcctNo", msg.getBankAcctNo());		
		paramMap.put("IFSCCd", msg.getIfscCode());
		paramMap.put("bankName", msg.getBankName());
		paramMap.put("dirFName", msg.getDirectorFName());
		paramMap.put("dirLName", msg.getDirectorLName());
		paramMap.put("panCard", msg.getPanCardNo());
		paramMap.put("aadharId", msg.getAadharCardId());
		int row = jdbcTemplate.update(sql, paramMap);
		if( row == 1 && updateRegistrationStatus(UserContext.getUserName(), RegistrationStatusEnum.USER.getCode())){
			baseMsg = new BaseMsg();
		}
		return baseMsg;
		
	}

	
	
}

