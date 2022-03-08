package com.appforfiles.storage;

import java.nio.file.Path;
import java.util.stream.Stream;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

	void store(MultipartFile file, String fileName, String invoiceOrRoming);

	boolean copyFile(String sourcePathString, String destionationPathString);

	Stream<Path> loadAll();

	Path load(String filename);

	Resource loadAsResource(String filename);

}