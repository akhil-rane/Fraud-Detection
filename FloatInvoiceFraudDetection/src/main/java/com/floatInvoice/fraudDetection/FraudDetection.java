package com.floatInvoice.fraudDetection;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.api.java.function.VoidFunction;

import scala.Tuple2;

public class FraudDetection {
	
	private Map<Integer,Double> actualProportion=new HashMap<Integer, Double>();
	private final String appName="FraudDetector";
	private final String masterAddress="spark://10.244.32.56:7077";
	private final String jarPath="E:\\FloatInvoice\\FraudDetection.jar";
	private SparkConf conf;
	private JavaSparkContext context;
	
	
	public FraudDetection() {
		
		conf = new SparkConf().setAppName(appName).setMaster(masterAddress);		
		context = new JavaSparkContext(conf);
		context.addJar(jarPath);
		
	}
		
	private static final FlatMapFunction<Invoice, String> EXTRACTOR =
			new FlatMapFunction<Invoice, String>() {
			
		public Iterable<String> call(Invoice invoice) throws Exception {
			
			List<String> list =new ArrayList<String>();
			Double PurchaseAmount=invoice.getInvoiceAmount();
			
			//Benfords Law
			list.add(PurchaseAmount.toString().substring(0,1));
			
			//Purchase Order Missing
			if(invoice.getPurchaseOrderID().equals(""))
				list.add("PurchaseOrderMissing");				
			
			//Rounded Payment amounts 
			if((PurchaseAmount == Math.floor(PurchaseAmount)))
				list.add("RoundedValue");					
			
			//Test for Even Amounts
			if((PurchaseAmount%1000d)==0)
				list.add("EvenAmount");			
			
			//Length in days between Invoice and payment dates
			 Calendar invoiceDateCal = new GregorianCalendar();
		     Calendar paymentDateCal = new GregorianCalendar();		    

		     Date invoiceDate = invoice.getInvoiceDate();
		     invoiceDateCal.setTime(invoiceDate);
		     Date paymentDate = invoice.getPaymentDueDate();
		     paymentDateCal.setTime(paymentDate);

		     int days=(int) ((paymentDate.getTime() - invoiceDate.getTime()) / (1000 * 60 * 60 * 24));
			 if(days > 30)
				 list.add("DaysToPaymentExceedThreshold");
		     
			 if(paymentDateCal.get(Calendar.DAY_OF_WEEK)==7)
				 list.add("PaymentMadeOnSaturday");
			 else if(paymentDateCal.get(Calendar.DAY_OF_WEEK)==1)
				 list.add("PaymentMadeOnSunday");
			return list;
		}

		
	};

	private static final PairFunction<String, String, Integer> MAPPER =
			new PairFunction<String, String, Integer>() {

		public Tuple2<String, Integer> call(String s) throws Exception {
			return new Tuple2<String, Integer>(s, 1);
		}
		
	};

	private static final Function2<Integer, Integer, Integer> REDUCER =
			new Function2<Integer, Integer, Integer>() {

		public Integer call(Integer a, Integer b) throws Exception {
			return a + b;
		}
	};

	private static final VoidFunction<String> SSS_TEST=new VoidFunction<String>() {

		public void call(String arg0) throws Exception {
			
			
		}
		
		
	};
	
	public Result getResults (List invoiceList) {
        
		Result result=new Result();
			
		JavaRDD<Invoice> file =context.parallelize(invoiceList);		
		JavaRDD<String> words = file.flatMap(EXTRACTOR);		
		JavaPairRDD<String, Integer> pairs = words.mapToPair(MAPPER);
		JavaPairRDD<String, Integer> counter = pairs.reduceByKey(REDUCER);
		
		
		
        System.out.println(counter.collectAsMap());            
        
        double totalCount=(double) file.count();

		for (Map.Entry<String, Integer> entry : counter.collectAsMap().entrySet())
		{
			
			try
			{
				actualProportion.put(Integer.parseInt(entry.getKey()), ((double)entry.getValue()/totalCount)*100d);
			}
			catch(NumberFormatException e)
			{
				if(entry.getKey().equals("PurchaseOrderMissing"))
					result.setPurchaseOrderMissing(entry.getValue());
				else if(entry.getKey().equals("RoundedValue"))
					result.setRoundedValues(entry.getValue());
				else if(entry.getKey().equals("EvenAmount"))
					result.setEvenAmounts(entry.getValue());
				else if(entry.getKey().equals("DaysToPaymentExceedThreshold"))
					result.setDaysToPaymentExceedThreshold(entry.getValue());
				else if(entry.getKey().equals("PaymentMadeOnSaturday"))
					result.setPaymentMadeOnSaturday(entry.getValue());
				else if(entry.getKey().equals("PaymentMadeOnSunday"))
					result.setPaymentMadeOnSunday(entry.getValue());
			}
			
			result.setBenfordsLawResult(actualProportion);

		}

		return result;
	}
}
