package com.floatinvoice.messages;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name="RegistrationStep3PersonalAndCorpDtls")
@XmlAccessorType(value=XmlAccessType.PROPERTY)
public class RegistrationStep3UserPersonalDtlsMsg {

	@XmlElement(name="bankAcctNo")
	private String bankAcctNo;

	@XmlElement(name="ifscCode")
	private String ifscCode;

	@XmlElement(name="bankName")
	private String bankName;

	@XmlElement(name="directorFName")
	private String directorFName;

	@XmlElement(name="directorLName")
	private String directorLName;

	@XmlElement(name="panCardNo")
	private String panCardNo;
	
	@XmlElement(name="aadharCardId")
	private String aadharCardId;

	public String getBankAcctNo() {
		return bankAcctNo;
	}

	public void setBankAcctNo(String bankAcctNo) {
		this.bankAcctNo = bankAcctNo;
	}

	public String getIfscCode() {
		return ifscCode;
	}

	public void setIfscCode(String ifscCode) {
		this.ifscCode = ifscCode;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getDirectorFName() {
		return directorFName;
	}

	public void setDirectorFName(String directorFName) {
		this.directorFName = directorFName;
	}

	public String getDirectorLName() {
		return directorLName;
	}

	public void setDirectorLName(String directorLName) {
		this.directorLName = directorLName;
	}

	public String getPanCardNo() {
		return panCardNo;
	}

	public void setPanCardNo(String panCardNo) {
		this.panCardNo = panCardNo;
	}

	public String getAadharCardId() {
		return aadharCardId;
	}

	public void setAadharCardId(String aadharCardId) {
		this.aadharCardId = aadharCardId;
	}
	
	
}
