package com.floatinvoice.messages;

import org.springframework.web.multipart.MultipartFile;


public class UploadMessage extends BaseMsg{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private MultipartFile file;
	
	public UploadMessage(){
		
	}
	
	public UploadMessage(MultipartFile multiPartFile){
		this.file = multiPartFile;
	}
	
	private String fileName;
	
	private String smeAcronym;

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getSmeAcronym() {
		return smeAcronym;
	}

	public void setSmeAcronym(String smeAcronym) {
		this.smeAcronym = smeAcronym;
	}
	
	
}
