package com.floatinvoice.business.dao;

import java.util.List;

import com.floatinvoice.messages.BankDtlsMsg;
import com.floatinvoice.messages.BaseMsg;
import com.floatinvoice.messages.LoanDtlsMsg;
import com.floatinvoice.messages.LoanInstallmentsDtlsMsg;

public interface BankInfoDao {

	List<BankDtlsMsg> fetchBankDetails(int orgId);
	
	BaseMsg saveBankInfo(BankDtlsMsg bankDetails);
	
	BaseMsg approveLoan(LoanDtlsMsg loanDtlsMsg);
	
	BaseMsg payLoanInstallment(LoanInstallmentsDtlsMsg installment);
	
	List<LoanDtlsMsg> viewInstallments(int orgId, String loanRefId);
	
	List<LoanDtlsMsg> viewActiveLoans(int smeOrgId);
}

