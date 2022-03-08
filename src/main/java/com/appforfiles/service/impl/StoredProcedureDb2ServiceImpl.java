package com.appforfiles.service.impl;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.appforfiles.repository_db2.StoredProcedureCustomRepositoryDb2;
import com.appforfiles.service.StoredProcedureDb2Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class StoredProcedureDb2ServiceImpl implements StoredProcedureDb2Service {

	private final StoredProcedureCustomRepositoryDb2 storedProcedureCustomRepositoryDb2;

	public Map<String, String> importStornoStoredProcedure(String fileName) {
		return storedProcedureCustomRepositoryDb2.importStornoStoredProcedure(fileName);
	}

}
