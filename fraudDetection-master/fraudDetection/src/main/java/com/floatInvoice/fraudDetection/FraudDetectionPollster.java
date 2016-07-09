package com.floatInvoice.fraudDetection;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.floatInvoice.fraudDetection.jdbc.JdbcDispatcher;

@EnableScheduling
public class FraudDetectionPollster {

	public static ConcurrentLinkedQueue<DispatcherKey> assignmentQueue = new ConcurrentLinkedQueue<>();
	
	JdbcDispatcher dispatcher;

	public FraudDetectionPollster(){}

	public FraudDetectionPollster( JdbcDispatcher dispatcher ){
		this.dispatcher = dispatcher;
	}

	@Scheduled(fixedRate=5000)
	public void processTask( ){
		List<DispatcherKey> list = dispatcher.execute();
		ExecutorService executor = Executors.newFixedThreadPool(1);
		for (DispatcherKey entry : list){
			System.out.println("Current entry" + entry);
			if(assignmentQueue.contains(entry)){
				System.out.println("Skipping entry" + entry);
				continue;
			}else{
				System.out.println("Processing queue entry" + entry);
				assignmentQueue.add(entry);
			}
			WorkerThread worker = new WorkerThread(dispatcher.getJdbcTemplate(), entry);
			Future future = executor.submit(worker);
		}
		executor.shutdown();
        while (!executor.isTerminated()) {
        }
	}

}
