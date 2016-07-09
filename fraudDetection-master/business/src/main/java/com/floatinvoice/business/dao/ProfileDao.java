package com.floatinvoice.business.dao;

import com.floatinvoice.messages.UserProfile;

public interface ProfileDao {

	UserProfile fetchUserProfileDetails( String userEmail );
	
	int findUserRegistrationStatus( String userEmail );
	
	/*boolean isCompanyRegistered( String userEmail );
	
	boolean isPersonalInfoRegistered( String userEmail );*/
}
