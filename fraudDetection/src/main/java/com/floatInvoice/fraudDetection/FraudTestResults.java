package com.floatInvoice.fraudDetection;

import java.util.LinkedList;
import java.util.List;

import com.floatinvoice.messages.FraudInvoiceDtls;

public class FraudTestResults {

	private FraudInvoiceDtls invoiceDtls;
	private List<Integer> fraudTestIds;
	
	public FraudTestResults(){}
	
	public FraudTestResults(FraudInvoiceDtls invoiceDtls, List<Integer> fraudTestIds){
		this.invoiceDtls = invoiceDtls;
		this.fraudTestIds = new LinkedList<>(fraudTestIds);
	}

	public FraudInvoiceDtls getInvoiceDtls() {
		return invoiceDtls;
	}

	public void setInvoiceDtls(FraudInvoiceDtls invoiceDtls) {
		this.invoiceDtls = invoiceDtls;
	}

	public List<Integer> getFraudTestIds() {
		return fraudTestIds;
	}

	public void setFraudTestIds(List<Integer> fraudTestIds) {
		this.fraudTestIds = fraudTestIds;
	}


	
}
