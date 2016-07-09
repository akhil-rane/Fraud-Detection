package com.floatinvoice.business.dao;

import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.floatinvoice.messages.UserProfile;

public class JdbcProfileDao implements ProfileDao {

	private NamedParameterJdbcTemplate jdbcTemplate;
	private OrgReadDao orgReadDao;

	public JdbcProfileDao(){
		
	}
	
	public JdbcProfileDao( DataSource dataSource, OrgReadDao orgReadDao){
		 this.jdbcTemplate = new NamedParameterJdbcTemplate( dataSource );
		 this.orgReadDao = orgReadDao;
	}
	
	@Override
	public UserProfile fetchUserProfileDetails( String userEmail ) {
		
		final String sql = " SELECT OI.COMPANY_ID, OI.ACRONYM, OI.ORG_TYPE, CLI.EMAIL, CLI.USER_ID, CLI.REGISTRATION_STATUS FROM CLIENT_LOGIN_INFO CLI "
				+ " JOIN USER_ORGANIZATION_MAP MAP "
				+ " ON CLI.USER_ID = MAP.USER_ID "
				+ " JOIN ORGANIZATION_INFO OI "
				+ " ON OI.COMPANY_ID = MAP.COMPANY_ID"
				+ " WHERE CLI.EMAIL = :userEmail";
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("userEmail", userEmail);
		Map<String, Object> result = jdbcTemplate.queryForObject(sql, params, new ColumnMapRowMapper());
		UserProfile userProfile = new UserProfile();
		userProfile.setEmail((String)result.get("EMAIL"));
		userProfile.setOrgAcronym((String) result.get("ACRONYM"));
		userProfile.setOrgId((int)result.get("COMPANY_ID"));
		userProfile.setOrgType((String)result.get("ORG_TYPE"));
		userProfile.setUserId((int) result.get("USER_ID"));
		userProfile.setRegistrationStatus((int)result.get("REGISTRATION_STATUS"));
		return userProfile;
	}

	@Override
	public int findUserRegistrationStatus(String userEmail) {
		final String sql = " SELECT REGISTRATION_STATUS FROM CLIENT_LOGIN_INFO CLI WHERE CLI.EMAIL = :email";
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("email", userEmail);
		Map<String, Object> result = jdbcTemplate.queryForObject(sql, params, new ColumnMapRowMapper());
		return (int)result.get("REGISTRATION_STATUS");
	}

	/*@Override
	public boolean isCompanyRegistered(String userEmail) {
		final String sql = " SELECT COUNT(*) FROM  USER_ORGANIZATION_MAP UOM "
				+ " JOIN CLIENT_LOGIN_INFO CLI "
				+ " ON UOM.USER_ID = CLI.USER_ID "
				+ " WHERE CLI.EMAIL = :email ";
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("email", userEmail);
		int result = jdbcTemplate.queryForInt(sql, params);
		if(result > 0)
			return true;
		return false;
	}

	@Override
	public boolean isPersonalInfoRegistered(String userEmail) {
		// TODO Auto-generated method stub
		return false;
	}
*/
	
}
