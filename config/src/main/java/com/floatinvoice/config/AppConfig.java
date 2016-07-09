package com.floatinvoice.config;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
import org.springframework.web.servlet.mvc.ParameterizableViewController;
import org.springframework.web.servlet.mvc.SimpleControllerHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.floatinvoice.business.interceptors.SecurityInterceptor;
import com.floatinvoice.converter.AppJacksonHttpMessageConverter;


@Configuration
@EnableWebMvc
@ComponentScan("com.floatinvoice")/*(basePackages = {"com.floatinvoice.esecurity.*"},
excludeFilters = @ComponentScan.Filter(type=FilterType.ASSIGNABLE_TYPE, value=AppConfig.class))*/
public class AppConfig extends WebMvcConfigurerAdapter {

	
    @Bean
    public InternalResourceViewResolver viewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setViewClass(JstlView.class);
        viewResolver.setPrefix("/WEB-INF/");
        viewResolver.setSuffix(".jsp");
        return viewResolver;
    }
    @Bean
    public CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver resolver=new CommonsMultipartResolver();
        resolver.setDefaultEncoding("utf-8");
        return resolver;
    }
    
    @Bean
	public ObjectMapper jacksonObjectMapper(){
		ObjectMapper om = new ObjectMapper();
		om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		AnnotationIntrospector primary = new JacksonAnnotationIntrospector();
		//JaxbAnnotationIntrospector jai = new JaxbAnnotationIntrospector(om.getTypeFactory());
	   // AnnotationIntrospector pair = AnnotationIntrospectorPair.create(primary, secondary);
	    om.setAnnotationIntrospector(primary);
		om.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
		om.setSerializationInclusion(Include.NON_NULL);
		return om;
	}
    
	@Bean
	public RequestMappingHandlerAdapter adapter(){
		RequestMappingHandlerAdapter adapter = new RequestMappingHandlerAdapter();
		//adapter.setContentNegotiationManager(cnManager().getObject());
		adapter.setOrder(1);
		List<HttpMessageConverter<?>> converters = new ArrayList<>();
		converters.add(jacksonConverter());
		adapter.setMessageConverters(converters);
		return adapter;
		
	}
	
	@Bean
	public MappingJackson2HttpMessageConverter jacksonConverter(){
		MappingJackson2HttpMessageConverter jaxConverter = new AppJacksonHttpMessageConverter();
		jaxConverter.setObjectMapper(jacksonObjectMapper());
		return jaxConverter;
	} 
	
	
	
	
	@Bean
	public HandlerMapping landingPageHandlerMapping(){
		SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();
		mapping.setOrder(0);
		ParameterizableViewController pvc = new ParameterizableViewController();
		pvc.setViewName("loginPage");

		mapping.setUrlMap(Collections.singletonMap("/login", pvc));
		return mapping;
	}
	
	@Bean
	public RequestMappingHandlerMapping requestMappingHandlerMapping(){
		RequestMappingHandlerMapping handlerMapping = new RequestMappingHandlerMapping();
		handlerMapping.setOrder(1);
		handlerMapping.setInterceptors(Arrays.asList(new SecurityInterceptor()).toArray());
		//handlerMapping.setContentNegotiationManager(cnManager().getObject());
		return handlerMapping;
	}
	
	
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/css/**").addResourceLocations("/WEB-INF/css/**")/*.setCachePeriod(31556926)*/;
        registry.addResourceHandler("/img/**").addResourceLocations("/WEB-INF/img/**")/*.setCachePeriod(31556926)*/;
        registry.addResourceHandler("/js/**").addResourceLocations("/WEB-INF/js/**")/*.setCachePeriod(31556926)*/;
        registry.addResourceHandler("/html/**").addResourceLocations("/WEB-INF/html/**")/*.setCachePeriod(31556926)*/;
    }

   
	
	@Bean
	public HandlerMapping viewControllerHandlerMapping(){
		SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();
		mapping.setOrder(2);
		
		Map<String, String> jspMapping = jspMapping().getConfig();
		Map<String, ParameterizableViewController> jspViewBeans = new HashMap<>();
		ParameterizableViewController controller;
		for(String url : jspMapping.keySet()){
			controller = new ParameterizableViewController();
			controller.setViewName(jspMapping.get(url));
			jspViewBeans.put(url, controller);
		}
		mapping.setUrlMap(jspViewBeans);
		return mapping;
		
		
	}
	 
	@Bean
	public SimpleControllerHandlerAdapter viewControllerHandlerAdapter(){
		return new SimpleControllerHandlerAdapter();
	}
	
/*	@Bean
	public ContentNegotiationManagerFactoryBean cnManager(){
		ContentNegotiationManagerFactoryBean cmFB = new ContentNegotiationManagerFactoryBean();
		cmFB.setDefaultContentType(MediaType.APPLICATION_JSON);
		cmFB.setFavorParameter(true);
		cmFB.setFavorPathExtension(true);
		cmFB.setIgnoreAcceptHeader(true);
		Properties mediaTypes = new Properties();
		mediaTypes.put(MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE);
		mediaTypes.put(MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_XML_VALUE);
		
		mediaTypes.put("json", MediaType.APPLICATION_JSON_VALUE);
		mediaTypes.put("xml", MediaType.APPLICATION_XML_VALUE);	
		
		cmFB.setMediaTypes(mediaTypes);
		return cmFB;
	}*/
	
	@Bean JspMappingConfig jspMapping(){
		JspMappingConfig config = new JspMappingConfig();
		config.mapJspToUri("/register/signInPage", "registerSignInPage");
		config.mapJspToUri("/register/orgInfoPage", "registerOrgInfoPage");
		config.mapJspToUri("/register/usrInfoPage", "registerUserInfoPage");
		config.mapJspToUri("/finViewPage", "financierView");
		config.mapJspToUri("/buyerView", "buyerView");
		config.mapJspToUri("/adminView", "adminView");
		return config;
	}
	
	protected static class JspMappingConfig{
		private Map<String, String> map = new HashMap<>();
		Map<String, String> getConfig(){
			return map;
		}
		void mapJspToUri( String url, String pathToJsp){
			map.put(url, pathToJsp);
		}
	}


}
