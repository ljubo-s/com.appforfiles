package com.appforfiles.sftp;

public interface FileTransferService {

	boolean uploadFile(String localFilePath, String remoteFilePath);

	boolean downloadFile(String localFilePath, String remoteFilePath);

	boolean uploadFileWithPassword(String localFilePath, String remoteFilePath);

}
