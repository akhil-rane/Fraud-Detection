package com.floatinvoice.business.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.floatinvoice.common.UserContext;

public class SecurityInterceptor extends AbstractInterceptor{

	public static boolean isHomePageController(HttpServletRequest request){
		
		if(request.getRequestURI().contains("loginSubmit") || 
				request.getRequestURI().contains("signInInfo") ){
			return true;
		}
		return false;
	}
	
	@Override
	public boolean beforeControllerMethodExecutes(HttpServletRequest request, HttpServletResponse response,
			Object handler) throws Exception {
		
		// Look up the userId value in request.
		String userName = lookupUserName(request);
		String acronym = lookupAcronym(request);
		
		if( userName != null){
			UserContext.addContextData(userName, acronym, null, 0);
			return true;
		}else{
			if (isHomePageController(request)){
				return true;
			}else{
				userName = lookupUserNameInSession(request);
				UserContext.addContextData(userName, acronym, null, 0);
				return true;
			}
		}
	}
	
	public static String lookupUserName(HttpServletRequest request){
		String userName = request.getHeader("remote-user");
		return userName;		
	}
	
	public static String lookupAcronym(HttpServletRequest request){
		String acronym = request.getParameter("acro");
		return acronym;
	}
	
	public static String lookupUserNameInSession(HttpServletRequest request){
		HttpSession session = request.getSession(false);
		if(session == null){
			throw new RuntimeException(" User not found in the Httpsession...");
		}else{
			String uName = (String)session.getAttribute("remote-user");
			System.out.println("Fetched Remote User from session, " + uName);
			if (uName == null)
				throw new RuntimeException(" User not found in the Httpsession...");
			return uName;
		}
	}
}
