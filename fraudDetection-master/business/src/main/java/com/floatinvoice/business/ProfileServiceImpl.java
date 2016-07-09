package com.floatinvoice.business;

import com.floatinvoice.business.dao.ProfileDao;
import com.floatinvoice.messages.UserProfile;

public class ProfileServiceImpl implements ProfileService {

	
	ProfileDao profileDao;
	
	public ProfileServiceImpl(){
		
	}
	
	public ProfileServiceImpl(ProfileDao profileDao){
		this.profileDao = profileDao;
	}
	
	@Override
	public UserProfile fetchUserProfile(String userEmail) {
		return profileDao.fetchUserProfileDetails(userEmail);
	}

	@Override
	public int findUserRegistrationStatus(String userEmail) {
		return profileDao.findUserRegistrationStatus(userEmail);
	}

	

}
