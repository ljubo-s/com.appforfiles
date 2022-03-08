package com.appforfiles.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.appforfiles.service.FileHandlerService;

@Service
public class FileHandlerServiceImpl implements FileHandlerService {

	Logger logger = LoggerFactory.getLogger(FileHandlerServiceImpl.class);

	private final static String SEPARATOR = "<EOFD>";
	private final static String END_OF_RECORD = "<EORD>";
	private final static int MAX_FILE_NAME_LENGTH = 50;
	private int sheetNumber = 0;
	private int fromRow = 1;
	private int toRow;
	private boolean haveError = false;

	private List<String> clientErrorMessages = new ArrayList<>();
	private List<String> clientValidMessages = new ArrayList<>();

	public List<String> getClientErrorMessages() {
		return clientErrorMessages;
	}

	public void setClientErrorMessages(List<String> clientErrorMessages) {
		this.clientErrorMessages = clientErrorMessages;
	}

	public void addClientErrorMessages(String errorMessage) {
		this.clientErrorMessages.add(errorMessage);
	}

	public void cleanClientErrorMessages() {
		this.sheetNumber = 0;
		this.haveError = false;
		this.clientErrorMessages.clear();
	}

	public String formatClientErrorMessages() {
		String errorMessage = this.clientErrorMessages.toString();
		cleanClientErrorMessages();
		return errorMessage.substring(1, errorMessage.length() - 1);
//				return errorMessage.substring(1, errorMessage.length() - 1).replace(",", "\n");
	}

	public List<String> getClientValidMessages() {
		return clientValidMessages;
	}

	public void setClientValidMessages(List<String> clientValidMessages) {
		this.clientValidMessages = clientValidMessages;
	}

	public void addClientValidMessages(String clientValidMessage) {
		this.clientValidMessages.add(clientValidMessage);
	}

	public void cleanClientValidMessages() {
		this.sheetNumber = 0;
		this.haveError = false;
		this.clientValidMessages.clear();
	}

	public String formatClientValidMessages() {
		String validMessage = this.clientValidMessages.toString();
		cleanClientValidMessages();
		return validMessage.substring(1, validMessage.length() - 1);
//				return validMessage.substring(1, validMessage.length() - 1).replace(",", "\n");
	}

	public com.appforfiles.model.File fileModel = new com.appforfiles.model.File();

	/**
	 * 
	 * @param fromRow
	 * @param toRow
	 * @param sheet
	 * @return StringBuilder, csv content
	 */
	public StringBuilder excelToCsv(XSSFSheet sheet) {

		StringBuilder csvStringBuilder = new StringBuilder();
		Row row;
		int i = fromRow;

		try {

			for (; i < toRow; i++) {

				row = sheet.getRow(i);
				int lastColumn = row.getLastCellNum();

				for (int cNum = 0; cNum < lastColumn; cNum++) {

					Cell cell = row.getCell(cNum);

					if (cell == null) {
						csvStringBuilder.append("" + SEPARATOR);

					} else {

						switch (cell.getCellType()) {
						case BOOLEAN:
							csvStringBuilder.append(cell.getBooleanCellValue() + SEPARATOR);
							break;
						case NUMERIC:
							csvStringBuilder.append(cell.getNumericCellValue() + SEPARATOR);
							break;
						case STRING:
							csvStringBuilder.append(cell.getStringCellValue() + SEPARATOR);
							break;
						case BLANK:
							csvStringBuilder.append("" + SEPARATOR);
							break;
						case _NONE:
							csvStringBuilder.append("" + SEPARATOR);
							break;
						case ERROR:
							csvStringBuilder.append("" + SEPARATOR);
							break;
						default:
							csvStringBuilder.append(cell.toString() + SEPARATOR);
						}// switch
					}// else
				}// for 2

				csvStringBuilder.delete(csvStringBuilder.lastIndexOf(SEPARATOR), csvStringBuilder.length());
				csvStringBuilder.append(END_OF_RECORD);
				// csvStringBuilder.append("\n");
			}// for 1

		} catch (Exception ioe) {
			ioe.printStackTrace();
		}
		return csvStringBuilder;
	}

	/**
	 * 
	 * @param sheetXssf
	 * @return int, number of first row with empty cell on first column
	 */
	public int rowNumberOfFirstEmptyCell(XSSFSheet sheetXssf) {
		int i = 0;
		int lastRow = sheetXssf.getLastRowNum();

		for (i = 0; i <= lastRow; i++) {

			if (sheetXssf.getRow(i).getCell(0) == null || sheetXssf.getRow(i).getCell(0).getCellType() == CellType.BLANK || sheetXssf.getRow(i).getCell(0).toString().length() == 0) {
				toRow = i;
				return toRow;
			}
		}
		toRow = i;
		return toRow;
	}

	/**
	 * 
	 * @param storedXlsxFilePath
	 * @param sheetNumber
	 * @return
	 */
	public XSSFSheet getSheet(String storedXlsxFilePath, int sheetNumber) {

		FileInputStream excelFile = null;
		XSSFWorkbook workbook = null;
		XSSFSheet sheet = null;
		haveError = false;

		try {
			excelFile = new FileInputStream(new File(storedXlsxFilePath));
			workbook = new XSSFWorkbook(excelFile);

			sheet = workbook.cloneSheet(sheetNumber);

			excelFile.close();
			workbook.close();

		} catch (FileNotFoundException e) {
			haveError = true;
			addClientErrorMessages("FileNotFound, " + e.toString());
		} catch (IOException e) {
			haveError = true;
			addClientErrorMessages("IOException, " + e.toString());
		} catch (IllegalArgumentException e) {
			haveError = true;
			addClientErrorMessages("IllegalArgumentException, " + e.toString());
		} catch (Exception e) {
			haveError = true;
			addClientErrorMessages("Exception, " + e.toString());
		}

		if (sheet == null) {
			addClientErrorMessages("No sheet");
			haveError = true;
		}

		if (haveError) {
			return null;
		}

		return sheet;
	}

	/**
	 * 
	 * @param sheetXssf
	 * @return
	 */
	public XSSFSheet checkFile(String storedXlsxFilePath) {

		FileInputStream excelFile = null;
		XSSFWorkbook workbook = null;
		XSSFSheet sheet = null;
		haveError = false;

		try {
			excelFile = new FileInputStream(new File(storedXlsxFilePath));
			workbook = new XSSFWorkbook(excelFile);

			sheet = workbook.getSheetAt(sheetNumber);

			excelFile.close();
			workbook.close();
		} catch (FileNotFoundException e) {
			haveError = true;
			addClientErrorMessages("FileNotFound, " + e.toString());
		} catch (IOException e) {
			haveError = true;
			addClientErrorMessages("IOException, " + e.toString());
		} catch (IllegalArgumentException e) {
			haveError = true;
			addClientErrorMessages("IllegalArgumentException, " + e.toString());
		} catch (Exception e) {
			haveError = true;
			addClientErrorMessages("Exception, " + e.toString());
		}

		if (haveError) {
			return null;
		}

		if (sheet == null) {
			addClientErrorMessages("No sheet at position " + sheetNumber);
			return null;
		}

		if (workbook.getNumberOfSheets() > 1) {
			addClientErrorMessages("There are more than one sheet!");
			return null;
		}

		int rowNumberOfFirstEmptyCell = rowNumberOfFirstEmptyCell(sheet);

// is it frirst row(header) empty
		if (rowNumberOfFirstEmptyCell == 0) {
			addClientErrorMessages("There is no header in file!");
			haveError = true;
		}
// is it second row empty
		if (rowNumberOfFirstEmptyCell == 1) {
			addClientErrorMessages("First row for data is empty!");
			haveError = true;
		}

// does file contains SEPARATOR or END_OF_RECORD
		Row row;
		String checkSeparator;
		int i = fromRow;

		for (; i < toRow; i++) {

			row = sheet.getRow(i);
			int lastColumn = row.getLastCellNum();

			for (int cNum = 0; cNum < lastColumn; cNum++) {

				Cell cell = row.getCell(cNum);

				if (cell != null) {

					switch (cell.getCellType()) {
					case STRING:
						checkSeparator = cell.getStringCellValue().toLowerCase();

						if (checkSeparator.contains(SEPARATOR)) {
							haveError = true;
							addClientErrorMessages("File contains: " + SEPARATOR + ", row: " + row.getRowNum() + ", column: " + cell.getColumnIndex());
						}
						if (checkSeparator.contains(END_OF_RECORD)) {
							haveError = true;
							addClientErrorMessages("File contains: " + END_OF_RECORD + ", row: " + row.getRowNum() + ", column: " + cell.getColumnIndex());
						}
						break;
					default:
						try {
							checkSeparator = cell.getStringCellValue().toLowerCase();

							if (checkSeparator.contains(SEPARATOR)) {
								haveError = true;
								addClientErrorMessages("File contains: " + SEPARATOR + ", row: " + row.getRowNum() + ", column: " + cell.getColumnIndex());
							}
							if (checkSeparator.contains(END_OF_RECORD)) {
								haveError = true;
								addClientErrorMessages("File contains: " + END_OF_RECORD + ", row: " + row.getRowNum() + ", column: " + cell.getColumnIndex());
							}
						} catch (Exception e) {

						}
						break;
					}// switch
				}// if
			}// for 2
		}// for 1

		if (haveError) {
			return null;
		}

		addClientValidMessages("The data in file are valid!");
		return sheet;
	}

	/**
	 * @param
	 * @return
	 */
	public boolean checkFileBeforeUpload(MultipartFile file) {
		boolean haveError = false;

// 1. is file empty
		if (file.isEmpty()) {
			addClientErrorMessages("File is empty!");
			fileModel = null;
			haveError = true;
			return false;
		}

// 2. checking extension
		String fileName = org.apache.commons.io.FilenameUtils.getName(file.getOriginalFilename()).toString();
		String extension = fileName.substring(fileName.lastIndexOf(".") + 1);

		if (!extension.toLowerCase().equals("xlsx")) {
			haveError = true;
			addClientErrorMessages("File is not allowed extension, " + extension);
		}

// 3. file name length
		if (fileName.length() > MAX_FILE_NAME_LENGTH) {
			haveError = true;
			addClientErrorMessages("File name length is more then allowed, " + String.valueOf(fileName.length()));
		}

// 4. allowed file name chars
		String fileNameWithoutExtension = fileName.replace("." + extension, "");
		String regex = "^[a-zA-Z0-9-_\\s]+$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(fileNameWithoutExtension);

		if (!matcher.matches()) {
			haveError = true;
			addClientErrorMessages("File name contain not allowed characters, " + fileName);
		}

		if (haveError) {
			addClientErrorMessages(getClientErrorMessages().toString());
			return false;
		}

		addClientValidMessages("File is valid!");
		return true;
	}

}
