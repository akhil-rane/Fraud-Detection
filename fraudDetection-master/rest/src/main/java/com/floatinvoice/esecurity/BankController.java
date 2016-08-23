package com.floatinvoice.esecurity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.floatinvoice.business.BankService;
import com.floatinvoice.business.InvoiceService;
import com.floatinvoice.messages.BankDtlsMsg;
import com.floatinvoice.messages.BaseMsg;
import com.floatinvoice.messages.ListMsg;
import com.floatinvoice.messages.LoanDtlsMsg;
import com.floatinvoice.messages.LoanInstallmentsDtlsMsg;

@Controller
@RequestMapping(value="/bank")
public class BankController {

	@Autowired
	private BankService bankService;
	
	@Autowired
	private InvoiceService invoiceService;
	
    @RequestMapping(value = { "/viewAccounts"}, method = RequestMethod.GET)
    public  ResponseEntity<ListMsg<BankDtlsMsg>> viewBankDetails(@RequestParam(value="acro", required=true) String acro) {
        return new ResponseEntity<ListMsg<BankDtlsMsg>>(bankService.fetchBankDetails(acro), HttpStatus.OK);
    }
    
    @RequestMapping(value = { "/saveAccount"}, method = RequestMethod.POST)
    public  ResponseEntity<BaseMsg> saveBankDetails(@RequestBody BankDtlsMsg bankDetails) throws Exception {
    	
        return new ResponseEntity<BaseMsg>(bankService.saveBankDetails(bankDetails), HttpStatus.OK);
    }

    @RequestMapping(value = { "/approveLoan"}, method = RequestMethod.POST)
    public  ResponseEntity<BaseMsg> approveLoan(@RequestBody LoanDtlsMsg loanDetails) throws Exception {
        return new ResponseEntity<BaseMsg>(bankService.approveLoan(loanDetails), HttpStatus.OK);
    }
    
    @RequestMapping(value = { "/payLoan"}, method = RequestMethod.POST)
    public  ResponseEntity<BaseMsg> payLoanInstallment(@RequestBody LoanInstallmentsDtlsMsg installment) throws Exception {
        return new ResponseEntity<BaseMsg>(bankService.payLoanInstallment(installment), HttpStatus.OK);
    }
    
    @RequestMapping(value = { "/viewAllLoans"}, method = RequestMethod.GET)
    public  ResponseEntity<ListMsg<LoanDtlsMsg>> viewAllLoans(@RequestParam(value="acro", required=true) String acro) {
        return new ResponseEntity<ListMsg<LoanDtlsMsg>>(invoiceService.viewFundedInvoicesByFinancier(acro) , HttpStatus.OK);
    }
    
    @RequestMapping(value = { "/viewActiveLoans"}, method = RequestMethod.GET)
    public  ResponseEntity<ListMsg<LoanDtlsMsg>> viewActiveLoans(@RequestParam(value="acro", required=true) String smeAcronym) {
        return new ResponseEntity<ListMsg<LoanDtlsMsg>>(bankService.viewActiveLoans(smeAcronym) , HttpStatus.OK);
    }
}
