package com.floatInvoice.fraudDetection;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;




import com.floatinvoice.messages.FraudInvoiceDtls;

public class FraudTestResultsGroupBySupplierID {

	private int supplierId;
	
	private Map<String, Integer> benfordsLawTestResults=new HashMap<String, Integer>();
	
	private List<FraudInvoiceDtls> sameSameSameTestResults=new ArrayList<FraudInvoiceDtls>();
	
	private List<FraudInvoiceDtls> sameSameDifferentTestResults=new ArrayList<FraudInvoiceDtls>();
	
	private Map<MonthYearKey,Double> abnormalInvoiceVolumeResults=new HashMap<MonthYearKey,Double>();
	
	private Double relativeSizeFactor;
	
	public FraudTestResultsGroupBySupplierID(int supplierId) {
		
		this.supplierId=supplierId; 
	}
    		

	public int getSupplierId() {
		return supplierId;
	}
    	
	public void setSupplierId(int supplierId) {
		this.supplierId = supplierId;
	}



	public Map<String, Integer> getBenfordsLawTestResults() {
		return benfordsLawTestResults;
	}

	public void setBenfordsLawTestResults(
			Map<String, Integer> benfordsLawTestResults) {
		this.benfordsLawTestResults = benfordsLawTestResults;
	}

	public List<FraudInvoiceDtls> getSameSameSameTestResults() {
		return sameSameSameTestResults;
	}

	public void setSameSameSameTestResults(
			List<FraudInvoiceDtls> sameSameSameTestResults) {
		this.sameSameSameTestResults = sameSameSameTestResults;
	}

	public List<FraudInvoiceDtls> getSameSameDifferentTestResults() {
		return sameSameDifferentTestResults;
	}

	public void setSameSameDifferentTestResults(
			List<FraudInvoiceDtls> sameSameDifferentTestResults) {
		this.sameSameDifferentTestResults = sameSameDifferentTestResults;
	}


	public Map<MonthYearKey, Double> getAbnormalInvoiceVolumeResults() {
		return abnormalInvoiceVolumeResults;
	}


	public void setAbnormalInvoiceVolumeResults(
			Map<MonthYearKey, Double> abnormalInvoiceVolumeResults) {
		this.abnormalInvoiceVolumeResults = abnormalInvoiceVolumeResults;
	}


	public Double getRelativeSizeFactor() {
		return relativeSizeFactor;
	}


	public void setRelativeSizeFactor(Double relativeSizeFactor) {
		this.relativeSizeFactor = relativeSizeFactor;
	}
	
	
    
}
