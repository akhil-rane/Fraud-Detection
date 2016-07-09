package com.floatinvoice.esecurity;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.floatinvoice.business.ProfileService;
import com.floatinvoice.business.RegistrationService;
import com.floatinvoice.common.UserContext;
import com.floatinvoice.messages.BaseMsg;
import com.floatinvoice.messages.RegistrationStep1SignInDtlsMsg;
import com.floatinvoice.messages.RegistrationStep2CorpDtlsMsg;
import com.floatinvoice.messages.RegistrationStep3UserPersonalDtlsMsg;

@Controller
@RequestMapping("/register")
public class RegistrationController {

	@Autowired
	ProfileService profileService;
	
	@Autowired
	RegistrationService registrationSvc;

	
	@RequestMapping(value = { "/signInInfo"}, method = RequestMethod.POST)
    public ResponseEntity<BaseMsg> signInInfo(@RequestBody RegistrationStep1SignInDtlsMsg regStep1,
    		 HttpSession session) {
		String email = regStep1.getEmail();
		initializeSession(email, session);
		
        return new ResponseEntity<>(registrationSvc.registerSignInInfo(regStep1), HttpStatus.OK);
    }
	
	@RequestMapping(value = { "/corpInfo"}, method = RequestMethod.POST)
    public ResponseEntity<BaseMsg> regCorpInfo(@RequestBody RegistrationStep2CorpDtlsMsg regStep2 ) {
       // ModelAndView model = new ModelAndView();
       // model.setViewName("homePage");
        return new ResponseEntity<>(registrationSvc.registerOrgInfo(regStep2), HttpStatus.OK);
    }
	
	@RequestMapping(value = { "/usrInfo"}, method = RequestMethod.POST)
    public ResponseEntity<BaseMsg> regUserInfo(@RequestBody RegistrationStep3UserPersonalDtlsMsg regStep3 ) {
       // ModelAndView model = new ModelAndView();
       // model.setViewName("homePage");
        return new ResponseEntity<>(registrationSvc.registerUserBankInfo(regStep3), HttpStatus.OK);
	}
	
	 protected void initializeSession(String email, HttpSession session){	    	
    	if(session != null){
    		session.setAttribute("remote-user", email);
    		UserContext.addContextData(email, null, null, 0);
    	}
    	
	  }
}
