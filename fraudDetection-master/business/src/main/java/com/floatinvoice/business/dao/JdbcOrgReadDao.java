/**
 * 
 */
package com.floatinvoice.business.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

/**
 * @author gnaik
 *
 */
public class JdbcOrgReadDao implements OrgReadDao {

	/* (non-Javadoc)
	 * @see com.floatinvoice.business.dao.OrgReadDao#findSMEOrgId(java.lang.String)
	 * 
	 * 
	 */
	
	
	private NamedParameterJdbcTemplate jdbcTemplate;
	
	public JdbcOrgReadDao(){
		
	}
	
	public JdbcOrgReadDao( DataSource dataSource){
		 jdbcTemplate = new NamedParameterJdbcTemplate( dataSource );
	}
	
	@Override
	public Map<String, Object> findOrgId( String acronym ){
		Map<String, Object> result;
		final String sql = "select company_id,org_type from ORGANIZATION_INFO oi where oi.acronym=:acronym";
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("acronym",acronym);
		result = jdbcTemplate.queryForObject(sql, params, new ColumnMapRowMapper());
		return result;
	}

	@Override
	public List<Integer> findAllFinancierOrgIds(){
		List<Map<String, Object>> queryResultSet;
		final String sql = "select company_id from ORGANIZATION_INFO oi where oi.org_type='BANK'";
		queryResultSet = jdbcTemplate.query(sql, new ColumnMapRowMapper());
		List<Integer> result = new ArrayList<>();
		for ( Map<String, Object> map : queryResultSet ){
			result.add((Integer)map.get("company_id"));
		}
		return result;
	}
}
