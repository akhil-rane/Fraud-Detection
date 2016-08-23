package com.floatinvoice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.floatinvoice.business.dao.BankInfoDao;
import com.floatinvoice.business.dao.FraudInvoiceInfoDao;
import com.floatinvoice.business.dao.InvoiceFileUploadDao;
import com.floatinvoice.business.dao.InvoiceInfoDao;
import com.floatinvoice.business.dao.JdbcBankInfoDao;
import com.floatinvoice.business.dao.JdbcFraudInvoiceInfoDao;
import com.floatinvoice.business.dao.JdbcInvoiceFileUploadDao;
import com.floatinvoice.business.dao.JdbcInvoiceInfoDao;
import com.floatinvoice.business.dao.JdbcOrgReadDao;
import com.floatinvoice.business.dao.JdbcProfileDao;
import com.floatinvoice.business.dao.JdbcRegistrationDao;
import com.floatinvoice.business.dao.OrgReadDao;
import com.floatinvoice.business.dao.ProfileDao;
import com.floatinvoice.business.dao.RegistrationDao;

@Configuration
public class ReadServicesConfig {
	
	@Autowired
	DataSourceConfig dataSourceConfig;


	@Bean
	public InvoiceInfoDao invoiceInfoReadDao(){
		return new JdbcInvoiceInfoDao(dataSourceConfig.dataSource(), orgReadDao());
	}
	
	@Bean
	public InvoiceFileUploadDao invoiceFileUploadDao(){
		return new JdbcInvoiceFileUploadDao( dataSourceConfig.dataSource(), dataSourceConfig.lobHandler(), orgReadDao() );
	}
	
	@Bean
	public OrgReadDao orgReadDao(){
		return new JdbcOrgReadDao(dataSourceConfig.dataSource());
	}
	
	@Bean
	public ProfileDao profileDao(){
		return new JdbcProfileDao(dataSourceConfig.dataSource(), orgReadDao());
	}
	
	@Bean
	public RegistrationDao registrationDao(){
		return new JdbcRegistrationDao(dataSourceConfig.dataSource());
	}
	
	@Bean
	public BankInfoDao bankInfoDao(){
		return new JdbcBankInfoDao(orgReadDao(), invoiceInfoReadDao(), dataSourceConfig.dataSource());
	}
	
	@Bean
	public FraudInvoiceInfoDao fraudInvoiceInfoDao(){
		return new JdbcFraudInvoiceInfoDao(dataSourceConfig.dataSource());
	}
}
