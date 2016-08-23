package com.floatinvoice.messages;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="baseMsg")
@XmlAccessorType(value=XmlAccessType.PROPERTY)

public class BaseMsg implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@XmlElement(name="msgs")
	private SystemMessages systemMessages;

	@XmlElement(name="refId")
	private String refId;
	
	@XmlElement(name="refIds")
	private List<String> refIds;
	
	
	public SystemMessages getSystemMessages() {
		return systemMessages;
	}

	public void setSystemMessages(SystemMessages systemMessages) {
		this.systemMessages = systemMessages;
	}
	
	public void addInfoMsg( String text, int code ){
		SystemNotification sn = new SystemNotification(text, code);
		if(this.systemMessages == null)
			systemMessages = new SystemMessages();		
		systemMessages.addInfo(sn);
	}
	
	public void addWarnMsg( String text, int code ){		
		if(this.systemMessages == null)
			systemMessages = new SystemMessages();		
		systemMessages.addWarn(new SystemNotification(text, code));
	}
	
	
	public void addErrorMsg( String text, int code ){		
		if(this.systemMessages == null)
			systemMessages = new SystemMessages();		
		systemMessages.addError(new SystemNotification(text, code));
	}

	public String getRefId() {
		return refId;
	}

	public void setRefId(String refId) {
		this.refId = refId;
	}

	public List<String> getRefIds() {
		return refIds;
	}

	public void setRefIds(List<String> refIds) {
		this.refIds = refIds;
	}
	
    
}
