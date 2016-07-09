package com.floatinvoice.messages;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="list")
@XmlAccessorType(value=XmlAccessType.PROPERTY)
public class ListMsg<T extends BaseMsg> extends BaseMsg {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private List<T> list;
	
	private Integer count;
	
	public ListMsg(){}
	
	public ListMsg(List<T> list){
		this.list = list;
	}

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	

}
