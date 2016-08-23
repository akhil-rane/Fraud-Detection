package com.floatinvoice.test.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

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
import org.springframework.util.Assert;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.floatinvoice.business.ProfileService;
import com.floatinvoice.messages.LoginDtlsMsg;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:Beans.xml")
@WebAppConfiguration
public class LoginControllerTest {

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
	public void testLoginForSession() throws Exception{
		LoginDtlsMsg msg = new LoginDtlsMsg();
		msg.setEmail("askforgautam@gmail.com");
		msg.setPasswd("test123");
		
		System.out.println(" Json " + objMapper.writeValueAsString(msg));
		
		RequestBuilder req = post("/loginSubmit")
				
				.contentType(MediaType.APPLICATION_JSON)
				.content(objMapper.writeValueAsString(msg));
		MvcResult res = mockMvc.perform(req).andDo(print()).andReturn();	
	}
	
	@Test
	public void testProfileRegStatus(){
		int result = profileSvc.findUserRegistrationStatus("naikgautamg@gmail.com");
		Assert.isTrue(result == 3 );
	}
	
}
