package com.appforfiles.storage;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class StorageServiceImpl implements StorageService {

	private Logger logger = LoggerFactory.getLogger(StorageServiceImpl.class);

	private final Path invoiceLocation;
	private final Path stornoLocation;
	private Path rootLocation;

	@Autowired
	public StorageServiceImpl(StorageProperties storageProperties) {

		this.invoiceLocation = Paths.get(storageProperties.getInvoiceOrginalXlsxLocation());
		this.stornoLocation = Paths.get(storageProperties.getStornoOrginalXlsxLocation());
		rootLocation = Paths.get(storageProperties.getInvoiceOrginalXlsxLocation());
	}

	/**
	 * @param file
	 * @param fileName
	 * @param invoiceOrStorno
	 */
	@Override
	public void store(MultipartFile file, String fileName, String invoiceOrStorno) {

		try {

			if (file.isEmpty()) {
				throw new StorageException("Failed to store empty file.");
			}

			if (invoiceOrStorno.equals(FilesInfo.INVOICE.getValue())) {

//@formatter:off     
				rootLocation = this.invoiceLocation.resolve(Paths.get(fileName))
                                         			.normalize()
                                         			.toAbsolutePath();
//@formatter:on  
			}

			if (invoiceOrStorno.equals(FilesInfo.STORNO.getValue())) {
//@formatter:off  	 
				rootLocation = this.stornoLocation.resolve(Paths.get(fileName))
												  .normalize()
												  .toAbsolutePath();
//@formatter:on
			}
			
//			Path destinationFile = rootLocation;

			try (InputStream inputStream = file.getInputStream()) {

				Files.copy(inputStream, rootLocation, StandardCopyOption.REPLACE_EXISTING);
//				return rootLocation.toString();
			}

		} catch (IOException e) {
			throw new StorageException("Failed to store file.", e);
		}

	}

	/**
	 * @param sourcePathString
	 * @param destinationPathString
	 * @return boolean
	 */
	public boolean copyFile(String sourcePathString, String destinationPathString) {

		Path sourcePath = Paths.get(sourcePathString);
		Path destinationPath = Paths.get(destinationPathString);

		try {
			Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException ex) {
			throw new StorageException("I/O Error when copying file");
		}

		return true;
	}

	
	@Override
	public Stream<Path> loadAll() {

		try {
// @formatter:off 
		  return Files.walk(this.rootLocation, 1)
				  .filter(path -> !path.equals(this.rootLocation))
				  .map(this.rootLocation::relativize);
//@formatter:on
		} catch (IOException e) {
			throw new StorageException("Failed to read stored files", e);
		}
	}

	/**
	 * @param fileName
	 * @return
	 */
	@Override
	public Path load(String fileName) {
		logger.warn("public Path load(String fileName), fileName: " + fileName);
		return rootLocation.resolve(fileName);
	}

	/**
	 * @param fileName
	 */
	@Override
	public Resource loadAsResource(String fileName) {

		try {
			Path file = load(fileName);
			Resource resource = new UrlResource(file.toUri());

			if (resource.exists() || resource.isReadable()) {

				return resource;
			} else {

				throw new StorageFileNotFoundException("Could not read file: " + fileName);
			}
		} catch (MalformedURLException e) {
			throw new StorageFileNotFoundException("Could not read file: " + fileName, e);
		}
	}

}
