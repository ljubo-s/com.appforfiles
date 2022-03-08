package com.appforfiles.controller;

import java.io.FileOutputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.multipart.MultipartFile;

import com.appforfiles.model.Log;
import com.appforfiles.service.FileHandlerService;
import com.appforfiles.service.FileService;
import com.appforfiles.service.LogService;
import com.appforfiles.service.StoredProcedureDb2Service;
import com.appforfiles.service.StoredProcedureService;
import com.appforfiles.sftp.FileTransferService;
import com.appforfiles.storage.FilesInfo;
import com.appforfiles.storage.StorageProperties;
import com.appforfiles.storage.StorageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class AjaxContoller {

	Logger logger = LoggerFactory.getLogger(AjaxContoller.class);

	private final FileHandlerService fileHandlerService;
	private final StorageService storageService;
	private final FileService fileService;
	private final StoredProcedureService storedProcedureService;
	private final StoredProcedureDb2Service storedProcedureDb2Service;
	private final FileTransferService fileTransferService;
	private final StorageProperties storageProperties;
	private final LogService logService;

	private com.appforfiles.model.File fileModel;

	@PostMapping("/upload-file-ajax")
	@ResponseBody
	public Message fileUploadAjax(@RequestParam("file") MultipartFile file, @RequestParam("type") String type, @RequestParam(value = "page") String page) {

		Message message = new Message();
		fileModel = new com.appforfiles.model.File();

		if (file.isEmpty()) {

			message.setStatus(1);
			message.setFunctionName("upload-file-ajax");
			message.addMessage("There is no file!");
			return message;
		}

		fileModel.setStartDownload(new Timestamp(System.currentTimeMillis()));
		String xlsxClientsFileName = org.apache.commons.io.FilenameUtils.getName(file.getOriginalFilename()).toString();
		fileModel.setXlsxClientsName(xlsxClientsFileName);

		if (!fileHandlerService.checkFileBeforeUpload(file)) {

			message.setStatus(1);
			message.setFunctionName("upload-file-ajax");
			message.addMessage(fileHandlerService.formatClientErrorMessages());
			return message;
		}

		String storedOriginalXlsxFilePath = "";
		String storageFilePath = "";

		if (type.equals(FilesInfo.INVOICE.getValue())) {

			fileModel.setFileType(FilesInfo.INVOICE.getValue());
			storedOriginalXlsxFilePath = storageProperties.getInvoiceOrginalXlsxLocation();
			storageFilePath = storageProperties.getInvoiceXlsxLocation();

		} else if (type.equals(FilesInfo.STORNO.getValue())) {

			fileModel.setFileType(FilesInfo.STORNO.getValue());
			storedOriginalXlsxFilePath = storageProperties.getStornoOrginalXlsxLocation();
			storageFilePath = storageProperties.getStornoXlsxLocation();

		} else {

			fileHandlerService.addClientErrorMessages("Error on type!");
			message.setStatus(1);
			message.setFunctionName("upload-file-ajax");
			message.addMessage("There is no file!");
			message.addMessage(fileHandlerService.formatClientErrorMessages());
			return message;
		}

		String sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
		Log logEntity = logService.getLogBySessionId(sessionId);

		fileModel.setLog(logEntity);
		fileModel.setLog_id(logEntity.getId());
		fileService.saveOrUpdate(fileModel);

		String newName = xlsxClientsFileName.toLowerCase();
		newName = newName.replace(".xlsx", "_" + fileModel.getId() + "_" + System.currentTimeMillis() + ".xlsx");

		fileModel.setXlsxStorageName(newName);
		fileModel.setXlsxOriginalPath(storedOriginalXlsxFilePath);

		// save orginal file
		storageService.store(file, storedOriginalXlsxFilePath + newName, type);

		fileModel.setEndDownload(new Timestamp(System.currentTimeMillis()));
		fileService.saveOrUpdate(fileModel);

		// copy original file, to get file for check and extract data
		storageService.copyFile(fileModel.getXlsxOriginalPath() + fileModel.getXlsxStorageName(), storageFilePath + newName);

		fileModel.setXlsxStoragePath(storageFilePath);
		fileService.saveOrUpdate(fileModel);

		fileHandlerService.addClientValidMessages("You successfully uploaded file!");

		message.setStatus(0);
		message.setFunctionName("upload-file-ajax");
		message.addMessage(fileHandlerService.formatClientValidMessages());

		return message;
	}


	@PostMapping("/check-file-ajax")
	@ResponseBody
	public Message checkFileAjax(@RequestParam(value = "type") String type, @RequestParam(value = "page") String page) {

		Message message = new Message();
		if (fileModel.getXlsxClientsName() == null) {

			message.setStatus(1);
			message.setFunctionName("check-file-ajax");
			message.addMessage("There is no uploaded file!");
			message.addMessage(fileHandlerService.formatClientErrorMessages());

			return message;
		}
		if (fileModel.getFileType().equals(FilesInfo.INVOICE.getValue())) {

		} else if (fileModel.getFileType().equals(FilesInfo.STORNO.getValue())) {

		} else {

		}

		String storedXlsxFilePath = fileModel.getXlsxStoragePath() + fileModel.getXlsxStorageName();
		XSSFSheet sheet = null;

		sheet = fileHandlerService.checkFile(storedXlsxFilePath);

		if (sheet != null) {

		} else {
			message.setStatus(1);
			message.setFunctionName("upload-file-ajax");
			message.addMessage(fileHandlerService.formatClientErrorMessages());

			return message;
		}
		message.setStatus(0);
		message.setFunctionName("upload-file-ajax");
		message.addMessage(fileHandlerService.formatClientValidMessages());

		return message;
	}

	@PostMapping("/convert-to-csv-ajax")
	@ResponseBody
	public Message convertToCsvAjax(@RequestParam(value = "type") String type, @RequestParam(value = "page") String page) {

		Message message = new Message();
		if (fileModel.getXlsxStorageName() != null) {

		} else {
			fileHandlerService.addClientErrorMessages("There is no file to be converted!");
			message.setStatus(1);
			message.setFunctionName("convert-to-csv-ajax");
			message.addMessage(fileHandlerService.formatClientErrorMessages());

			return message;
		}

		String csvName = fileModel.getXlsxStorageName().replace(".xlsx", ".csv");
		String storageLocationCsv = storageProperties.getInvoiceCsvLocation();
		String storedXlsxFilePath = fileModel.getXlsxStoragePath() + fileModel.getXlsxStorageName();

		if (fileModel.getFileType().equals(FilesInfo.STORNO.getValue())) {
			storageLocationCsv = storageProperties.getStornoCsvLocation();
		}

		fileService.saveOrUpdate(fileModel);

		try {
			XSSFSheet sheet = null;
			sheet = fileHandlerService.checkFile(storedXlsxFilePath);

			if (sheet == null) {
				message.setStatus(1);
				message.setFunctionName("convert-to-csv-ajax");
				message.addMessage(fileHandlerService.formatClientErrorMessages());

				return message;
			}

			StringBuilder stringCSV = fileHandlerService.excelToCsv(sheet);

			FileOutputStream fos = new FileOutputStream(storageLocationCsv + csvName);
			fos.write(stringCSV.toString().getBytes());
			fos.close();

			fileModel.setCsvName(csvName);
			fileModel.setCsvPath(storageLocationCsv);
			fileService.saveOrUpdate(fileModel);

		} catch (Exception e) {

			fileHandlerService.addClientErrorMessages("Choose file !");
			message.setStatus(1);
			message.setFunctionName("convert-to-csv-ajax");
			message.addMessage(fileHandlerService.formatClientErrorMessages());

			return message;
		}

		fileHandlerService.addClientValidMessages("Successfully converted to csv!");
		message.setStatus(0);
		message.setFunctionName("convert-to-csv-ajax");
		message.addMessage(fileHandlerService.formatClientValidMessages());

		return message;
	}

	@PostMapping("/send-to-server-ajax")
	@ResponseBody
	public Message sendFilesToDbServerAjax(@RequestParam(value = "type") String type, @RequestParam(value = "page") String page) {

		Message message = new Message();
		if (fileModel.getXlsxStorageName() != null && fileModel.getCsvName() != null) {

		} else {
			fileHandlerService.addClientErrorMessages("There is no file to be sended!");

			message.setStatus(1);
			message.setFunctionName("send-to-server-ajax");
			message.addMessage(fileHandlerService.formatClientErrorMessages());

			return message;
		}

		String storageLocationCsvFile = storageProperties.getInvoiceCsvLocation() + fileModel.getCsvName();
		String storageLocationXlsxFile = storageProperties.getInvoiceXlsxLocation() + fileModel.getXlsxStorageName();

		String dbServerLocationXlsx = storageProperties.getDbInvoicePath() + fileModel.getXlsxStorageName();
		String dbServerLocationCsv = storageProperties.getDbInvoicePath() + fileModel.getCsvName();

		if (type.equals(FilesInfo.INVOICE.getValue())) {
			storageLocationCsvFile = storageProperties.getInvoiceCsvLocation() + fileModel.getCsvName();
			storageLocationXlsxFile = storageProperties.getInvoiceXlsxLocation() + fileModel.getXlsxStorageName();

			dbServerLocationXlsx = storageProperties.getDbInvoicePath() + fileModel.getXlsxStorageName();
			dbServerLocationCsv = storageProperties.getDbInvoicePath() + fileModel.getCsvName();
		} else if (type.equals(FilesInfo.STORNO.getValue())) {
			storageLocationCsvFile = storageProperties.getStornoCsvLocation() + fileModel.getCsvName();
			storageLocationXlsxFile = storageProperties.getStornoXlsxLocation() + fileModel.getXlsxStorageName();

			dbServerLocationXlsx = storageProperties.getDbStornoPath() + fileModel.getXlsxStorageName();
			dbServerLocationCsv = storageProperties.getDbStornoPath() + fileModel.getCsvName();
		} else {

			message.setStatus(1);
			message.setFunctionName("send-to-server-ajax");
			message.addMessage("Error while try to send file to Server!");
			message.addMessage(fileHandlerService.formatClientErrorMessages());

			return message;
		}

		// SFTP csv
		boolean isUploadedCsv = fileTransferService.uploadFile(storageLocationCsvFile, dbServerLocationCsv);
		if (isUploadedCsv) {
			fileHandlerService.addClientValidMessages("You successfully send CSV file - " + fileModel.getCsvName() + " to Server!");
		} else {
			fileHandlerService.addClientErrorMessages("Error while try to send CSV file " + fileModel.getCsvName() + " to Server!");
		}

		// SFTP xlsx
		boolean isUploadedXlsx = fileTransferService.uploadFile(storageLocationXlsxFile, dbServerLocationXlsx);
		if (isUploadedXlsx) {
			fileHandlerService.addClientValidMessages("<br> You successfully send XLSX file to - " + fileModel.getXlsxStorageName() + " to Server!");
		} else {
			fileHandlerService.addClientErrorMessages("<br> Error while try to send XLSX file to - " + fileModel.getXlsxStorageName() + " to Server! \n");
		}

		if (isUploadedCsv && isUploadedXlsx) {
			message.setStatus(0);
			message.setFunctionName("send-to-server-ajax");
			message.addMessage(fileHandlerService.formatClientValidMessages());

			return message;
		} else {
			message.setStatus(1);
			message.setFunctionName("send-to-server-ajax");
			message.addMessage(fileHandlerService.formatClientErrorMessages());

			return message;
		}
	}

	@PostMapping("/call-first-stored-procedure-ajax")
	@ResponseBody
	public Message callStoredProcedureAjax(@RequestParam(value = "type") String type, @RequestParam(value = "page") String page) {

		Map<String, String> mappings = new HashMap<String, String>();
		Message message = new Message();

		String json = "";
		String result = "";

		if (type.equals(FilesInfo.INVOICE.getValue())) {
			mappings = storedProcedureService.importInvoiceStoredProcedure(fileModel.getCsvName());
			result = mappings.toString();

			try {
				json = new ObjectMapper().writeValueAsString(mappings);
				result = json;
			} catch (JsonProcessingException e) {
				e.printStackTrace();
//				add to exception log
			}
		}

		if (type.equals(FilesInfo.STORNO.getValue())) {

			mappings = storedProcedureDb2Service.importStornoStoredProcedure(fileModel.getCsvName());
			result = mappings.toString();

			try {
				json = new ObjectMapper().writeValueAsString(mappings);
				result = json;
			} catch (JsonProcessingException e) {
				e.printStackTrace();
//				add to exception log
			}
		}

		ObjectMapper mapper = new ObjectMapper();
		JsonNode jsonObj = null;
		JsonNode jsonNode = null;

		try {
			jsonObj = mapper.readTree(json);
			jsonNode = jsonObj.get("status");
			String statusString = jsonNode.toString();

			if (statusString.equals("\"1\"")) {
				fileHandlerService.addClientValidMessages("Import succesfull!");
				fileHandlerService.addClientValidMessages(result);

				message.setStatus(0);
				message.setFunctionName("call-first-stored-procedure-ajax");
				message.addMessage(fileHandlerService.formatClientValidMessages());

				return message;

			} else {
				fileHandlerService.addClientErrorMessages("Something went wrong!");
				fileHandlerService.addClientErrorMessages(result);

				message.setStatus(1);
				message.setFunctionName("call-first-stored-procedure-ajax");
				message.addMessage(fileHandlerService.formatClientErrorMessages());

				return message;
			}

		} catch (JsonMappingException e) {
			e.printStackTrace();
			fileHandlerService.addClientErrorMessages("Something went wrong(JsonMappingException)!");
			fileHandlerService.addClientErrorMessages(result);

			message.setStatus(1);
			message.setFunctionName("call-first-stored-procedure-ajax");
			message.addMessage(fileHandlerService.formatClientErrorMessages());

			return message;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			fileHandlerService.addClientErrorMessages("Something went wrong(JsonProcessingException)!");

			message.setStatus(1);
			message.setFunctionName("call-first-stored-procedure-ajax");
			message.addMessage(fileHandlerService.formatClientErrorMessages());

			return message;
		}
	}

	class Message {

		private int status;
		private String functionName;
		private List<String> message = new ArrayList<>();

		public int getStatus() {
			return status;
		}

		public void setStatus(int status) {
			this.status = status;
		}

		public String getFunctionName() {
			return functionName;
		}

		public void setFunctionName(String functionName) {
			this.functionName = functionName;
		}

		public List<String> getMessage() {
			return message;
		}

		public void setMessage(List<String> message) {
			this.message = message;
		}

		public void addMessage(String newMessage) {
			this.message.add(newMessage);
		}

		public void clearMessage() {
			this.message.clear();
		}

	}

}
