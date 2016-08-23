package com.floatinvoice.messages;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name="RegistrationStep2PersonalAndCorpDtls")
@XmlAccessorType(value=XmlAccessType.PROPERTY)
public class RegistrationStep2CorpDtlsMsg {

	@XmlElement(name="acronym")
	private String acronym;
	
	@XmlElement(name="compName")
	private String compName;
	
	@XmlElement(name="street")
	private String street;
	
	@XmlElement(name="city")
	private String city;
	
	@XmlElement(name="state")
	private String state;
	
	@XmlElement(name="zipCode")
	private String zipCode;
	
	@XmlElement(name="phoneNo")
	private String phoneNo;
	
	@XmlElement(name="orgType")
	private String orgType;

	public String getAcronym() {
		return acronym;
	}

	public void setAcronym(String acronym) {
		this.acronym = acronym;
	}

	public String getCompName() {
		return compName;
	}

	public void setCompName(String companyName) {
		this.compName = companyName;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getOrgType() {
		return orgType;
	}

	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}
	
	
}
