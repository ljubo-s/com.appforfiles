package com.appforfiles.config;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

//@formatter:off  
@Configuration
@EnableJpaRepositories(
		basePackages = "com.appforfiles.repository",
		entityManagerFactoryRef = "db1EntityManagerFactory",
		transactionManagerRef = "db1TransactionManager"
)
//@formatter:on 
public class Schema1Config {

	@Autowired
	private Environment env;

	@Bean(name = "db1DataSource")
	@ConfigurationProperties(prefix = "spring.datasource.hikari")
	@Primary
	public DataSource db1DataSource() {
		return DataSourceBuilder.create().build();
	}

	@Bean(name = "db1TransactionManager")
	public PlatformTransactionManager db1TransactionManager() {
		EntityManagerFactory factory = db1EntityManagerFactory().getObject();
		return new JpaTransactionManager(factory);
	}

	@Bean(name = "db1EntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean db1EntityManagerFactory() {

		LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();

		factory.setDataSource(db1DataSource());
		factory.setPackagesToScan("com.appforfiles.model");
		factory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
		factory.setPersistenceUnitName("db1PU");

		Properties jpaProperties = new Properties();

		jpaProperties.put("open-in-view", env.getProperty("spring.jpa.open-in-view"));
		jpaProperties.put("hibernate.ddl-auto", env.getProperty("spring.jpa.hibernate.ddl-auto"));
		jpaProperties.put("hibernate.temp.use_jdbc_metadata_defaults", env.getProperty("spring.jpa.properties.hibernate.temp.use_jdbc_metadata_defaults"));
		jpaProperties.put("hibernate.use-new-id-generator-mappings", env.getProperty("spring.jpa.hibernate.use-new-id-generator-mappings"));
		jpaProperties.put("hibernate.dialect", env.getProperty("spring.jpa.properties.hibernate.dialect"));
		jpaProperties.put("hibernate.use-new-id-generator-mappings", env.getProperty("spring.jpa.hibernate.use-new-id-generator-mappings"));
		jpaProperties.put("hibernate.physical_naming_strategy", env.getProperty("spring.jpa.properties.hibernate.physical_naming_strategy"));
//		jpaProperties.put("show-sql", env.getProperty("spring.jpa.show-sql"));
//		jpaProperties.put("hibernate.format_sql", env.getProperty("spring.jpa.properties.hibernate.format_sql"));

		factory.setJpaProperties(jpaProperties);

		return factory;
	}
}
