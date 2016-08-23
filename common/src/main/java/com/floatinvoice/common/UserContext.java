package com.floatinvoice.common;

import java.util.UUID;

public class UserContext {
	
	private UserContext(){
		
	}
	
	private static final ThreadLocal<UserData> context = new ThreadLocal<UserData>(){
		
		@Override
		protected UserData initialValue() {
			return new UserData();
		}
	};

	public static void clear(){
		context.remove();
	}
	
	public static String getUserName(){
		return context.get().userName;
	}
	
	public static String getRequestId(){
		return context.get().requestId;
	}
	
	public static String getUserType(){
		return context.get().userType;
	}
	
	/*public static long getAccountNo(){
		return context.get().accountNo;
	}*/
	
	public static String getAcronym(){
		return context.get().acronym;
	}
	
	public static int getSourceApp(){
		return context.get().sourceApp;
	}
	
	public static void addContextData(String userName, /*String accountNo,*/ String acronym, String userType, int sourceApp){
		final UserData userData = new UserData();
		userData.acronym = acronym;
		userData.userName = userName;
		userData.userType = userType;
		userData.sourceApp = sourceApp;
		context.set(userData);
	}
	
	protected static class UserData{
		
		private String userName;
		private String requestId = UUID.randomUUID().toString();
	//	private long accountNo;
		private String acronym;
		private String userType;
		private int sourceApp;
	}
}
