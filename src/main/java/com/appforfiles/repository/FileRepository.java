package com.appforfiles.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.appforfiles.model.File;

public interface FileRepository extends JpaRepository<File, Integer> {


}
