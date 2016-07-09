package com.floatinvoice.messages;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="sysMsgs")
@XmlAccessorType(value=XmlAccessType.PROPERTY)
public class SystemMessages implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public SystemMessages(){
		
	}
	
	private List<SystemNotification> errors;
	private List<SystemNotification> warnings;
	private List<SystemNotification> info;

	@XmlElement(name="error")
	public List<SystemNotification> getErrors() {
		return errors;
	}
	public void setErrors(List<SystemNotification> errors) {
		this.errors = errors;
	}
	
	@XmlElement(name="warn")
	public List<SystemNotification> getWarnings() {
		return warnings;
	}
	public void setWarnings(List<SystemNotification> warnings) {
		this.warnings = warnings;
	}
	
	@XmlElement(name="info")
	public List<SystemNotification> getInfo() {
		return info;
	}
	public void setInfo(List<SystemNotification> info) {
		this.info = info;
	}
	
	
	public void addError(SystemNotification error){
		if (this.errors == null)
			this.errors = new LinkedList<>();
		this.errors.add(error);
	}
	
	public void addWarn(SystemNotification warn){
		if (this.warnings == null)
			this.warnings = new LinkedList<>();
		this.warnings.add(warn);
		
	}
	
	public void addInfo(SystemNotification info){
		if (this.info == null)
			this.info = new LinkedList<>();
		this.info.add(info);
	}
	
	public boolean hasErrors(){
		return errors == null ? false: errors.size() > 0;
	}
	
}
