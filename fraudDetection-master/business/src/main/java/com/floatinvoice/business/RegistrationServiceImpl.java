package com.floatinvoice.business;

import org.springframework.http.HttpStatus;

import com.floatinvoice.business.dao.RegistrationDao;
import com.floatinvoice.common.RegistrationStatusEnum;
import com.floatinvoice.messages.BaseMsg;
import com.floatinvoice.messages.RegistrationStep1SignInDtlsMsg;
import com.floatinvoice.messages.RegistrationStep2CorpDtlsMsg;
import com.floatinvoice.messages.RegistrationStep3UserPersonalDtlsMsg;

public class RegistrationServiceImpl implements RegistrationService {

	
	RegistrationDao registrationDao;
	
	public RegistrationServiceImpl(){
		
	}
	
	public RegistrationServiceImpl(RegistrationDao registrationDao){
		this.registrationDao = registrationDao;
	}
	
	@Override
	public BaseMsg registerSignInInfo(RegistrationStep1SignInDtlsMsg msg) {		
		String userEmail = msg.getEmail();
		String password = msg.getPasswd();
		String confirmedPassword = msg.getConfirmPasswd();
		int regStatusId = RegistrationStatusEnum.LOGIN.getCode();
		BaseMsg baseMsg = registrationDao.registerSignInInfo(userEmail, password, confirmedPassword, regStatusId);
		baseMsg.addInfoMsg("Message saved successfully", HttpStatus.OK.value());
		return baseMsg;
	}

	@Override
	public BaseMsg registerOrgInfo(RegistrationStep2CorpDtlsMsg msg) {		
		BaseMsg baseMsg = registrationDao.registerOrgInfo(msg);
		baseMsg.addInfoMsg("Message saved successfully", HttpStatus.OK.value());
		return baseMsg;
	}

	@Override
	public BaseMsg registerUserBankInfo(RegistrationStep3UserPersonalDtlsMsg msg) {
		BaseMsg baseMsg = registrationDao.registerUserBankInfo(msg);
		baseMsg.addInfoMsg("Message saved successfully", HttpStatus.OK.value());
		return baseMsg;
	}

}
