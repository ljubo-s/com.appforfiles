package com.appforfiles.service;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.springframework.web.multipart.MultipartFile;

public interface FileHandlerService {

	public StringBuilder excelToCsv(XSSFSheet sheet);

	public int rowNumberOfFirstEmptyCell(XSSFSheet sheetXssf);

	public XSSFSheet getSheet(String storedXlsxFilePath, int sheetNumber);

	public XSSFSheet checkFile(String storedXlsxFilePath);

	public boolean checkFileBeforeUpload(MultipartFile file);

	public String formatClientValidMessages();

	public String formatClientErrorMessages();

	public void addClientValidMessages(String clientValidMessage);

	public void addClientErrorMessages(String errorMessage);
}
