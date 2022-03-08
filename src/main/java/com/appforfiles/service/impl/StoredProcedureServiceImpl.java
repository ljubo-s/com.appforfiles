package com.appforfiles.service.impl;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.appforfiles.repository.StoredProcedureCustomRepository;
import com.appforfiles.service.StoredProcedureService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class StoredProcedureServiceImpl implements StoredProcedureService {

	private final StoredProcedureCustomRepository storedProcedureCustomRepository;

	@Override
	public Map<String, String> importInvoiceStoredProcedure(String fileName) {
		return storedProcedureCustomRepository.importInvoiceStoredProcedure(fileName);
	}

}
