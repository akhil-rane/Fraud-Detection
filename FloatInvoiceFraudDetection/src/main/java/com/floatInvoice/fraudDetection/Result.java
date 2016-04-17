package com.floatInvoice.fraudDetection;

import java.util.HashMap;
import java.util.Map;

public class Result {
	private Map<Integer,Double> BenfordsLawResult=new HashMap<Integer, Double>();
	private int PurchaseOrderMissing;
	private int RoundedValues;
	private int DaysToPaymentExceedThreshold;
	private int PaymentMadeOnSaturday;
	private int PaymentMadeOnSunday;
	private int EvenAmounts;
	

	@Override
	public String toString() {
		return "Result [BenfordsLawResult=" + BenfordsLawResult
				+ ", PurchaseOrderMissing=" + PurchaseOrderMissing
				+ ", RoundedValues=" + RoundedValues
				+ ", DaysToPaymentExceedThreshold="
				+ DaysToPaymentExceedThreshold + ", PaymentMadeOnSaturday="
				+ PaymentMadeOnSaturday + ", PaymentMadeOnSunday="
				+ PaymentMadeOnSunday + ", EvenAmounts=" + EvenAmounts + "]";
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
