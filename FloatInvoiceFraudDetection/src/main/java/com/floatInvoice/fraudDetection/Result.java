package com.floatInvoice.fraudDetection;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Result {
	private Map<Integer,Double> BenfordsLawResult=new HashMap<Integer, Double>();
	private int PurchaseOrderMissing;
	private int RoundedValues;
	private int DaysToPaymentExceedThreshold;
	private int PaymentMadeOnSaturday;
	private int PaymentMadeOnSunday;
	private int EvenAmounts;
	private List<String> duplicateSSS;
	private List<String> duplicateSSD;



	public List<String> getDuplicateSSS() {
		return duplicateSSS;
	}

	public void setDuplicateSSS(List<String> duplicateSSS) {
		this.duplicateSSS = duplicateSSS;
	}

	public List<String> getDuplicateSSD() {
		return duplicateSSD;
	}

	public void setDuplicateSSD(List<String> duplicateSSD) {
		this.duplicateSSD = duplicateSSD;
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

	
}
