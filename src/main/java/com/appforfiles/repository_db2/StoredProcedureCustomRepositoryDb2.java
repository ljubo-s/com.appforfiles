package com.appforfiles.repository_db2;

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
public class StoredProcedureCustomRepositoryDb2 {

	Logger logger = LoggerFactory.getLogger(StoredProcedureCustomRepositoryDb2.class);

	EntityManager entityManager;

	public StoredProcedureCustomRepositoryDb2(@Qualifier("db2EntityManagerFactory") EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public Map<String, String> importStornoStoredProcedure(String fileName) {

		Map<String, String> returnValuesMap = new HashMap<String, String>();

		try {
// @formatter:off 
            StoredProcedureQuery importStorno = entityManager.createStoredProcedureQuery("DUMMY_IMPORT_STORNO")
    		.registerStoredProcedureParameter("filename", String.class, ParameterMode.IN)
     		.registerStoredProcedureParameter("message", String.class, ParameterMode.OUT)
    		.registerStoredProcedureParameter("status", Long.class, ParameterMode.OUT);
 
// @formatter:on

            importStorno.setParameter("filename", fileName);

			try {

				importStorno.execute();
				
				returnValuesMap.put("fileName", fileName);
				returnValuesMap.put("message", (String) importStorno.getOutputParameterValue("message"));
				returnValuesMap.put("status", importStorno.getOutputParameterValue("status").toString());

			} catch (Exception e) {

// import to ErrorLog				
				logger.error("importStornoStoredProcedure, e: " + e.toString());
			}

		} catch (Exception ex) {
// import to ErrorLog				
			logger.error("importStornoStoredProcedure, ex: " + ex.toString());
		}

// import to AuditLog

		return returnValuesMap;
	}

}
