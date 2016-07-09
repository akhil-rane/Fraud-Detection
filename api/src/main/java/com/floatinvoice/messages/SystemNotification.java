package com.floatinvoice.messages;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="msg")
@XmlAccessorType(value=XmlAccessType.PROPERTY)
public class SystemNotification implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int code;
	private String msgText;
	
	public SystemNotification(){
		
	}
	public SystemNotification(String text, int code) {
		this.msgText = text;
		this.code =code;
	}
	
	public SystemNotification(String text) {
		this.msgText = text;
	}
	
	@XmlAttribute(name="code", required=false)
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	
	@XmlAttribute(name="text", required=false)
	public String getMsgText() {
		return msgText;
	}
	public void setMsgText(String msgText) {
		this.msgText = msgText;
	}

	
}

