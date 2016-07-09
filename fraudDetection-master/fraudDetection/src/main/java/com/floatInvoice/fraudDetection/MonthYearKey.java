package com.floatInvoice.fraudDetection;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MonthYearKey implements Serializable,Comparable<MonthYearKey> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String month;
	
	private String year;
	
	private SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

	public MonthYearKey(String month, String year) {
		super();
		this.month = month;
		this.year = year;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((month == null) ? 0 : month.hashCode());
		result = prime * result + ((year == null) ? 0 : year.hashCode());
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
		MonthYearKey other = (MonthYearKey) obj;
		if (month == null) {
			if (other.month != null)
				return false;
		} else if (!month.equals(other.month))
			return false;
		if (year == null) {
			if (other.year != null)
				return false;
		} else if (!year.equals(other.year))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "MonthYearKey [month=" + month + ", year=" + year + "]";
	}
	
	public int compareTo(MonthYearKey monthYearKey) {
		
		try {
		Date date1 =sdf.parse("01-"+this.month+"-"+this.year);
		
		Date date2 =sdf.parse("01-"+monthYearKey.getMonth()+"-"+monthYearKey.getYear());
		
		if(date1.before(date2))
			return -1;
		else if(date1.after(date2))
			return 1;
		else
			return 0;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			
		}
		return 0;
	}
	
	
	
}
