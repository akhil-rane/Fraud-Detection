package com.floatInvoice.fraudDetection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.floatinvoice.messages.FraudInvoiceDtls;
import com.floatinvoice.messages.InvoiceDtlsMsg;

public class Result {
	private Map<Integer,Double> BenfordsLawResult=new HashMap<Integer, Double>();
	private int PurchaseOrderMissing;
	private int RoundedValues;
	private int DaysToPaymentExceedThreshold;
	private int PaymentMadeOnSaturday;
	private int PaymentMadeOnSunday;
	private int EvenAmounts;
	private List<FraudInvoiceDtls> duplicateSSS;
	private List<FraudInvoiceDtls> duplicateSSD;
	private InvoiceDtlsMsg invoiceDtls;

	public List<FraudInvoiceDtls> getDuplicateSSS() {
		return duplicateSSS;
	}

	public void setDuplicateSSS(List<FraudInvoiceDtls> duplicateSSS) {
		this.duplicateSSS = new ArrayList(duplicateSSS);
	}

	public List<FraudInvoiceDtls> getDuplicateSSD() {
		return duplicateSSD;
	}

	public void setDuplicateSSD(List<FraudInvoiceDtls> duplicateSSD) {
		this.duplicateSSD = new ArrayList(duplicateSSD);
	}

	@Override
	public String toString() {
		return "Result [BenfordsLawResult=" + BenfordsLawResult
				+ ", PurchaseOrderMissing=" + PurchaseOrderMissing
				+ ", RoundedValues=" + RoundedValues
				+ ", DaysToPaymentExceedThreshold="
				+ DaysToPaymentExceedThreshold + ", PaymentMadeOnSaturday="
				+ PaymentMadeOnSaturday + ", PaymentMadeOnSunday="
				+ PaymentMadeOnSunday + ", EvenAmounts=" + EvenAmounts
				+ ", duplicateSSS=" + duplicateSSS + ", duplicateSSD="
				+ duplicateSSD + "]";
	}

	public int getEvenAmounts() {
		return EvenAmounts;
	}

	public void setEvenAmounts(int evenAmounts) {
		EvenAmounts = evenAmounts;
	}

	public Map<Integer, Double> getBenfordsLawResult() {
		return BenfordsLawResult;
	}

	public void setBenfordsLawResult(Map<Integer, Double> benfordsLawResult) {
		BenfordsLawResult = benfordsLawResult;
	}

	public int getPurchaseOrderMissing() {
		return PurchaseOrderMissing;
	}

	public void setPurchaseOrderMissing(int purchaseOrderMissing) {
		PurchaseOrderMissing = purchaseOrderMissing;
	}

	public int getRoundedValues() {
		return RoundedValues;
	}

	public void setRoundedValues(int roundedValues) {
		RoundedValues = roundedValues;
	}

	public int getDaysToPaymentExceedThreshold() {
		return DaysToPaymentExceedThreshold;
	}

	public void setDaysToPaymentExceedThreshold(int daysToPaymentExceedThreshold) {
		DaysToPaymentExceedThreshold = daysToPaymentExceedThreshold;
	}

	public int getPaymentMadeOnSaturday() {
		return PaymentMadeOnSaturday;
	}

	public void setPaymentMadeOnSaturday(int paymentMadeOnSaturday) {
		PaymentMadeOnSaturday = paymentMadeOnSaturday;
	}

	public int getPaymentMadeOnSunday() {
		return PaymentMadeOnSunday;
	}

	public void setPaymentMadeOnSunday(int paymentMadeOnSunday) {
		PaymentMadeOnSunday = paymentMadeOnSunday;
	}

	public InvoiceDtlsMsg getInvoiceDtls() {
		return invoiceDtls;
	}

	public void setInvoiceDtls(InvoiceDtlsMsg invoiceDtls) {
		this.invoiceDtls = invoiceDtls;
	}

	
}
