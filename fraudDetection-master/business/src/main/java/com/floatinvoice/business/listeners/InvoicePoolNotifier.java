package com.floatinvoice.business.listeners;

import org.springframework.scheduling.annotation.Scheduled;

import com.floatinvoice.business.dao.InvoiceInfoDao;

public class InvoicePoolNotifier {

	InvoiceInfoDao invoiceInfoReadDao;

	public InvoicePoolNotifier(){
		
	}
	
	public InvoicePoolNotifier( InvoiceInfoDao invoiceInfoReadDao){
		this.invoiceInfoReadDao = invoiceInfoReadDao;
				
	}
	
	@Scheduled(fixedRate=5000)
	public void poll( ){
		try {
			invoiceInfoReadDao.processInvoicePoolsInCompleteState();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
