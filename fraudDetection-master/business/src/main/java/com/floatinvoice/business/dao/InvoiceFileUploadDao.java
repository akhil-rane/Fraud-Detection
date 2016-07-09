package com.floatinvoice.business.dao;

import java.util.List;
import java.util.Map;

import com.floatinvoice.messages.BaseMsg;
import com.floatinvoice.messages.FileDetails;
import com.floatinvoice.messages.UploadMessage;

public interface InvoiceFileUploadDao {

	//BaseMsg fileUpload( UploadMessage msg ) throws Exception;

	BaseMsg fileUpload(UploadMessage msg)
			throws Exception;

	List<FileDetails> pollEligibleFiles();

	void uploadInvoiceRecords(Map<String, List<Object>> invoiceDtMap, int orgId, int fileId);
	
	int markFileProcessingAsComplete(FileDetails fileDetails);
}
