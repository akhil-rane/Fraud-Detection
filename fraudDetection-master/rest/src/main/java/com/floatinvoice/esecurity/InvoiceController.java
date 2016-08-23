package com.floatinvoice.esecurity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.floatinvoice.business.FraudInvoiceService;
import com.floatinvoice.business.InvoiceService;
import com.floatinvoice.common.UserContext;
import com.floatinvoice.messages.BaseMsg;
import com.floatinvoice.messages.BuyerActionMsg;
import com.floatinvoice.messages.FraudInvoiceDtls;
import com.floatinvoice.messages.InvoiceAccountInfoMsg;
import com.floatinvoice.messages.InvoiceDtlsMsg;
import com.floatinvoice.messages.ListMsg;
import com.floatinvoice.messages.UploadMessage;

@Controller
@RequestMapping(value="/invoice")
public class InvoiceController {

	@Autowired
	InvoiceService invoiceService;
	
	@Autowired
	FraudInvoiceService fraudInvoiceService;
	
	@RequestMapping(value = { "/fraudAll"}, method = RequestMethod.GET)
    public  ResponseEntity<ListMsg<FraudInvoiceDtls>> fetchAllFraudInvoices(@RequestParam(value="acro", required=true) String acro) throws Exception {
        return new ResponseEntity<ListMsg<FraudInvoiceDtls>>(fraudInvoiceService.findAllFraudInvoices(), HttpStatus.OK);
    }
	
	@RequestMapping(value = { "/fraud"}, method = RequestMethod.GET)
    public  ResponseEntity<ListMsg<FraudInvoiceDtls>> findFraudInvoicesBySupplier(@RequestParam(value="acro", required=true) String acro) throws Exception {
        return new ResponseEntity<ListMsg<FraudInvoiceDtls>>(fraudInvoiceService.findFraudInvoicesBySupplier(acro), HttpStatus.OK);
    }
	
	@RequestMapping(value = { "/view/paid"}, method = RequestMethod.GET)
    public  ResponseEntity<ListMsg<InvoiceDtlsMsg>> paidInvoices(@RequestParam(value="acro", required=true) String acro) throws Exception {
        return new ResponseEntity<ListMsg<InvoiceDtlsMsg>>(invoiceService.fetchPaidInvoicePools(acro), HttpStatus.OK);
    }
	
	@RequestMapping(value="/{poolRefId}", method = RequestMethod.GET)
	public ResponseEntity<ListMsg<InvoiceDtlsMsg>> viewInvoicePoolDtls(@RequestParam(value="acro", required=false) String acro,
			@PathVariable String poolRefId) {
        return new ResponseEntity<ListMsg<InvoiceDtlsMsg>>(invoiceService.fetchInvoicePoolDetails(acro, poolRefId), HttpStatus.OK);
    }
	
	@RequestMapping(value="/viewPage/{poolRefId}", method = RequestMethod.GET)
	public ModelAndView viewInvoicePoolDtlsWithPage(@RequestParam(value="acro", required=false) String acro,
			@PathVariable String poolRefId) {
		ModelAndView mv = new ModelAndView();
		ListMsg<InvoiceDtlsMsg> lstMsg = invoiceService.fetchInvoicePoolDetails(acro, poolRefId);
		mv.setViewName("invoicePopUp");
		mv.addObject("lstMsg", lstMsg);
        return mv;
    }
	
    @RequestMapping(value = { "/view"}, method = RequestMethod.GET)
    public  ResponseEntity<ListMsg<InvoiceDtlsMsg>> viewNewInvoices(@RequestParam(value="acro", required=true) String acro) {
        return new ResponseEntity<ListMsg<InvoiceDtlsMsg>>(invoiceService.fetchAllNewInvoices(acro), HttpStatus.OK);
    }
    
    @RequestMapping(value = { "/funded"}, method = RequestMethod.GET)
    public  ResponseEntity<ListMsg<InvoiceDtlsMsg>> viewFundedInvoices(@RequestParam(value="acro", required=true) String acro) {
        return new ResponseEntity<ListMsg<InvoiceDtlsMsg>>(invoiceService.fetchFundedInvoices(acro), HttpStatus.OK);
    }

    @RequestMapping(value = { "/pending"}, method = RequestMethod.GET)
    public  ResponseEntity<ListMsg<InvoiceDtlsMsg>> viewPendingInvoices(@RequestParam(value="acro", required=true) String acro) {
        return new ResponseEntity<ListMsg<InvoiceDtlsMsg>>(invoiceService.fetchPendingInvoices(acro), HttpStatus.OK);
    }
    
    @RequestMapping(value = { "/upload"}, method = RequestMethod.POST)
    public  ResponseEntity<BaseMsg> uploadFile(@RequestParam(value="acro", required=true) String acro,
    		@RequestParam(value="filename", required=false) String fileName,
    		@RequestParam(value="file", required=true) MultipartFile file) throws Exception {    	
    	UploadMessage uploadMsg = new UploadMessage(file);
    	uploadMsg.setFileName(file.getOriginalFilename());
    	uploadMsg.setSmeAcronym(acro);    	
        return new ResponseEntity<BaseMsg>(invoiceService.uploadInvoiceFile(uploadMsg), HttpStatus.OK);
    }
    
    
    @RequestMapping(value = { "/credit"}, method = RequestMethod.POST)
    public  ResponseEntity<BaseMsg> creditInvoices(@RequestBody BaseMsg request) throws Exception {
        return new ResponseEntity<BaseMsg>(invoiceService.creditInvoice(request.getRefId()), HttpStatus.OK);
    }
    
    @RequestMapping(value = { "/bid"}, method = RequestMethod.POST)
    public  ResponseEntity<BaseMsg> bidInvoices(@RequestBody InvoiceDtlsMsg request) throws Exception {
    	return new ResponseEntity<BaseMsg>(invoiceService.bidInvoice(request, UserContext.getAcronym()), HttpStatus.OK);
    }
    
    @RequestMapping(value = { "/account"}, method = RequestMethod.POST)
    public  ResponseEntity<BaseMsg> createFloatInvoiceAcct(@RequestBody InvoiceAccountInfoMsg msg) throws Exception {
        return new ResponseEntity<BaseMsg>(invoiceService.createInvoiceAccount(msg), HttpStatus.OK);
    }
    
    @RequestMapping(value = { "/acceptBid"}, method = RequestMethod.POST)
    public  ResponseEntity<BaseMsg> acceptBids(@RequestBody BaseMsg request) throws Exception {
    	String candidateRefId = request.getRefId();
    	return new ResponseEntity<BaseMsg>(invoiceService.acceptBid(candidateRefId), HttpStatus.OK);
    }
    
    @RequestMapping(value = { "/viewBids"}, method = RequestMethod.GET)
    public  ResponseEntity<ListMsg<InvoiceDtlsMsg>> viewAcceptedBidsBySME(@RequestParam(value="acro", required=true) String acro) throws Exception {
        return new ResponseEntity<ListMsg<InvoiceDtlsMsg>>(invoiceService.fetchAcceptedBidsBySME(acro), HttpStatus.OK);
    }
    
    @RequestMapping(value = { "/pending/approval"}, method = RequestMethod.GET)
    public  ResponseEntity<ListMsg<InvoiceDtlsMsg>> viewPendingInvoicesForApproval(@RequestParam(value="acro", required=true) String buyerOrgAcro) {
        return new ResponseEntity<ListMsg<InvoiceDtlsMsg>>(invoiceService.fetchPendingInvoicesForApproval(buyerOrgAcro), HttpStatus.OK);
    }
    
    @RequestMapping(value = { "/approved"}, method = RequestMethod.GET)
    public  ResponseEntity<ListMsg<InvoiceDtlsMsg>> viewApprovedInvoices(@RequestParam(value="acro", required=true) String buyerOrgAcro) {
        return new ResponseEntity<ListMsg<InvoiceDtlsMsg>>(invoiceService.viewBuyerApprovedInvoices(buyerOrgAcro), HttpStatus.OK);
    }
    
    @RequestMapping(value = { "/rejected"}, method = RequestMethod.GET)
    public  ResponseEntity<ListMsg<InvoiceDtlsMsg>> viewRejectedInvoices(@RequestParam(value="acro", required=true) String buyerOrgAcro) {
        return new ResponseEntity<ListMsg<InvoiceDtlsMsg>>(invoiceService.viewBuyerRejectedInvoices(buyerOrgAcro), HttpStatus.OK);
    }
    
    @RequestMapping(value = { "/alleged"}, method = RequestMethod.GET)
    public  ResponseEntity<ListMsg<InvoiceDtlsMsg>> viewAllegedInvoices(@RequestParam(value="acro", required=true) String buyerOrgAcro) {
        return new ResponseEntity<ListMsg<InvoiceDtlsMsg>>(invoiceService.viewBuyerAllegedInvoices(buyerOrgAcro), HttpStatus.OK);
    }
    
    @RequestMapping(value = { "/manage/approval"}, method = RequestMethod.POST)
    public  ResponseEntity<BaseMsg> bidInvoices(@RequestBody BuyerActionMsg request) throws Exception {
    	return new ResponseEntity<BaseMsg>(invoiceService.managePendingInvoices(request.getRefId(), request.getAction()), HttpStatus.OK);
    }
}
