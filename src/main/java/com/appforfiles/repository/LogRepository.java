package com.appforfiles.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.appforfiles.model.Log;

public interface LogRepository extends JpaRepository<Log, Integer> {
	
	@Query("SELECT log FROM Log log WHERE log.sid = :sid")
	public Log getLogBySessionId(@Param("sid") String sid);

	@Override
	@Query("SELECT log FROM Log log LEFT JOIN FETCH log.fileSet f")
	public List <Log> findAll();


}
