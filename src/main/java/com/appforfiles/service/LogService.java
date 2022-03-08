package com.appforfiles.service;

import java.util.List;
import java.util.Optional;
import com.appforfiles.model.Log;

public interface LogService {

	public List<Log> getAll();

	public Optional<Log> getById(Integer id);

	public void saveOrUpdate(Log log);

	public void deleteById(Integer id);

	void delete(Log log);

	public Log getLogBySessionId(String sessionId);
}
