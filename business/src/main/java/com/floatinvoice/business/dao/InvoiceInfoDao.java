package com.floatinvoice.business.dao;

import java.util.Map;

import com.floatinvoice.messages.BaseMsg;
import com.floatinvoice.messages.InvoiceAccountInfoMsg;
import com.floatinvoice.messages.InvoiceDtlsMsg;
import com.floatinvoice.messages.ListMsg;
import com.floatinvoice.messages.LoanDtlsMsg;

public interface InvoiceInfoDao {

	ListMsg<InvoiceDtlsMsg> fetchAllNewInvoices( String smeAcronym );

	ListMsg<InvoiceDtlsMsg> fetchInvoicePoolDtls( int orgId, String poolRefId );

	ListMsg<InvoiceDtlsMsg> fetchInvoicesAvailableForBanks( int bankOrgId );

	Integer creditInvoice( String refId )throws Exception;
	
	ListMsg<InvoiceDtlsMsg> fetchFundedInvoices( int orgId );

	ListMsg<InvoiceDtlsMsg> fetchPendingInvoices( int orgId );
	
	BaseMsg bidInvoice ( InvoiceDtlsMsg msg, int financierOrgId ) throws Exception;

	void manageInvoicePools(  );

	void processInvoicePoolsInCompleteState() throws Exception;
	
	BaseMsg createInvoiceAccount(InvoiceAccountInfoMsg msg);
	
	Map<String, Object> findInvoicePoolByRefId(String poolRefId);
	
	ListMsg<InvoiceDtlsMsg> fetchPaidInvoicePools(String acronym) throws Exception;
	
	Map<String, Object> findInfoByFinancierCandidateRefId( String candidateRefId);
	
	void notifyInvoiceBidsClosed( int poolId );
	
	void mapInvoiceToFinancierForFunding(int poolId, int candidateId, int financierId);
	
	ListMsg<InvoiceDtlsMsg> fetchAcceptedBidBySME(String acronym) throws Exception;
	
	ListMsg<LoanDtlsMsg> viewFundedInvoicesByFinancier( int orgId );
	
	ListMsg<InvoiceDtlsMsg> fetchPendingInvoicesForApproval( int buyerOrgId );
	
	ListMsg<InvoiceDtlsMsg> viewBuyerApprovedInvoices( int buyerOrgId );
	
	ListMsg<InvoiceDtlsMsg> viewBuyerRejectedInvoices( int buyerOrgId );
	
	ListMsg<InvoiceDtlsMsg> viewBuyerAllegedInvoices( int buyerOrgId );

	int managePendingInvoices(String invoiceRefId, int action);
	
}

