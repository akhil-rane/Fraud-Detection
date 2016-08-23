package com.floatInvoice.fraudDetection.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.floatInvoice.fraudDetection.DispatcherKey;

public class JdbcDispatcher {
	
	private NamedParameterJdbcTemplate jdbcTemplate;
	
	public JdbcDispatcher(){
		
	}
	
	public JdbcDispatcher(NamedParameterJdbcTemplate jdbcTemplate){
		this.jdbcTemplate = jdbcTemplate;
	}
	
	public List<DispatcherKey> execute(){
		final String sql = " SELECT DISTINCT BUYER_ID, SUPPLIER_ID FROM FRAUD_DETECTION_QUEUE WHERE status = 0";
		return jdbcTemplate.query(sql, new DispatcherKeyRowMapper());
	}

	public NamedParameterJdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(NamedParameterJdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	private class DispatcherKeyRowMapper implements RowMapper<DispatcherKey>{
		@Override
		public DispatcherKey mapRow(ResultSet rs, int arg1)
				throws SQLException {
			DispatcherKey row = new DispatcherKey(rs.getInt("BUYER_ID"), rs.getInt("SUPPLIER_ID"));
			return row;
		}
	}
	
}
