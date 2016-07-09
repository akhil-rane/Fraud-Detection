package com.floatinvoice.common;

public class InvoiceCounterPartyObject {

	int smeOrgId;
	
	int buyerOrgId;
	
	public InvoiceCounterPartyObject(){
		
	}
	
	public InvoiceCounterPartyObject(int smeOrgId, int buyerOrgId){
		this.smeOrgId = smeOrgId;
		this.buyerOrgId = buyerOrgId;	
	}

	public int getSmeOrgId() {
		return smeOrgId;
	}

	public void setSmeOrgId(int smeOrgId) {
		this.smeOrgId = smeOrgId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + buyerOrgId;
		result = prime * result + smeOrgId;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		InvoiceCounterPartyObject other = (InvoiceCounterPartyObject) obj;
		if (buyerOrgId != other.buyerOrgId)
			return false;
		if (smeOrgId != other.smeOrgId)
			return false;
		return true;
	}
	
	
}
