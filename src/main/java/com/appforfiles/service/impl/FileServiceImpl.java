package com.appforfiles.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.appforfiles.model.File;
import com.appforfiles.repository.FileRepository;
import com.appforfiles.service.FileService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class FileServiceImpl implements FileService {

	private final FileRepository fileRepository;

	@Override
	public List<File> getAll() {
		return fileRepository.findAll();
	}

	@Override
	public Optional<File> getById(Integer id) {
		return fileRepository.findById(id);
	}

	@Override
	public void saveOrUpdate(File file) {
		fileRepository.save(file);
	}

	@Override
	public void deleteById(Integer id) {
		fileRepository.deleteById(id);
	}

	@Override
	public void delete(File file) {
		fileRepository.delete(file);
	}

}
