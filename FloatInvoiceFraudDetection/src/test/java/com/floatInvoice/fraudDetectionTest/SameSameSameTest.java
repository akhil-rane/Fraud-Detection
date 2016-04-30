package com.floatInvoice.fraudDetectionTest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;

import scala.Tuple2;

import com.floatInvoice.fraudDetection.InvoicePuller;
import com.google.common.collect.Lists;

public class SameSameSameTest {



	private static final Function<Tuple2<String, Iterable<String>>, Boolean> FILTER_DUPLICATES = 
			new Function<Tuple2<String,Iterable<String>>, Boolean>() {

				public Boolean call(Tuple2<String, Iterable<String>> arg)
						throws Exception {
					Iterator<String> itr=arg._2.iterator();
					itr.next();					
					if(itr.hasNext())											
						return true;					
					else 
						return false;
				}
	};   
	
	private static final Function<String, String> GROUP_SSS =new Function<String, String>() {
		
		public String call(String arg0) throws Exception {
			String arg[]=arg0.split(",",-1);
			
			return arg[0]+","+arg[1]+","+arg[3]+","+arg[4]+","+arg[5];
		}
	};
	
	private static final Function<String, String> GROUP_SSD =new Function<String, String>() {
		
		public String call(String arg0) throws Exception {
			String arg[]=arg0.split(",",-1);
			
			return arg[0]+","+arg[1]+","+arg[3]+","+arg[5];
		}
	};
	
	private static final FlatMapFunction<Iterable<String>, String> IterableToList=
			 new FlatMapFunction<Iterable<String>, String>() {

				public Iterable<String> call(Iterable<String> iterable)
						throws Exception {
					
					return Lists.newArrayList(iterable) ;
				}
	};

	
	public static void main(String[] args) {

		final String appName="FraudDetector";
		final String masterAddress="spark://192.168.56.1:7077";
		final String jarPath="D:\\FraudDetection.jar";
		SparkConf conf;
		JavaSparkContext context;

		conf = new SparkConf().setAppName(appName).setMaster("spark://192.168.56.1:7077");		
		context = new JavaSparkContext(conf);
		context.addJar(jarPath);

		InvoicePuller invoicePuller=new InvoicePuller("D:\\dataset1.csv");		
		JavaRDD<String> file =context.parallelize(invoicePuller.getData());	

		JavaPairRDD<String, Iterable<String>> group_sss=file.groupBy(GROUP_SSS);

		
	
		 
		List<String> duplicates = group_sss.filter(FILTER_DUPLICATES).values().flatMap(IterableToList).collect();
		 System.out.println(duplicates);
		
		
		
	}
}
