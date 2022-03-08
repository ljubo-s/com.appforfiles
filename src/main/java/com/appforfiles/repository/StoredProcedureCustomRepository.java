package com.appforfiles.repository;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
public class StoredProcedureCustomRepository {
	
	Logger logger = LoggerFactory.getLogger(StoredProcedureCustomRepository.class);

	EntityManager entityManager;

	public StoredProcedureCustomRepository(@Qualifier("db1EntityManagerFactory") EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	/**
	 * 
	 * @param fileName
	 * @return
	 */
	public Map<String, String> importInvoiceStoredProcedure(String fileName) {

		Map<String, String> returnValuesMap = new HashMap<String, String>();
// @formatter:off  
	StoredProcedureQuery importInvoice = entityManager.createStoredProcedureQuery("DUMMY_IMPORT_INVOICE")
		.registerStoredProcedureParameter("filename", String.class, ParameterMode.IN)
		.registerStoredProcedureParameter("message", String.class, ParameterMode.OUT)
		.registerStoredProcedureParameter("status", Long.class, ParameterMode.OUT);
// @formatter:on   
		importInvoice.setParameter("filename", fileName);
		
		try {
			importInvoice.execute();

			returnValuesMap.put("fileName", fileName);
			returnValuesMap.put("status", importInvoice.getOutputParameterValue("status").toString());
			returnValuesMap.put("message", (String) importInvoice.getOutputParameterValue("message"));

		} catch (Exception e) {

//			import to ErrorLog
		}
		return returnValuesMap;
	}

}
