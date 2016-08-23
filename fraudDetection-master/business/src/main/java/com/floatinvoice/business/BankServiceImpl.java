package com.floatinvoice.business;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.floatinvoice.business.dao.BankInfoDao;
import com.floatinvoice.business.dao.OrgReadDao;
import com.floatinvoice.common.PaymentStatus;
import com.floatinvoice.messages.BankDtlsMsg;
import com.floatinvoice.messages.BaseMsg;
import com.floatinvoice.messages.ListMsg;
import com.floatinvoice.messages.LoanDtlsMsg;
import com.floatinvoice.messages.LoanInstallmentsDtlsMsg;

public class BankServiceImpl implements BankService {

	BankInfoDao bankInfoDao;	
	OrgReadDao orgReadDao;
	
	public BankServiceImpl() {
	}
	
	public BankServiceImpl(BankInfoDao bankInfoDao, OrgReadDao orgReadDao) {
		this.bankInfoDao = bankInfoDao;
		this.orgReadDao = orgReadDao;
	}
	
	@Override
	public ListMsg<BankDtlsMsg> fetchBankDetails(String acronym) {
		Map<String, Object> map = orgReadDao.findOrgId(acronym);
		int orgId = (Integer) map.get("company_id");
		return new ListMsg<>(bankInfoDao.fetchBankDetails(orgId));
	}

	@Override
	public BaseMsg saveBankDetails(BankDtlsMsg bankDetails) {
		return bankInfoDao.saveBankInfo(bankDetails);
	}

	@Override
	public BaseMsg approveLoan(LoanDtlsMsg loanDtlsMsg) {
		return bankInfoDao.approveLoan(loanDtlsMsg);
	}

	@Override
	public BaseMsg payLoanInstallment(LoanInstallmentsDtlsMsg installment) {
		return bankInfoDao.payLoanInstallment(installment);
	}
//http://learn-java-by-example.com/java/monthly-payment-calculator/
	@Override
	public ListMsg<LoanDtlsMsg> viewActiveLoans(String smeAcronym) {
		Map<String, Object> org = orgReadDao.findOrgId(smeAcronym);
		Integer smeOrgId = (Integer) org.get("COMPANY_ID");
		List<LoanDtlsMsg> lst = bankInfoDao.viewActiveLoans(smeOrgId);
		double monthlyInterestRate = 0;
		int termInMonths = 0 ;
		double monthlyPayment = 0;
		Calendar cal = Calendar.getInstance();		
		for (LoanDtlsMsg loanDtls : lst) {
			monthlyInterestRate = loanDtls.getInterestRate();
			monthlyInterestRate /= 100;
			termInMonths = loanDtls.getLoanPeriod();
			monthlyPayment = (loanDtls.getLoanAmt() * monthlyInterestRate) / 
			            (1-Math.pow(1 + monthlyInterestRate, -termInMonths));
			List<LoanInstallmentsDtlsMsg> emis = loanDtls.getEmis();
			boolean payNowFlag = true;
			for( int i = 0; i< termInMonths; i++){
				LoanInstallmentsDtlsMsg msg = new LoanInstallmentsDtlsMsg();
				cal.setTime(loanDtls.getLoanDispatchDt());
				cal.add(Calendar.MONTH, i+1);
				msg.setAmt(Math.ceil(monthlyPayment));
				msg.setFees((double)10);
				msg.setPaymentDueDt(cal.getTime());
				msg.setStatus(PaymentStatus.UNPAID.getText());
				if(!emis.contains(msg)){
					if(payNowFlag){
						msg.setPayNow(true);
						payNowFlag = false;
					}
					emis.add(msg);
				}
			}			
		}
		return new ListMsg<>(lst);
	}
}
