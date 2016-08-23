package com.floatInvoice.fraudDetectionTest;

import java.util.List;

import com.floatInvoice.fraudDetection.FraudDetection;
import com.floatInvoice.fraudDetection.FraudTestResultsGroupBySupplierID;
import com.floatInvoice.fraudDetection.InvoicePuller;
import com.floatinvoice.messages.FraudInvoiceDtls;


public class FraudDetectionTest {

	public static void main(String[] args) {

		String path="D:\\Project_FloatInvoice\\dataset\\dataset1.csv";

		FraudDetection fraudDetection=new FraudDetection();

		InvoicePuller invoicePuller=new InvoicePuller(path);

		List<FraudInvoiceDtls> fraudInvoiceList=invoicePuller.getData();

		FraudTestResultsGroupBySupplierID fraudTestResultsGroupBySupplierID=fraudDetection.doFraudTests(fraudInvoiceList, 21);

		System.out.println("----------------------------------------------------------------------------------------------------");
		System.out.println("OUTPUT:");
		System.out.println("1)Benfords Law");
		System.out.println(fraudTestResultsGroupBySupplierID.getBenfordsLawTestResults());
		System.out.println("\n2)Same-Same-Same Test");		
		for (FraudInvoiceDtls fraudInvoiceDtls : fraudTestResultsGroupBySupplierID.getSameSameSameTestResults()) {
			System.out.println("------------------------------------------------------");
			System.out.println("Invoice No:"+fraudInvoiceDtls.getInvoiceNo());
			System.out.println("Purchase No:"+fraudInvoiceDtls.getInvoicePONo());
			System.out.println("Buyer Id:"+fraudInvoiceDtls.getBuyerId());
			System.out.println("Supplier Id:"+fraudInvoiceDtls.getSupplierId());
			System.out.println("Payment Amout:"+fraudInvoiceDtls.getAmount());
		}
		System.out.println("\n3)Same-Same-Different Test");
		for (FraudInvoiceDtls fraudInvoiceDtls : fraudTestResultsGroupBySupplierID.getSameSameDifferentTestResults()) {
			System.out.println("------------------------------------------------------");
			System.out.println("Invoice No:"+fraudInvoiceDtls.getInvoiceNo());
			System.out.println("Purchase No:"+fraudInvoiceDtls.getInvoicePONo());
			System.out.println("Buyer Id:"+fraudInvoiceDtls.getBuyerId());
			System.out.println("Supplier Id:"+fraudInvoiceDtls.getSupplierId());
			System.out.println("Payment Amout:"+fraudInvoiceDtls.getAmount());
		}
		System.out.println("\n4)Abnormal Activity Test");
		System.out.println(fraudTestResultsGroupBySupplierID.getAbnormalInvoiceVolumeResults());	
		System.out.println("\n5)Relative Size Factor Test");
		System.out.println(fraudTestResultsGroupBySupplierID.getRelativeSizeFactor());	
	}

}
