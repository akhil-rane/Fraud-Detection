package com.floatInvoice.fraudDetectionTest;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.floatInvoice.fraudDetection.FraudDetection;
import com.floatInvoice.fraudDetection.Invoice;
import com.floatInvoice.fraudDetection.Result;

public class FraudDetectionTest {

	public static void main(String[] args) {


		FraudDetection fraudDetection=new FraudDetection();

		//Code to Fetch Invoices from csv File-Start
		List<Invoice> invoiceList=new ArrayList<Invoice>();

		String path="E:\\FloatInvoice\\dataset1.csv";
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(path));
			String line=null;
			while ((line = br.readLine()) != null) {

				Invoice invoice =new Invoice();
				SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

				String arg[]=line.split(",",-1);

				invoice.setInvoiceID(arg[0]);
				invoice.setInvoiceDate(sdf.parse(arg[1]));
				invoice.setPaymentDueDate(sdf.parse(arg[2]));
				invoice.setBuyerID(arg[3]);
				invoice.setSellerID(arg[4]);
				invoice.setInvoiceAmount(Double.parseDouble(arg[5]));
				invoice.setPurchaseOrderID(arg[6]);
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//Code to Fetch Invoices from csv File-End
		
		//Code to fire query and display result-Start
		Result result = fraudDetection.getResults(invoiceList);
		System.out.println("----------------------------------------------------------------------------------------------------");
		System.out.println("OUTPUT:");
		System.out.println(result);
		System.out.println("----------------------------------------------------------------------------------------------------");
		//Code to fire query and display result-End
	}

}
