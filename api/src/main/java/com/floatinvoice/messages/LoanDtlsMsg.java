package com.floatinvoice.messages;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="LoanDtls")
@XmlAccessorType(value=XmlAccessType.PROPERTY)

public class LoanDtlsMsg extends BaseMsg {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@XmlElement(name="loanId")
	private int loanId;
	
	@XmlElement(name="loanRefId")
	private String loanRefId;
	
	@XmlElement(name="poolRefId")
	private String poolRefId;
	
	@XmlElement(name="poolId")
	private String poolId;
	
	@XmlElement(name="loanStatus")
	private String loanStatus;
	
	@XmlElement(name="loanAmt")
	private Double loanAmt;
	
	@XmlElement(name="loanDispatchDt")
	private Date loanDispatchDt;

	@XmlElement(name="loanCloseDt")
	private Date loanCloseDt;
	
	@XmlElement(name="smeAcro")
	private String smeAcro;
	
	@XmlElement(name="financierAcro")
	private String financierAcro;

	@XmlElement(name="interestRate")
	private double interestRate;
	
	@XmlElement(name="loanPeriod")
	private int loanPeriod;
	
	@XmlElement(name="emis")
	private List<LoanInstallmentsDtlsMsg> emis = new LinkedList<>();

	public int getLoanId() {
		return loanId;
	}

	public void setLoanId(int loanId) {
		this.loanId = loanId;
	}

	public String getLoanRefId() {
		return loanRefId;
	}

	public void setLoanRefId(String loanRefId) {
		this.loanRefId = loanRefId;
	}

	public String getLoanStatus() {
		return loanStatus;
	}

	public void setLoanStatus(String loanStatus) {
		this.loanStatus = loanStatus;
	}

	public Double getLoanAmt() {
		return loanAmt;
	}

	public void setLoanAmt(Double loanAmt) {
		this.loanAmt = loanAmt;
	}

	public Date getLoanDispatchDt() {
		return loanDispatchDt;
	}

	public void setLoanDispatchDt(Date loanDispatchDt) {
		this.loanDispatchDt = loanDispatchDt;
	}

	public Date getLoanCloseDt() {
		return loanCloseDt;
	}

	public void setLoanCloseDt(Date loanCloseDt) {
		this.loanCloseDt = loanCloseDt;
	}

	public String getSmeAcro() {
		return smeAcro;
	}

	public void setSmeAcro(String smeAcro) {
		this.smeAcro = smeAcro;
	}

	public String getFinancierAcro() {
		return financierAcro;
	}

	public void setFinancierAcro(String financierAcro) {
		this.financierAcro = financierAcro;
	}

	public List<LoanInstallmentsDtlsMsg> getEmis() {
		return emis;
	}

	public void setEmis(List<LoanInstallmentsDtlsMsg> emis) {
		this.emis = emis;
	}

	public String getPoolRefId() {
		return poolRefId;
	}

	public void setPoolRefId(String poolRefId) {
		this.poolRefId = poolRefId;
	}

	public String getPoolId() {
		return poolId;
	}

	public void setPoolId(String poolId) {
		this.poolId = poolId;
	}

	public double getInterestRate() {
		return interestRate;
	}

	public void setInterestRate(double interestRate) {
		this.interestRate = interestRate;
	}

	public int getLoanPeriod() {
		return loanPeriod;
	}

	public void setLoanPeriod(int loanPeriod) {
		this.loanPeriod = loanPeriod;
	}

	
}
