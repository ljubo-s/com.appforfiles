package com.appforfiles.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.appforfiles.model.Log;
import com.appforfiles.repository.LogRepository;
import com.appforfiles.service.LogService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class LogServiceImpl implements LogService {

	private final LogRepository logRepository;

	@Override
	public List<Log> getAll() {
		return logRepository.findAll();
	}

	@Override
	public Optional<Log> getById(Integer id) {
//    	return Log Object
//    	return logRepository.findById(id).orElse(new Log());
		return logRepository.findById(id);
	}

	@Override
	public void saveOrUpdate(Log log) {
		logRepository.save(log);
	}

	@Override
	public void deleteById(Integer id) {
		logRepository.deleteById(id);
	}

	@Override
	public void delete(Log log) {
		logRepository.delete(log);
	}

	@Override
	public Log getLogBySessionId(String sessionId) {
		return logRepository.getLogBySessionId(sessionId);
	}

}
