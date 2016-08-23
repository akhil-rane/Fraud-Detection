package com.floatinvoice.business.dao;

import com.floatinvoice.messages.BaseMsg;
import com.floatinvoice.messages.RegistrationStep2CorpDtlsMsg;
import com.floatinvoice.messages.RegistrationStep3UserPersonalDtlsMsg;

public interface RegistrationDao {

	BaseMsg registerSignInInfo(String userEmail, String password, String confirmedPassword, int regCode);
	
	BaseMsg registerOrgInfo(RegistrationStep2CorpDtlsMsg msg);

	BaseMsg registerUserBankInfo (RegistrationStep3UserPersonalDtlsMsg msg);
}
