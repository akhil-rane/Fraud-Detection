package com.floatinvoice.messages;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name="FraudInvoiceDtls")
@XmlAccessorType(value=XmlAccessType.PROPERTY)
public class FraudInvoiceDtls extends InvoiceDtlsMsg {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@XmlElement(name="invoiceId")
	private int invoiceId;
	
	@XmlElement(name="buyerId")
	private int buyerId;
	
	@XmlElement(name="supplierId")
	private int supplierId;
	
	@XmlElement(name="fraudRefId")
	private String fraudRefId;

	/*@XmlElement(name="fraudTestIds")
	private String fraudTestId;
	
	@XmlElement(name="fraudTestName")
	private String fraudTestName;
	*/
	@XmlElement(name="fraudTestNames")
	private List<String> fraudTestName = new LinkedList<>();
	
	@XmlElement(name="insertDt")
	private Date insertDt;
	
	@XmlElement(name="userId")
	private int userId;
	
	@XmlElement(name="fraudId")
	private int fraudId;
	
	@XmlElement(name="email")
	private String email;
	
	public String getFraudRefId() {
		return fraudRefId;
	}

	public void setFraudRefId(String fraudRefId) {
		this.fraudRefId = fraudRefId;
	}



	public Date getInsertDt() {
		return insertDt;
	}

	public void setInsertDt(Date insertDt) {
		this.insertDt = insertDt;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getFraudId() {
		return fraudId;
	}

	public void setFraudId(int fraudId) {
		this.fraudId = fraudId;
	}

	public int getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(int invoiceId) {
		this.invoiceId = invoiceId;
	}

	public int getBuyerId() {
		return buyerId;
	}

	public void setBuyerId(int buyerId) {
		this.buyerId = buyerId;
	}

	public int getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(int supplierId) {
		this.supplierId = supplierId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<String> getFraudTestName() {
		return fraudTestName;
	}

	public void setFraudTestName(List<String> fraudTestName) {
		this.fraudTestName = fraudTestName;
	}

	
}
