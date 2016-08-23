package com.floatinvoice.business;

import com.floatinvoice.messages.BaseMsg;
import com.floatinvoice.messages.InvoiceAccountInfoMsg;
import com.floatinvoice.messages.InvoiceDtlsMsg;
import com.floatinvoice.messages.ListMsg;
import com.floatinvoice.messages.LoanDtlsMsg;
import com.floatinvoice.messages.UploadMessage;

public interface InvoiceService {

	
	ListMsg<InvoiceDtlsMsg> fetchAllNewInvoices( String acronym );
	
	ListMsg<InvoiceDtlsMsg> fetchPendingInvoices( String acronym );

	ListMsg<InvoiceDtlsMsg> fetchFundedInvoices( String acronym );

	BaseMsg uploadInvoiceFile ( UploadMessage msg) throws Exception;
		
	BaseMsg creditInvoice ( String refId ) throws Exception;
	
	BaseMsg bidInvoice ( InvoiceDtlsMsg msg, String acronym ) throws Exception;
	
	ListMsg<InvoiceDtlsMsg> fetchInvoicePoolDetails( String acronym, String poolRefId );

	BaseMsg createInvoiceAccount(InvoiceAccountInfoMsg msg);
	
	ListMsg<InvoiceDtlsMsg> fetchPaidInvoicePools(String acronym) throws Exception;
	
	BaseMsg acceptBid( String candidateRefId );
	
	ListMsg<InvoiceDtlsMsg> fetchAcceptedBidsBySME(String acronym) throws Exception;
	
	ListMsg<LoanDtlsMsg> viewFundedInvoicesByFinancier( String acronym );
	
	ListMsg<InvoiceDtlsMsg> fetchPendingInvoicesForApproval( String buyerOrgAcro);
	
	BaseMsg managePendingInvoices(String invoiceRefId, int action);
	
	ListMsg<InvoiceDtlsMsg> viewBuyerApprovedInvoices( String buyerOrgAcro);
	
	ListMsg<InvoiceDtlsMsg> viewBuyerRejectedInvoices( String buyerOrgAcro);
	
	ListMsg<InvoiceDtlsMsg> viewBuyerAllegedInvoices( String buyerOrgAcro);
}
