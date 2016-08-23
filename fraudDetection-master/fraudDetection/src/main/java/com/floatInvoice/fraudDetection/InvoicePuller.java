package com.floatInvoice.fraudDetection;

import java.io.BufferedReader;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.floatinvoice.messages.FraudInvoiceDtls;

public class InvoicePuller {

	private String path;
	
	private SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
	
	public InvoicePuller(String path) {
		this.path=path;
	}
	
	public List<FraudInvoiceDtls> getData()
	{
		List<FraudInvoiceDtls> data=null; 
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(path));
		    String line;
		    data=new ArrayList<FraudInvoiceDtls>();
		    
			while ((line = br.readLine()) != null) {
				String arg[]=line.split(",",-1);
				FraudInvoiceDtls fraudInvoiceDtls=new FraudInvoiceDtls();
				fraudInvoiceDtls.setInvoiceNo(arg[0]);
				fraudInvoiceDtls.setStartDt(sdf.parse(arg[1]));
				fraudInvoiceDtls.setPaidDt(sdf.parse(arg[2]));
				fraudInvoiceDtls.setBuyerId(Integer.parseInt(arg[3]));
				fraudInvoiceDtls.setSupplierId(Integer.parseInt(arg[4]));
				fraudInvoiceDtls.setAmount(Double.parseDouble(arg[5]));
				fraudInvoiceDtls.setInvoicePONo(arg[6]);
				data.add(fraudInvoiceDtls);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return data;
	}
	
}
