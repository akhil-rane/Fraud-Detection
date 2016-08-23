package com.floatinvoice.business.dao;

import java.util.List;
import java.util.Map;

public interface OrgReadDao {

	public Map<String, Object> findOrgId( String acronym );

	List<Integer> findAllFinancierOrgIds();
	
}
