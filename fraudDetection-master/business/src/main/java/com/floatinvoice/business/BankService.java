package com.floatinvoice.business;

import com.floatinvoice.messages.BankDtlsMsg;
import com.floatinvoice.messages.BaseMsg;
import com.floatinvoice.messages.ListMsg;
import com.floatinvoice.messages.LoanDtlsMsg;
import com.floatinvoice.messages.LoanInstallmentsDtlsMsg;

public interface BankService {

	ListMsg<BankDtlsMsg> fetchBankDetails(String acronym);
	
	BaseMsg saveBankDetails(BankDtlsMsg bankDetails);
	
	BaseMsg approveLoan(LoanDtlsMsg loanDtlsMsg);
	
	BaseMsg payLoanInstallment(LoanInstallmentsDtlsMsg installment);
	
	ListMsg<LoanDtlsMsg> viewActiveLoans( String smeAcronym );

}
