package com.floatinvoice.test.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.util.Calendar;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.floatinvoice.business.ProfileService;
import com.floatinvoice.messages.BankDtlsMsg;
import com.floatinvoice.messages.LoanDtlsMsg;
import com.floatinvoice.messages.LoanInstallmentsDtlsMsg;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:Beans.xml")
@WebAppConfiguration
public class BankControllerTest {

	@Autowired
	private WebApplicationContext wac;
	
	@Autowired
	private ProfileService profileSvc;
	
	private ObjectMapper objMapper;
	private MockMvc mockMvc;
	
	@Before
	public void setup(){
		this.mockMvc = webAppContextSetup(this.wac).build();
		objMapper = wac.getBean(ObjectMapper.class);
	}
	
	@Test
	public void testSaveAccount() throws Exception{
		BankDtlsMsg msg = new BankDtlsMsg();
		msg.setAcctType("SAVINGS");
		msg.setBankAcctNo("002000000001");
		msg.setBankName("US Bank");
		msg.setBranchName("Boston");
		msg.setIfscCode("IFSCCode");
		
		RequestBuilder req = post("/bank/saveAccount?acro=COTIND")				
				.contentType(MediaType.APPLICATION_JSON)
				.header("remote-user", "abc.xyz@gmail.com")
				.content(objMapper.writeValueAsString(msg));
		MvcResult res = mockMvc.perform(req).andDo(print()).andReturn();	
	}
	
	@Test
	public void testApproveLoan() throws Exception{
		LoanDtlsMsg msg = new LoanDtlsMsg();
		msg.setPoolRefId("123");
		msg.setLoanAmt(new Double(100000));
		msg.setLoanDispatchDt(new Date());
		msg.setFinancierAcro("CITIBANK");
		msg.setSmeAcro("COTIND");		
		Calendar cal = Calendar.getInstance();
		cal.setTime(msg.getLoanDispatchDt());
		cal.add(Calendar.MONTH, 4);
		msg.setLoanCloseDt(cal.getTime());
		
		System.out.println(" Request Msg " + objMapper.writeValueAsString(msg));
		
		RequestBuilder req = post("/bank/approveLoan")				
				.contentType(MediaType.APPLICATION_JSON)
				.header("remote-user", "abc.xyz@gmail.com")
				.content(objMapper.writeValueAsString(msg));
		MvcResult res = mockMvc.perform(req).andDo(print()).andReturn();
	}
	
	@Test
	public void testLoanPayment() throws Exception{
		LoanInstallmentsDtlsMsg msg = new LoanInstallmentsDtlsMsg();
		msg.setAmt(new Double(1000));
		msg.setFinancier("CITIBANK");
		msg.setLoanRefId("68F95E08BEAE45FB915A54921847A5F8");
		msg.setSmeAcro("COTIND");
		msg.setPaidDt(new Date());
		msg.setPaymentDueDt(new Date());
		System.out.println(objMapper.writeValueAsString(msg));
		RequestBuilder req = post("/bank/payLoan")				
				.contentType(MediaType.APPLICATION_JSON)
				.header("remote-user", "abc.xyz@gmail.com")
				.content(objMapper.writeValueAsString(msg));
		MvcResult res = mockMvc.perform(req).andDo(print()).andReturn();
	}

	@Test
	public void testViewAllLoans() throws Exception{
		RequestBuilder req = get("/bank/viewAllLoans?acro=CITIBANK").contentType(MediaType.APPLICATION_JSON)
				.header("remote-user", "gnaik@floatinvoice.com");
		MvcResult res = mockMvc.perform(req).andDo(print()).andReturn();	
	}
	
	@Test
	public void testViewActiveLoans() throws Exception{
		RequestBuilder req = get("/bank/viewActiveLoans?acro=COTIND").contentType(MediaType.APPLICATION_JSON)
				.header("remote-user", "abc.xyz@gmail.com");
		MvcResult res = mockMvc.perform(req).andDo(print()).andReturn();	
	}
}
