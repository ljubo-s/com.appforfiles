package com.appforfiles.config;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

//@formatter:off  
@Configuration
@EnableJpaRepositories(
		basePackages = "com.appforfiles.repository_db2",
        entityManagerFactoryRef = "db2EntityManagerFactory",
        transactionManagerRef = "db2TransactionManager"
)
//@formatter:on
public class Schema2Config {

	@Autowired
	private Environment env;

	@Bean(name = "db2DataSource")
	@ConfigurationProperties(prefix = "spring.datasource2.hikari")
	public DataSource db2DataSource() {
		return DataSourceBuilder.create().build();
	}

	@Bean(name = "db2TransactionManager")
	public PlatformTransactionManager db2TransactionManager() {
		EntityManagerFactory factory = db2EntityManagerFactory().getObject();
		return new JpaTransactionManager(factory);
	}

	@Bean(name = "db2EntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean db2EntityManagerFactory() {

		LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();

		factory.setDataSource(db2DataSource());
		factory.setPackagesToScan("com.appforfiles.model");
		factory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
		factory.setPersistenceUnitName("db2PU");

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
