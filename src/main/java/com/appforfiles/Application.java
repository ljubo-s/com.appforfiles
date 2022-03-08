package com.appforfiles;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.appforfiles.storage.StorageProperties;

//@formatter:off  
@SpringBootApplication(
		exclude = {
		     HibernateJpaAutoConfiguration.class,
		     DataSourceTransactionManagerAutoConfiguration.class 
		})
//@formatter:on
@EnableTransactionManagement
@EnableConfigurationProperties(StorageProperties.class)
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
