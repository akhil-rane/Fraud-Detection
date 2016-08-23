package com.floatinvoice.business.listeners;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.scheduling.annotation.Scheduled;
import com.floatinvoice.business.dao.InvoiceFileUploadDao;
import com.floatinvoice.messages.FileDetails;

public class FileProcessor {

	InvoiceFileUploadDao invoiceFileUploadDao;

	public FileProcessor(){

	}

	public FileProcessor( InvoiceFileUploadDao invoiceFileUploadDao ){
		this.invoiceFileUploadDao = invoiceFileUploadDao;
	}


	@Scheduled(fixedRate=5000)
	public void processTask( ){
		List<FileDetails> fileList = pollEligibleFiles();
		for(FileDetails fileDetails : fileList){
			try {
				processFile(fileDetails);
				markFileProcessingAsComplete(fileDetails);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}	

	}

	public List<FileDetails> pollEligibleFiles(){
		return invoiceFileUploadDao.pollEligibleFiles();
	}	

	private void processFile(FileDetails fileDetails) throws Exception{
		try {
			int orgId = fileDetails.getCompanyId();
			final Workbook workbook = WorkbookFactory.create(new ByteArrayInputStream(fileDetails.getFileBytes()));
			workbook.getSheetAt(0);
			Sheet sheet = workbook.getSheetAt(0);
			Iterator<Row> rowIterator = sheet.iterator();			
			Map<String, List<Object>> invoiceDtMap = new HashMap<>();			
			while(rowIterator.hasNext()) {
				Row row = rowIterator.next();
				extractRowData(invoiceDtMap, row);
			}
			invoiceFileUploadDao.uploadInvoiceRecords(invoiceDtMap, orgId, fileDetails.getFileId());
		} catch (EncryptedDocumentException | InvalidFormatException
				| IOException e) {
			throw e;
		}
	}

	private void markFileProcessingAsComplete(FileDetails fileDetails){
		invoiceFileUploadDao.markFileProcessingAsComplete(fileDetails);
	}
	
	private void extractRowData(Map<String, List<Object>> invoiceDtMap, Row row) {
		Iterator<Cell> cellIterator = row.cellIterator();
		while(cellIterator.hasNext()) {
			Cell cell = cellIterator.next();
			if( cell.getCellType() == Cell.CELL_TYPE_STRING && row.getRowNum() == 0){
				readHeaderRow(invoiceDtMap, cell);
			}else{
				readOtherRows(invoiceDtMap, cell);
			}
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void readOtherRows(Map<String, List<Object>> invoiceDtMap, Cell cell) {
		switch (cell.getCellType()) {
			case Cell.CELL_TYPE_NUMERIC:
				if (DateUtil.isCellDateFormatted(cell)) {
					if(cell.getColumnIndex() == 0){
						((LinkedList)invoiceDtMap.get("INVOICE_DATE")).add(cell.getDateCellValue());
					}
					if(cell.getColumnIndex() == 1){
						((LinkedList) invoiceDtMap.get("DUE_DATE")).add(cell.getDateCellValue());
					}                                
				} else {
					System.out.println(cell.getNumericCellValue());
					if(cell.getColumnIndex() == 2){
						((LinkedList)invoiceDtMap.get("AMOUNT")).add((Double)cell.getNumericCellValue());
					}
					
				}
				break;
			case Cell.CELL_TYPE_STRING:
				if(cell.getColumnIndex() == 3){
					((LinkedList)invoiceDtMap.get("INVOICE_NO")).add(cell.getStringCellValue());
				}
				if(cell.getColumnIndex() == 4){
					((LinkedList)invoiceDtMap.get("CUSTOMER_NAME")).add(cell.getStringCellValue());
				}
				if(cell.getColumnIndex() == 5){
					((LinkedList)invoiceDtMap.get("DESCRIPTION")).add(cell.getStringCellValue());
				}
				if(cell.getColumnIndex() == 6){
					((LinkedList)invoiceDtMap.get("PURCHASE_ORDER_NO")).add(cell.getStringCellValue());
				}
				break;
		}
	}

	private void readHeaderRow(Map<String, List<Object>> invoiceDtMap, Cell cell) {
		if("INVOICE_DATE".equalsIgnoreCase(cell.getStringCellValue())){
			invoiceDtMap.put("INVOICE_DATE", new LinkedList<>());
		}
		if("DUE_DATE".equalsIgnoreCase(cell.getStringCellValue())){
			invoiceDtMap.put("DUE_DATE", new LinkedList<>());
		}							            
		if("AMOUNT".equalsIgnoreCase(cell.getStringCellValue())){
			invoiceDtMap.put("AMOUNT", new LinkedList<>());
		}
		if("INVOICE_NO".equalsIgnoreCase(cell.getStringCellValue())){
			invoiceDtMap.put("INVOICE_NO", new LinkedList<>());						
		}
		if("CUSTOMER_NAME".equalsIgnoreCase(cell.getStringCellValue())){
			invoiceDtMap.put("CUSTOMER_NAME", new LinkedList<>());						
		}
		if("DESCRIPTION".equalsIgnoreCase(cell.getStringCellValue())){
			invoiceDtMap.put("DESCRIPTION", new LinkedList<>());
		}
		if("PURCHASE_ORDER_NO".equalsIgnoreCase(cell.getStringCellValue())){
			invoiceDtMap.put("PURCHASE_ORDER_NO", new LinkedList<>());
		}		
	}

}
