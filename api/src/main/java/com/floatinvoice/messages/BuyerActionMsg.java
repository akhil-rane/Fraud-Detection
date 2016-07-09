package com.floatinvoice.messages;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name="BuyerActionDtls")
@XmlAccessorType(value=XmlAccessType.PROPERTY)
public class BuyerActionMsg extends BaseMsg {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int action;

	@XmlElement(name="action")
	public int getAction() {
		return action;
	}

	public void setAction(int action) {
		this.action = action;
	}
	
	
}
