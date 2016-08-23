package com.floatinvoice.converter;

import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

public class AppJacksonHttpMessageConverter extends MappingJackson2HttpMessageConverter {
		@Override
		public boolean canWrite(Class<?> clazz, MediaType mediaType){
					return clazz.equals(String.class) ? false 
							: (this.getObjectMapper().canSerialize(clazz) && canWrite(mediaType));
		
		}
}
