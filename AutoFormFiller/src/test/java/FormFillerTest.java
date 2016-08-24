import java.util.HashMap;


public class FormFillerTest {

	public static void main(String[] args) {
				
		String template_path="data/application_form.docx";
		String output_path="data/output_form.pdf";
		FormFiller formFiller = new FormFiller(template_path,output_path);		
		
		HashMap<String, String> formData = new HashMap<String, String>();
		formData.put("%name%", "ABC Corp.");
		formData.put("%address%", "Tech Park, Goa, India");
		formData.put("%loan_amount%", "210000");
		formData.put("%invoice_date%", "01-01-2016");
		formData.put("%payment_date%", "10-02-2016");
		
		formFiller.generateForm(formData);
	}
}
