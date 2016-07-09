package com.floatInvoice.fraudDetection;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
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
import org.apache.spark.broadcast.Broadcast;
import org.apache.spark.broadcast.HttpBroadcastFactory;

import scala.Tuple2;

import com.floatinvoice.messages.FraudInvoiceDtls;
import com.google.common.collect.Lists;

public class FraudDetection implements Serializable {

	/*private Map<Integer,Double> actualProportion=new HashMap<Integer, Double>();
	private final String appName="FraudDetector";
	private final String masterAddress="spark://localhost:7077";
	private final String jarPath="D:\\FraudDetection.jar";
	private SparkConf conf;
	private JavaSparkContext context;


	public FraudDetection() {

		conf = new SparkConf().setAppName(appName).setMaster("spark://localhost:7077");		
		context = new JavaSparkContext(conf);
		//context.addJar(jarPath);

	}

	private static final FlatMapFunction<FraudInvoiceDtls, String> EXTRACTOR =
			new FlatMapFunction<FraudInvoiceDtls, String>() {

		public Iterable<String> call(FraudInvoiceDtls invoiceDtls) throws Exception {
			//String arg[]=s.split(",",-1);
			List<String> list =new ArrayList<String>();
			Double PurchaseAmount = invoiceDtls.getAmount();
			//Benfords Law
			list.add(PurchaseAmount.toString().substring(0,1));
			//Purchase Order Missing
			if(invoiceDtls.getInvoicePONo().equals(""))
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
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			Date invoiceDate = sdf.parse(String.valueOf(invoiceDtls.getStartDt()));
			invoiceDateCal.setTime(invoiceDate);
			Date paymentDate = sdf.parse(String.valueOf(invoiceDtls.getPaidDt()));
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

	private static final Function<FraudInvoiceDtls, String> GROUP_SSS = new Function<FraudInvoiceDtls, String>() {

		public String call(FraudInvoiceDtls invoiceDtls) throws Exception {
			//String arg[]=arg0.split(",",-1);

			//return arg[0]+","+arg[1]+","+arg[3]+","+arg[4]+","+arg[5];
			return invoiceDtls.getInvoiceNo() + "," + invoiceDtls.getStartDt() + "," 
			+ invoiceDtls.getBuyerId() + "," + invoiceDtls.getSupplierId() + "," + invoiceDtls.getAmount();

		}

	};

	private static final Function<FraudInvoiceDtls, String> GROUP_SSD =new Function<FraudInvoiceDtls, String>() {

		public String call(FraudInvoiceDtls invoiceDtls) throws Exception {
			//String arg[]=arg0.split(",",-1);

			return invoiceDtls.getInvoiceNo() + "," + invoiceDtls.getStartDt() + "," 
			+ invoiceDtls.getBuyerId() + "," + invoiceDtls.getAmount();
		}
	};

	private static final Function<Tuple2<String, Iterable<FraudInvoiceDtls>>, Boolean> FILTER_DUPLICATES = 
			new Function<Tuple2<String,Iterable<FraudInvoiceDtls>>, Boolean>() {

				public Boolean call(Tuple2<String, Iterable<FraudInvoiceDtls>> arg)
						throws Exception {
					Iterator<FraudInvoiceDtls> itr= arg._2.iterator();
					itr.next();					
					if(itr.hasNext())											
						return true;					
					else 
						return false;
				}
	};   

	private static final FlatMapFunction<Iterable<FraudInvoiceDtls>, FraudInvoiceDtls> IterableToList=
			 new FlatMapFunction<Iterable<FraudInvoiceDtls>, FraudInvoiceDtls>() {

				@Override
				public Iterable<FraudInvoiceDtls> call(Iterable<FraudInvoiceDtls> arg0) throws Exception {

					return Lists.newArrayList(arg0);
				}

	};

	public List<FraudTestResults> doFraudTests (List<FraudInvoiceDtls> list) {


		List<FraudTestResults> results = new ArrayList<>();
	//	InvoicePuller invoicePuller=new InvoicePuller(path);		
		JavaRDD<FraudInvoiceDtls> file =context.parallelize(list);		

		JavaRDD<String> words = file.flatMap(EXTRACTOR);		
		JavaPairRDD<String, Integer> pairs = words.mapToPair(MAPPER);
		JavaPairRDD<String, Integer> counter = pairs.reduceByKey(REDUCER);

		//result.setDuplicateSSS(file.groupBy(GROUP_SSS).filter(FILTER_DUPLICATES).values().flatMap(IterableToList).collect());
		//result.setDuplicateSSD(file.groupBy(GROUP_SSD).filter(FILTER_DUPLICATES).values().flatMap(IterableToList).collect());          

		double totalCount=(double) file.count();

		for (Map.Entry<String, Integer> entry : counter.collectAsMap().entrySet())
		{
			Result result = new Result();
			FraudTestResults ftr = new FraudTestResults();
			results.add(ftr);

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

		return null;
	}*/

	private static final long serialVersionUID = 1L;

	private static final String appName="FraudDetection";

	private static final String masterAddress="spark://localhost:7077";

	private final String jarPath="D:\\Project_FloatInvoice\\jar\\FraudDetection.jar";	

	private  static SparkConf conf;

	private  static JavaSparkContext context;

	//	static{
	//		
	//		//conf.set("spark.driver.allowMultipleContexts", "true");
	//		conf.set("spark.cleaner.ttl", "1000000");		
	//		conf.set("spark.cleaner.referenceTracking", "false");
	//	}

	public FraudDetection() {

		conf = new SparkConf().setAppName(appName).setMaster(masterAddress);
		context = new JavaSparkContext(conf);
		context.addJar(jarPath);
	}

	private static Function<FraudInvoiceDtls, FraudTestResults> FRAUD_TEST = new Function<FraudInvoiceDtls,FraudTestResults>() {

		@Override
		public FraudTestResults call(FraudInvoiceDtls fraudInvoiceDtls) throws Exception {

			FraudTestResults results = new FraudTestResults();
			results.setInvoiceDtls(fraudInvoiceDtls);
			List<Integer> fraudTestIds = new ArrayList<>();
			results.setFraudTestIds(fraudTestIds);

			//Test to check if Purchase Order is missing
			if(fraudInvoiceDtls.getInvoicePONo().trim().equals("")|| fraudInvoiceDtls.getInvoicePONo().equals(null))
				fraudTestIds.add(FraudDetectionTestNameEnums.PurchaseOrder.getCode());
			//Test for Rounded Payment amounts 
			if((fraudInvoiceDtls.getAmount() == Math.floor(fraudInvoiceDtls.getAmount())))
				fraudTestIds.add(FraudDetectionTestNameEnums.RoundedAmts.getCode());
			//Test for Even Amounts
			if( (fraudInvoiceDtls.getAmount() % 1000d )==0)
				fraudTestIds.add(FraudDetectionTestNameEnums.EvenAmts.getCode());

			//Test on Invoice and payment dates
			Calendar startDtCal = new GregorianCalendar();
			Calendar paidDtCal = new GregorianCalendar();

			Date startDt = fraudInvoiceDtls.getStartDt();
			Date paidDt = fraudInvoiceDtls.getPaidDt();

			if ( startDt != null && paidDt != null){
				startDtCal.setTime(startDt);
				paidDtCal.setTime(paidDt);
				int days=(int) ((paidDt.getTime() - startDt.getTime()) / (1000 * 60 * 60 * 24));
				if(days > 30)
					fraudTestIds.add(FraudDetectionTestNameEnums.LatePayments.getCode());
			}
			if( paidDtCal != null && 
					(paidDtCal.get(Calendar.DAY_OF_WEEK)== 7 || 
					paidDtCal.get(Calendar.DAY_OF_WEEK)== 1) ){
				fraudTestIds.add(FraudDetectionTestNameEnums.WeekendPayments.getCode());
			}
			return results;
		}
	};

	private static final FlatMapFunction<FraudInvoiceDtls, String> BENFORDS_LAW_EXTRACTOR=
			new FlatMapFunction<FraudInvoiceDtls, String>(){

		@Override
		public Iterable<String> call(FraudInvoiceDtls fraudInvoiceDtls)
				throws Exception {

			return Arrays.asList(String.valueOf(fraudInvoiceDtls.getAmount()).substring(0,1));
		}

	};
	private static final FlatMapFunction<FraudInvoiceDtls, MonthYearKey> MONTH_YEAR_EXTRACTOR=
			new FlatMapFunction<FraudInvoiceDtls, MonthYearKey>(){

		@Override
		public Iterable<MonthYearKey> call(FraudInvoiceDtls fraudInvoiceDtls)
				throws Exception {
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			String[] monthAndYear=new SimpleDateFormat("MM-yyyy").format(fraudInvoiceDtls.getStartDt()).split("-");
			return Arrays.asList(new MonthYearKey(monthAndYear[0], monthAndYear[1])) ;
		}


	} ;

	private static final PairFunction<String, String, Integer> MAPPER =
			new PairFunction<String, String, Integer>() {

		public Tuple2<String, Integer> call(String s) throws Exception {
			return new Tuple2<String, Integer>(s, 1);
		}
	};

	private static final PairFunction<MonthYearKey, MonthYearKey, Integer> MONTH_YEAR_MAPPER=
			new PairFunction<MonthYearKey, MonthYearKey, Integer>(){

		@Override
		public Tuple2<MonthYearKey, Integer> call(MonthYearKey monthYearKey)
				throws Exception {			

			return new Tuple2<MonthYearKey, Integer>(monthYearKey,1);
		}

	};

	private static final Function<FraudInvoiceDtls, Double> AMOUNT_MAPPER=
			new Function<FraudInvoiceDtls, Double>(){

		@Override
		public Double call(FraudInvoiceDtls fraudInvoiceDtls) throws Exception {
			// TODO Auto-generated method stub
			return fraudInvoiceDtls.getAmount();
		}

	};

	private static final Function2<Integer, Integer, Integer> REDUCER =
			new Function2<Integer, Integer, Integer>() {

		public Integer call(Integer a, Integer b) throws Exception {
			return a + b;
		}
	};	

	private static final Function2<Double, Double, Double> SUM_FINDER=
			new Function2<Double, Double, Double>(){

		@Override
		public Double call(Double a, Double b) throws Exception {

			return a + b;
		}

	};


	private static final Function2<Double, Double, Double> MAX_FINDER=
			new Function2<Double, Double, Double>(){

		@Override
		public Double call(Double a, Double b) throws Exception {
			if(a > b)
				return a;
			else if(a < b)
				return b;
			else
				return a;
		}

	};

	private static final Function<Tuple2<BaseFraudDetectionKey, Iterable<FraudInvoiceDtls>>, Boolean> FILTER_DUPLICATES=
			new Function<Tuple2<BaseFraudDetectionKey, Iterable<FraudInvoiceDtls>>, Boolean>(){

		@Override
		public Boolean call(
				Tuple2<BaseFraudDetectionKey, Iterable<FraudInvoiceDtls>> arg)
						throws Exception {
			Iterator<FraudInvoiceDtls> itr=arg._2.iterator();
			itr.next();					
			if(itr.hasNext())											
				return true;					
			else 
				return false;				
		}				
	};			


	private static final Function<FraudInvoiceDtls, BaseFraudDetectionKey> GROUP_SAME_SAME_SAME=
			new Function<FraudInvoiceDtls, BaseFraudDetectionKey>(){

		@Override
		public BaseFraudDetectionKey call(FraudInvoiceDtls fraudInvoiceDtls)
				throws Exception {
			BaseFraudDetectionKey sameSameSameKey =new SameSameSameKey(fraudInvoiceDtls.getSupplierId(),fraudInvoiceDtls.getInvoiceNo(),fraudInvoiceDtls.getInvoicePONo(), fraudInvoiceDtls.getAmount(),fraudInvoiceDtls.getBuyerId());
			return sameSameSameKey;
		}

	};

	private static final Function<FraudInvoiceDtls, BaseFraudDetectionKey> GROUP_SAME_SAME_DIFFERENT=
			new Function<FraudInvoiceDtls, BaseFraudDetectionKey>(){

		@Override
		public BaseFraudDetectionKey call(FraudInvoiceDtls fraudInvoiceDtls)
				throws Exception {
			BaseFraudDetectionKey sameSameDifferentKey=new SameSameDifferentKey(fraudInvoiceDtls.getSupplierId(),fraudInvoiceDtls.getInvoiceNo(),fraudInvoiceDtls.getInvoicePONo(), fraudInvoiceDtls.getAmount(),fraudInvoiceDtls.getBuyerId());
			return sameSameDifferentKey;
		}		
	};

	private static final FlatMapFunction<Iterable<FraudInvoiceDtls>, FraudInvoiceDtls> ITERABLE_TO_LIST=
			new FlatMapFunction<Iterable<FraudInvoiceDtls>, FraudInvoiceDtls>(){

		@Override
		public Iterable<FraudInvoiceDtls> call(Iterable<FraudInvoiceDtls> iterableFraudInvoiceDtls)
				throws Exception {

			return Lists.newArrayList(iterableFraudInvoiceDtls);
		}	
	};


	public List<FraudTestResults> doFraudTests (List<FraudInvoiceDtls> fraudInvoiceList ) {

		JavaRDD<FraudInvoiceDtls> fraudInvoices = context.parallelize(fraudInvoiceList);

		JavaRDD<FraudTestResults> fraudInvoicesResult = fraudInvoices.map(FRAUD_TEST);

		return fraudInvoicesResult.collect();
	}

	public FraudTestResultsGroupBySupplierID doFraudTests (List<FraudInvoiceDtls> fraudInvoiceList, int supplierId)
	{

		FraudTestResultsGroupBySupplierID fraudTestResultsGroupBySupplierID=new FraudTestResultsGroupBySupplierID(supplierId);		

		JavaRDD<FraudInvoiceDtls> fraudInvoices = context.parallelize(fraudInvoiceList);	

		long numOfFraudInvoices=fraudInvoices.count();

		//Benfords Law Test		
		JavaRDD<String> firstDigitOfAmount = fraudInvoices.flatMap(BENFORDS_LAW_EXTRACTOR);

		JavaPairRDD<String, Integer> tempPairs = firstDigitOfAmount.mapToPair(MAPPER);

		JavaPairRDD<String, Integer> benfordsLawResult = tempPairs.reduceByKey(REDUCER);

		fraudTestResultsGroupBySupplierID.setBenfordsLawTestResults(benfordsLawResult.collectAsMap());

		//Same Same Same Test				

		List<FraudInvoiceDtls> sameSameSameTestResults = fraudInvoices.groupBy(GROUP_SAME_SAME_SAME).filter(FILTER_DUPLICATES).values().flatMap(ITERABLE_TO_LIST).collect();

		fraudTestResultsGroupBySupplierID.setSameSameSameTestResults(sameSameSameTestResults);

		//Same Same Different Test		

		List<FraudInvoiceDtls> sameSameDifferentTestResults = fraudInvoices.groupBy(GROUP_SAME_SAME_DIFFERENT).filter(FILTER_DUPLICATES).values().flatMap(ITERABLE_TO_LIST).collect();

		fraudTestResultsGroupBySupplierID.setSameSameDifferentTestResults(sameSameDifferentTestResults);

		// Abnormal Invoice Volume Activity Test		

		JavaRDD<MonthYearKey> monthYearOfInvoices = fraudInvoices.flatMap(MONTH_YEAR_EXTRACTOR);

		JavaPairRDD<MonthYearKey, Integer> monthYearOfInvoicesPairs = monthYearOfInvoices.mapToPair(MONTH_YEAR_MAPPER);

		JavaPairRDD<MonthYearKey, Integer> noOfInvoicesInMonthYearCount = monthYearOfInvoicesPairs.reduceByKey(REDUCER);

		Map<MonthYearKey, Integer> monthKeyNoOfInvoicesMap=noOfInvoicesInMonthYearCount.collectAsMap();

		List<MonthYearKey> keysMonthYearList=new ArrayList<MonthYearKey>();

		keysMonthYearList.addAll(monthKeyNoOfInvoicesMap.keySet());

		Collections.sort(keysMonthYearList);

		for (int i = 1; i < keysMonthYearList.size(); i++) {

			double noOfInvoicesPrevMonth=monthKeyNoOfInvoicesMap.get(keysMonthYearList.get(i-1));

			double noOfInvoicesCurrentMonth=monthKeyNoOfInvoicesMap.get(keysMonthYearList.get(i));

			if(noOfInvoicesPrevMonth*3<=noOfInvoicesCurrentMonth)	
				fraudTestResultsGroupBySupplierID.getAbnormalInvoiceVolumeResults().put(keysMonthYearList.get(i), Double.valueOf(new DecimalFormat("#.##").format((noOfInvoicesCurrentMonth/noOfInvoicesPrevMonth-1d)*100d)));

		}

		// Relative Size Factor Test		

		JavaRDD<Double> amountRDD = fraudInvoices.map(AMOUNT_MAPPER);		

		Double maxAmount = amountRDD.reduce(MAX_FINDER);		

		Double sumAmount = amountRDD.reduce(SUM_FINDER);

		fraudTestResultsGroupBySupplierID.setRelativeSizeFactor(Double.valueOf(new DecimalFormat("#.##").format((maxAmount/((sumAmount-maxAmount)/(numOfFraudInvoices-1))))));

		// Z-Score test

//		double meanOfAmount=sumAmount/numOfFraudInvoices;
//
//		final Broadcast<Double> broadcastMeanAmount= context.broadcast(meanOfAmount);
//
//		JavaRDD<Double> x = fraudInvoices.map(new Function<FraudInvoiceDtls, Double>() {
//
//			@Override
//			public Double call(FraudInvoiceDtls v1) throws Exception {
//				// TODO Auto-generated method stub
//				double ac=broadcastMeanAmount.value();
//				return v1.getAmount() ;
//			}	
//
//		});
//		System.out.println(x.count());

		return fraudTestResultsGroupBySupplierID;
	}






}