package com.floatinvoice.messages;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="loginDtls")
@XmlAccessorType(value=XmlAccessType.PROPERTY)

public class LoginDtlsMsg extends BaseMsg{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	@XmlElement(name="email")
	private String email;
	

	@XmlElement(name="passwd")
	private String passwd;

	@XmlElement(name="regStatus")
	private String regStatus;
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public String getRegStatus() {
		return regStatus;
	}

	public void setRegStatus(String regStatus) {
		this.regStatus = regStatus;
	}
	
}
