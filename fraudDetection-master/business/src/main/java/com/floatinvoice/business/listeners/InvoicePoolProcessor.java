package com.floatinvoice.business.listeners;

import org.springframework.scheduling.annotation.Scheduled;

import com.floatinvoice.business.dao.InvoiceInfoDao;

public class InvoicePoolProcessor {

	InvoiceInfoDao invoiceInfoReadDao;
	
	public InvoicePoolProcessor(){
		
	}
	
	public InvoicePoolProcessor( InvoiceInfoDao invoiceInfoReadDao ){
		this.invoiceInfoReadDao = invoiceInfoReadDao;
	}
	
	@Scheduled(fixedRate=5000)
	public void processInvoicesToCreatePool(){
		invoiceInfoReadDao.manageInvoicePools();
	}
}
