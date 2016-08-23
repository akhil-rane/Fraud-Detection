package com.floatinvoice.test.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.util.Arrays;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.floatinvoice.business.dao.JdbcInvoiceFileUploadDao;
import com.floatinvoice.common.UserContext;
import com.floatinvoice.messages.BaseMsg;
import com.floatinvoice.messages.BuyerActionMsg;
import com.floatinvoice.messages.FileDetails;
import com.floatinvoice.messages.InvoiceAccountInfoMsg;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:Beans.xml")
@WebAppConfiguration
public class InvoiceControllerTest {

	
	@Autowired
	private WebApplicationContext wac;
	private ObjectMapper objMapper;
	private MockMvc mockMvc;
	
	@Before
	public void setup(){
		this.mockMvc = webAppContextSetup(this.wac).build();
		objMapper = wac.getBean(ObjectMapper.class);
	}
	
	@Test
	public void testViewInvoices() throws Exception{
		RequestBuilder req = get("/invoice/view?acro=COTIND").contentType(MediaType.APPLICATION_JSON);
		MvcResult res = mockMvc.perform(req).andDo(print()).andReturn();	
	}
	
	@Test
	public void testViewPaidInvoices() throws Exception{
		RequestBuilder req = get("/invoice/view/paid?acro=COTIND").contentType(MediaType.APPLICATION_JSON)
				.header("remote-user", "abc.xyz@gmail.com");;
		MvcResult res = mockMvc.perform(req).andDo(print()).andReturn();	
	}
	
	@Test
	public void testFinancierViewInvoices() throws Exception{
		RequestBuilder req = get("/invoice/view?acro=CITIBANK").contentType(MediaType.APPLICATION_JSON)
				.header("remote-user", "gnaik@floatinvoice.com");
		MvcResult res = mockMvc.perform(req).andDo(print()).andReturn();	
	}
	
	
	@Test
	public void testUploadFiles() throws Exception{
		Resource resource = new ClassPathResource("Sandbox.xlsx");
		MockMultipartFile file = new MockMultipartFile("file", "Sandbox.xlsx", "", resource.getInputStream());
		
		MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
		RequestBuilder reqBuilder = MockMvcRequestBuilders.fileUpload("/invoice/upload?acro=COTIND&filename=Sandbox.xlsx")
				.file(file).header("remote-user", "abc.xyz@gmail.com");
		
		mockMvc.perform(reqBuilder).andDo(print());		
	}
	
	@Test
	public void testCreditInvoices() throws Exception{
/*
		095C87D0211D4B33A6BC2C76F5F55BA9
43BC95641BE5432C9F3909CFB64DF30D
68714DFE6CA94EB0BA401A5DFB6EF8AD
1C3DEC9207C84305823A6320951B0368*/
		BaseMsg msg = new BaseMsg();
		msg.setRefId("095C87D0211D4B33A6BC2C76F5F55BA9");
		
		RequestBuilder req = post("/invoice/credit").header("remote-user", "abc.xyz@gmail.com")
				.contentType(MediaType.APPLICATION_JSON).content(objMapper.writeValueAsString(msg));
		MvcResult res = mockMvc.perform(req).andDo(print()).andReturn();
		
		BaseMsg msg2 = new BaseMsg();
		msg2.setRefId("43BC95641BE5432C9F3909CFB64DF30D");
		RequestBuilder req2 = post("/invoice/credit").header("remote-user", "abc.xyz@gmail.com")
				.contentType(MediaType.APPLICATION_JSON).content(objMapper.writeValueAsString(msg2));
		MvcResult res2 = mockMvc.perform(req2).andDo(print()).andReturn();

		BaseMsg msg3 = new BaseMsg();
		msg3.setRefId("68714DFE6CA94EB0BA401A5DFB6EF8AD");
		RequestBuilder req3 = post("/invoice/credit").header("remote-user", "abc.xyz@gmail.com")
				.contentType(MediaType.APPLICATION_JSON).content(objMapper.writeValueAsString(msg3));
		MvcResult res3 = mockMvc.perform(req3).andDo(print()).andReturn();

		BaseMsg msg4 = new BaseMsg();
		msg4.setRefId("1C3DEC9207C84305823A6320951B0368");
		RequestBuilder req4 = post("/invoice/credit").header("remote-user", "abc.xyz@gmail.com")
				.contentType(MediaType.APPLICATION_JSON).content(objMapper.writeValueAsString(msg4));
		MvcResult res4 = mockMvc.perform(req4).andDo(print()).andReturn();
		
		/*BaseMsg msg5 = new BaseMsg();
		msg4.setRefId("714F29A2C6BD4FC0832A510936550AD4");
		RequestBuilder req5 = post("/invoice/credit").header("remote-user", "abc.xyz@gmail.com")
				.contentType(MediaType.APPLICATION_JSON).content(objMapper.writeValueAsString(msg5));
		MvcResult res5 = mockMvc.perform(req4).andDo(print()).andReturn();	
*/	}
	
	@Test
	public void testPoolInfoDtls() throws Exception{		
		RequestBuilder req = get("/invoice/B30373612DA14ED18BE636BE0ABBE0E0?acro=COTIND").header("remote-user", "abc.xyz@gmail.com")
		.contentType(MediaType.APPLICATION_JSON);
		MvcResult res = mockMvc.perform(req).andDo(print()).andReturn();
	}
	
	@Test
	public void testFundedEndPoint() throws Exception{		
		RequestBuilder req = get("/invoice/funded?acro=COTIND").header("remote-user", "abc.xyz@gmail.com")
		.contentType(MediaType.APPLICATION_JSON);
		MvcResult res = mockMvc.perform(req).andDo(print()).andReturn();
	}
	
	
	@Test
	public void testBidInvoices() throws Exception{
		BaseMsg msg = new BaseMsg();
		msg.setRefIds(Arrays.asList("A94CCB5C51B34D24887C4795975A562B"));
		UserContext.addContextData(null, "CITIBANK", null, 0);
		System.out.println(" ******* " + objMapper.writeValueAsString(msg));		
		RequestBuilder req = post("/invoice/bid?acro=CITIBANK").header("remote-user", "gnaik@floatinvoice.com")
				.contentType(MediaType.APPLICATION_JSON).content(objMapper.writeValueAsString(msg));
		MvcResult res = mockMvc.perform(req).andDo(print()).andReturn();	
	}
	//280CD63E608445E581D32D6BDABA435D
	
	@Test
	public void testAcceptBid() throws Exception{
		BaseMsg msg = new BaseMsg();
		msg.setRefId("280CD63E608445E581D32D6BDABA435D");
		RequestBuilder req = post("/invoice/acceptBid").header("remote-user", "abc.xyz@gmail.com")
				.contentType(MediaType.APPLICATION_JSON).content(objMapper.writeValueAsString(msg));
		MvcResult res = mockMvc.perform(req).andDo(print()).andReturn();	
	}
	
	@Test
	public void testViewAcceptedBids() throws Exception{		
		RequestBuilder req = get("/invoice/viewBids?acro=CITIBANK").header("remote-user", "gnaik@floatinvoice.com")
		.contentType(MediaType.APPLICATION_JSON);
		MvcResult res = mockMvc.perform(req).andDo(print()).andReturn();
	
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void testFilePoll(){
		JdbcInvoiceFileUploadDao ifud = new JdbcInvoiceFileUploadDao();
		List<FileDetails> result = ifud.pollEligibleFiles();
		for(FileDetails r : result){
			
			System.out.println(r.getCompanyId());
			System.out.println(r.getFileName());
		}
		Assert.assertTrue(result.size()>0);
	}
	
	@Test
	public void testCreateInvoiceAccount() throws Exception{
		InvoiceAccountInfoMsg msg = new InvoiceAccountInfoMsg();
		msg.setAcro("COTIND");
		RequestBuilder req = post("/invoice/account")				
				.contentType(MediaType.APPLICATION_JSON)
				.header("remote-user", "abc.xyz@gmail.com")
				.content(objMapper.writeValueAsString(msg));
		MvcResult res = mockMvc.perform(req).andDo(print()).andReturn();	
	}
	
	@Test
	public void testPendingApprovalInvoices() throws Exception{
	
	
		RequestBuilder req = get("/invoice/pending/approval?acro=BEDNBATH").header("remote-user", "askforgautam@gmail.com")
				.contentType(MediaType.APPLICATION_JSON);
		MvcResult res = mockMvc.perform(req).andDo(print()).andReturn();
	}
	
	
	
	@Test
	public void testFraudInvoices() throws Exception{
		RequestBuilder req = get("/invoice/fraudAll?acro=COTIND").header("remote-user", "abc.xyz@gmail.com")
				.contentType(MediaType.APPLICATION_JSON);
		MvcResult res = mockMvc.perform(req).andDo(print()).andReturn();
	}
	
	@Test
	public void testFraudInvoicesForOneAcro() throws Exception{
		RequestBuilder req = get("/invoice/fraud?acro=COTIND").header("remote-user", "abc.xyz@gmail.com")
				.contentType(MediaType.APPLICATION_JSON);
		MvcResult res = mockMvc.perform(req).andDo(print()).andReturn();
	}
	
	
	
	@Test
	public void testManageBuyerApprovalInvoices() throws Exception{
		
		BuyerActionMsg msg = new BuyerActionMsg();
		msg.setAction(0);
		msg.setRefId("7EACBB8259A44956A628BFC6EA85618D");
		RequestBuilder req = post("/invoice/manage/approval")
				.contentType(MediaType.APPLICATION_JSON)
				.header("remote-user", "askforgautam@gmail.com")
				.content(objMapper.writeValueAsString(msg));
		MvcResult res = mockMvc.perform(req).andDo(print()).andReturn();
	}
}
