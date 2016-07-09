package com.floatinvoice.messages;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="BankDtls")
@XmlAccessorType(value=XmlAccessType.PROPERTY)
public class BankDtlsMsg extends BaseMsg {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@XmlElement(name="bankAcctNo")
	String bankAcctNo;
	
	@XmlElement(name="bankName")
	String bankName;
	
	@XmlElement(name="ifscCode")
	String ifscCode;

	@XmlElement(name="branchName")
	String branchName;
	
	@XmlElement(name="acro")
	String acro;
	
	@XmlElement(name="acctType")
	String acctType;
	
	@XmlElement(name="poolRefId")
	String poolRefId;
	
	public String getBankAcctNo() {
		return bankAcctNo;
	}

	public void setBankAcctNo(String bankAcctNo) {
		this.bankAcctNo = bankAcctNo;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getIfscCode() {
		return ifscCode;
	}

	public void setIfscCode(String ifscCode) {
		this.ifscCode = ifscCode;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public String getAcctType() {
		return acctType;
	}

	public void setAcctType(String acctType) {
		this.acctType = acctType;
	}

	public String getAcro() {
		return acro;
	}

	public void setAcro(String acro) {
		this.acro = acro;
	}

	public String getPoolRefId() {
		return poolRefId;
	}

	public void setPoolRefId(String poolRefId) {
		this.poolRefId = poolRefId;
	}

	
	
}
