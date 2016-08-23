package com.floatinvoice.business;

import java.util.Map;

import org.springframework.http.HttpStatus;

import com.floatinvoice.business.dao.InvoiceFileUploadDao;
import com.floatinvoice.business.dao.InvoiceInfoDao;
import com.floatinvoice.business.dao.OrgReadDao;
import com.floatinvoice.common.OrgType;
import com.floatinvoice.messages.BaseMsg;
import com.floatinvoice.messages.InvoiceAccountInfoMsg;
import com.floatinvoice.messages.InvoiceDtlsMsg;
import com.floatinvoice.messages.ListMsg;
import com.floatinvoice.messages.LoanDtlsMsg;
import com.floatinvoice.messages.UploadMessage;


public class InvoiceServiceImpl implements InvoiceService {

	InvoiceInfoDao invoiceInfoReadDao;
	InvoiceFileUploadDao invoiceFileUploadDao;
	OrgReadDao orgReadDao;
	
	public InvoiceServiceImpl(){
		
	}
	
	public InvoiceServiceImpl( InvoiceInfoDao invoiceInfoReadDao, InvoiceFileUploadDao invoiceFileUploadDao,
			OrgReadDao orgReadDao){		
		this.invoiceInfoReadDao = invoiceInfoReadDao;
		this.invoiceFileUploadDao = invoiceFileUploadDao;
		this.orgReadDao = orgReadDao;
	}

	@Override
	public ListMsg<InvoiceDtlsMsg> fetchAllNewInvoices(String acronym) {
		
		Map<String, Object> org = orgReadDao.findOrgId(acronym);
		String type = (String) org.get("ORG_TYPE");
		Integer bankOrgId = (Integer) org.get("COMPANY_ID");
		
		if(OrgType.BANK.getText().equalsIgnoreCase(type)){
			return invoiceInfoReadDao.fetchInvoicesAvailableForBanks(bankOrgId);
		}else
			return invoiceInfoReadDao.fetchAllNewInvoices(acronym);
	}

	@Override
	public BaseMsg uploadInvoiceFile(UploadMessage msg) throws Exception {
		return invoiceFileUploadDao.fileUpload(msg);
	}

	@Override
	public BaseMsg creditInvoice(String refId) throws Exception {
		BaseMsg response = null;
		int result = invoiceInfoReadDao.creditInvoice(refId);
		if (result > 0){
			response = new BaseMsg();	
			response.addInfoMsg("Notification successful", HttpStatus.OK.value());
		}
		return response;
	}

	@Override
	public ListMsg<InvoiceDtlsMsg> fetchFundedInvoices(String acronym) {	
		Map<String, Object> org = orgReadDao.findOrgId(acronym);
		Integer orgId = (Integer) org.get("COMPANY_ID");
		return invoiceInfoReadDao.fetchFundedInvoices(orgId);
	}

	@Override
	public ListMsg<InvoiceDtlsMsg> fetchPendingInvoices(String acronym) {
		Map<String, Object> org = orgReadDao.findOrgId(acronym);
		Integer orgId = (Integer) org.get("COMPANY_ID");
		return invoiceInfoReadDao.fetchPendingInvoices(orgId);
	}

	@Override
	public BaseMsg bidInvoice(InvoiceDtlsMsg msg, String acronym) throws Exception {
		Map<String, Object> org = orgReadDao.findOrgId(acronym);
		Integer orgId = (Integer) org.get("COMPANY_ID");
		BaseMsg response =  invoiceInfoReadDao.bidInvoice(msg, orgId);
		response.addInfoMsg("Success", HttpStatus.OK.value());
		return response;
	}

	@Override
	public ListMsg<InvoiceDtlsMsg> fetchInvoicePoolDetails(String acronym,
			String poolRefId) {
		Map<String, Object> org = orgReadDao.findOrgId(acronym);
		Integer orgId = (Integer) org.get("COMPANY_ID");
		return invoiceInfoReadDao.fetchInvoicePoolDtls(orgId, poolRefId);
	}

	@Override
	public BaseMsg createInvoiceAccount( InvoiceAccountInfoMsg msg ) {
		return invoiceInfoReadDao.createInvoiceAccount(msg);
	}

	@Override
	public ListMsg<InvoiceDtlsMsg> fetchPaidInvoicePools(String acronym)
			throws Exception {
		return invoiceInfoReadDao.fetchPaidInvoicePools(acronym);
	}

	@Override
	public BaseMsg acceptBid(String candidateRefId) {
		Map<String, Object> map = invoiceInfoReadDao.findInfoByFinancierCandidateRefId(candidateRefId);
		int financierOrgId = (int) map.get("FINANCIER_ID");
		int poolId = (int) map.get("INVOICE_POOL_ID");
		int candidateId = (int) map.get("CANDIDATE_ID");
		invoiceInfoReadDao.notifyInvoiceBidsClosed(poolId);
		invoiceInfoReadDao.mapInvoiceToFinancierForFunding(poolId, candidateId, financierOrgId);
		BaseMsg response = new BaseMsg();     
		response.addInfoMsg("Request processed successfully. ", HttpStatus.OK.value());
		return response;
	}

	@Override
	public ListMsg<InvoiceDtlsMsg> fetchAcceptedBidsBySME(String acronym) throws Exception {
		return invoiceInfoReadDao.fetchAcceptedBidBySME(acronym);
	}

	@Override
	public ListMsg<LoanDtlsMsg> viewFundedInvoicesByFinancier(String acronym) {
		Map<String, Object> org = orgReadDao.findOrgId(acronym);
		Integer bankOrgId = (Integer) org.get("COMPANY_ID");
		return invoiceInfoReadDao.viewFundedInvoicesByFinancier(bankOrgId);
	}

	@Override
	public ListMsg<InvoiceDtlsMsg> fetchPendingInvoicesForApproval(
			String buyerOrgAcro) {		
		Map<String, Object> buyerOrg = orgReadDao.findOrgId(buyerOrgAcro);
		Integer buyerOrgId = (Integer) buyerOrg.get("COMPANY_ID");
		return invoiceInfoReadDao.fetchPendingInvoicesForApproval(buyerOrgId);
	}

	@Override
	public BaseMsg managePendingInvoices(String invoiceRefId, int action) {
		int result = invoiceInfoReadDao.managePendingInvoices(invoiceRefId, action);
		BaseMsg response = null;     
		if(result > 0){
			response = new BaseMsg(); 
			response.addInfoMsg("Request processed successfully. ", HttpStatus.OK.value());
		}
		return response;
	}

	@Override
	public ListMsg<InvoiceDtlsMsg> viewBuyerApprovedInvoices(String buyerOrgAcro) {
		Map<String, Object> buyerOrg = orgReadDao.findOrgId(buyerOrgAcro);
		Integer buyerOrgId = (Integer) buyerOrg.get("COMPANY_ID");
		return invoiceInfoReadDao.viewBuyerApprovedInvoices(buyerOrgId);
	}

	@Override
	public ListMsg<InvoiceDtlsMsg> viewBuyerRejectedInvoices(String buyerOrgAcro) {
		Map<String, Object> buyerOrg = orgReadDao.findOrgId(buyerOrgAcro);
		Integer buyerOrgId = (Integer) buyerOrg.get("COMPANY_ID");
		return invoiceInfoReadDao.viewBuyerRejectedInvoices(buyerOrgId);
	}

	@Override
	public ListMsg<InvoiceDtlsMsg> viewBuyerAllegedInvoices(String buyerOrgAcro) {
		Map<String, Object> buyerOrg = orgReadDao.findOrgId(buyerOrgAcro);
		Integer buyerOrgId = (Integer) buyerOrg.get("COMPANY_ID");
		return invoiceInfoReadDao.viewBuyerAllegedInvoices(buyerOrgId);

	}
	
	
}
