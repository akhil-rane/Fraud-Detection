package com.floatinvoice.messages;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="invoiceAcctInfo")
@XmlAccessorType(value=XmlAccessType.PROPERTY)
public class InvoiceAccountInfoMsg extends BaseMsg{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@XmlElement(name="fIAcctNo")
	int fIAcctNo;

	@XmlElement(name="fIRefId")
	String fIRefId;
	
	@XmlElement(name="acro")
	String acro;
	
	@XmlElement(name="status")
	int status;

	public int getfIAcctNo() {
		return fIAcctNo;
	}

	public void setfIAcctNo(int fIAcctNo) {
		this.fIAcctNo = fIAcctNo;
	}

	public String getfIRefId() {
		return fIRefId;
	}

	public void setfIRefId(String fIRefId) {
		this.fIRefId = fIRefId;
	}

	public String getAcro() {
		return acro;
	}

	public void setAcro(String acro) {
		this.acro = acro;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
}
