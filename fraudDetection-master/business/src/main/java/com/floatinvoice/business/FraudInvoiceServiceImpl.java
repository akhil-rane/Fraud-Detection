package com.floatinvoice.business;

import java.util.Map;

import com.floatinvoice.business.dao.FraudInvoiceInfoDao;
import com.floatinvoice.business.dao.OrgReadDao;
import com.floatinvoice.messages.FraudInvoiceDtls;
import com.floatinvoice.messages.ListMsg;

public class FraudInvoiceServiceImpl implements FraudInvoiceService {

	private FraudInvoiceInfoDao fraudInvoiceInfoDao;
	private OrgReadDao orgReadDao;
	
	public FraudInvoiceServiceImpl(){}
	
	public FraudInvoiceServiceImpl(FraudInvoiceInfoDao fraudInvoiceInfoDao, OrgReadDao orgReadDao){
		this.fraudInvoiceInfoDao = fraudInvoiceInfoDao;
		this.orgReadDao = orgReadDao;
	}
	@Override
	public ListMsg<FraudInvoiceDtls> findFraudInvoicesBySupplier( String acronym ) {
		Map<String, Object> org = orgReadDao.findOrgId(acronym);
		Integer orgId = (Integer) org.get("COMPANY_ID");
		return fraudInvoiceInfoDao.findFraudInvoicesBySupplier(orgId);
	}

	@Override
	public ListMsg<FraudInvoiceDtls> findAllFraudInvoices() {
		return fraudInvoiceInfoDao.findAllFraudInvoices();
	}

}
