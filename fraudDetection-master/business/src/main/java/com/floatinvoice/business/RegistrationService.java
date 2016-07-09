package com.floatinvoice.business;

import com.floatinvoice.messages.BaseMsg;
import com.floatinvoice.messages.RegistrationStep1SignInDtlsMsg;
import com.floatinvoice.messages.RegistrationStep2CorpDtlsMsg;
import com.floatinvoice.messages.RegistrationStep3UserPersonalDtlsMsg;

public interface RegistrationService {

	BaseMsg registerSignInInfo( RegistrationStep1SignInDtlsMsg msg );
	
	BaseMsg registerOrgInfo( RegistrationStep2CorpDtlsMsg msg );
	
	BaseMsg registerUserBankInfo (RegistrationStep3UserPersonalDtlsMsg msg);

}
