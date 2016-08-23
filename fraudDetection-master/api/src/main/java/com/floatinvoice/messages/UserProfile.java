package com.floatinvoice.messages;

public class UserProfile {

	
	private String orgAcronym;
	
	private String email;
	
	private long userId;
	
	private long orgId;

	private String orgType;
	
	private int registrationStatus;
	
	
	public String getOrgAcronym() {
		return orgAcronym;
	}

	public void setOrgAcronym(String orgAcronym) {
		this.orgAcronym = orgAcronym;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getOrgId() {
		return orgId;
	}

	public void setOrgId(long orgId) {
		this.orgId = orgId;
	}

	public String getOrgType() {
		return orgType;
	}

	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}

	public int getRegistrationStatus() {
		return registrationStatus;
	}

	public void setRegistrationStatus(int registrationStatus) {
		this.registrationStatus = registrationStatus;
	}

	
}
