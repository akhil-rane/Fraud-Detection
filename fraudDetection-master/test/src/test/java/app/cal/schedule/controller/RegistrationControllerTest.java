package app.cal.schedule.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.floatinvoice.messages.RegistrationStep1SignInDtlsMsg;
import com.floatinvoice.messages.RegistrationStep2CorpDtlsMsg;
import com.floatinvoice.messages.RegistrationStep3UserPersonalDtlsMsg;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:Beans.xml")
@WebAppConfiguration
public class RegistrationControllerTest {

	
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
	public void testSignInRegistration() throws Exception{
		
		RegistrationStep1SignInDtlsMsg object = new RegistrationStep1SignInDtlsMsg();
		object.setEmail("gnaik@floatinvoice.com");
		object.setPasswd("passwd");
		object.setConfirmPasswd("passwd");
		RequestBuilder req = post("/register/signInInfo")					
					.contentType(MediaType.APPLICATION_JSON).content(objMapper.writeValueAsString(object));
		MvcResult res = mockMvc.perform(req).andDo(print()).andReturn();	
	}
	
	@Test
	public void testStep2Registration() throws Exception{
		
		RegistrationStep2CorpDtlsMsg object = new RegistrationStep2CorpDtlsMsg();
		object.setAcronym("CITIBANK");
		object.setCompName("Citi Bank ");
		object.setStreet("42nd Street");
		object.setCity("New York");
		object.setState("NewYork");
		object.setZipCode("02169");
		object.setOrgType("BANK");
		object.setPhoneNo("6178806856");		
		RequestBuilder req = post("/register/corpInfo")	
				.header("remote-user", "gnaik@floatinvoice.com")
				.contentType(MediaType.APPLICATION_JSON).content(objMapper.writeValueAsString(object));
		MvcResult res = mockMvc.perform(req).andDo(print()).andReturn();	
	}
	
	@Test
	public void testStep3Registration() throws Exception{
		
		RegistrationStep3UserPersonalDtlsMsg object = new RegistrationStep3UserPersonalDtlsMsg();
		object.setAadharCardId("1000000");
		object.setBankAcctNo("000000000000");
		object.setBankName("ICICI");
		object.setDirectorFName("Gautam");
		object.setDirectorLName("Naik");
		object.setIfscCode("ABCDEF");
		object.setPanCardNo("ADUNP1955H");
		RequestBuilder req = post("/register/usrInfo")	
				.header("remote-user", "gnaik@floatinvoice.com")
				.contentType(MediaType.APPLICATION_JSON).content(objMapper.writeValueAsString(object));
		MvcResult res = mockMvc.perform(req).andDo(print()).andReturn();	
	}
	
}
