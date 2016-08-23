/**
 * 
 */
package com.floatinvoice.config;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.DispatcherType;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * @author gnaik
 *
 */
public class AppInitializer implements WebApplicationInitializer {

	/* (non-Javadoc)
	 * @see org.springframework.web.WebApplicationInitializer#onStartup(javax.servlet.ServletContext)
	 */
	@Override
	public void onStartup(ServletContext servletContext)
			throws ServletException {

		List<Class<?>> configBeans = new LinkedList<>();
		configBeans.addAll(Arrays.asList(AppConfig.class,
										 DataSourceConfig.class,
										 //LoginSecurityConfig.class,
										 BusinessServiceConfig.class,
										 ReadServicesConfig.class,
										 ListenerSchedulingConfig.class
										 ));
		AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();
		registerConfigBeans(ctx, configBeans);
		//ctx.setConfigLocation("com.floatinvoice");
		ctx.setServletContext(servletContext);
		ServletRegistration.Dynamic servlet = servletContext.addServlet(
				"dispatcher", new DispatcherServlet(ctx));
		servlet.setLoadOnStartup(1);
		servlet.addMapping("/");
		//UrlRewriteFilter filter = new UrlRewriteFilter
		/*servletContext.addFilter("urlRewriteFilter", org.tuckey.web.filters.urlrewrite.UrlRewriteFilter.class)
					  .addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), false, "/*");*/
		
					  //http://stackoverflow.com/questions/14205140/spring-security-without-web-xml
	}

	private void registerConfigBeans( AnnotationConfigWebApplicationContext ctx, List<Class<?>> configBeans){
		
		Class<?> [] annotatedConfigClasses = new Class[configBeans.size()];
		configBeans.toArray(annotatedConfigClasses);
		ctx.register(annotatedConfigClasses);
	}
	
}
