package com.floatInvoice.fraudDetection;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class InvoicePuller {

	private String path;
	public InvoicePuller(String path) {
		this.path=path;
	}
	
	public List<String> getData()
	{
		List<String> data =new ArrayList<String>();
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(path));
		    String line;
			while ((line = br.readLine()) != null) {
				data.add(line);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return data;
	}
	
}
