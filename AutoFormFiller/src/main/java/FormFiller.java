import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.List;

import javax.swing.text.Document;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

public class FormFiller {
	
	private String template_path;
	
	private String output_path;
	
	public FormFiller(String template_path, String output_path) {		
		this.template_path=template_path;
		this.output_path=output_path;
	}
	
	public void generateForm(HashMap<String, String> formData) {
				
		try{			      
			
	        XWPFDocument doc = new XWPFDocument(OPCPackage.open(template_path));
	        for (XWPFParagraph p : doc.getParagraphs()) {
	            List<XWPFRun> runs = p.getRuns();
	            if (runs != null) {
	                for (XWPFRun r : runs) {
	                    String text = r.getText(0);
	                    if (text != null && text.contains("%name%")) {
	                        text = text.replace("%name%", formData.get("%name%"));
	                        r.setText(text, 0);
	                    }
//	                    else if (text != null && text.contains("%address%")) {
//	                        text = text.replace("%address%", formData.get("%address%"));
//	                        r.setText(text, 0);
//	                    }
//	                    else if (text != null && text.contains("%loan_amount%")) {
//	                        text = text.replace("%loan_amount%", formData.get("%loan_amount%"));
//	                        r.setText(text, 0);
//	                    }
//	                    else if (text != null && text.contains("%invoice_date%")) {
//	                        text = text.replace("%invoice_date%", formData.get("%invoice_date%"));
//	                        r.setText(text, 0);
//	                    }
//	                    else if (text != null && text.contains("%payment_date%")) {
//	                        text = text.replace("%payment_date%", formData.get("%payment_date%"));
//	                        r.setText(text, 0);
//	                    }
	                }
	            }
	        }

	        doc.write(new FileOutputStream(output_path));	               
	        
	        System.out.println("PDF Form created...");	    
	        
	        doc.close();
	    
		}catch(Exception e){
	        e.printStackTrace();
	    }
		
	}	
}
