package com.appforfiles.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Data;

@Entity
@Table(name = "app_file")
@Data
public class File implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "app_file_sequence")
	@Column(name = "id", insertable = false, updatable = false, nullable = false)
	@SequenceGenerator(name = "app_file_sequence", sequenceName = "APP_FILE_SEQUENCE", allocationSize = 1)
	private Integer id;

	@Column(name = "xlsx_clients_name")
	private String xlsxClientsName;

	@Column(name = "xlsx_storage_name")
	private String xlsxStorageName;

	@Column(name = "csv_name")
	private String csvName;

	@Column(name = "xlsx_original_path")
	private String xlsxOriginalPath;

	@Column(name = "xlsx_storage_path")
	private String xlsxStoragePath;

	@Column(name = "csv_path")
	private String csvPath;

	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "blob_file_xlsx", nullable = true)
	private byte[] blobFileXlsx;

	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "blob_file_csv", nullable = true)
	private byte[] blobFileCsv;

	@DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
	@Column(name = "start_download")
	private Date startDownload;

	@DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
	@Column(name = "end_download")
	private Date endDownload;

	@DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
	@Column(name = "start_upload")
	private Date startUpload;

	@DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
	@Column(name = "end_upload")
	private Date endUpload;

	@DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
	@Column(name = "start_check")
	private Date startCheck;

	@DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
	@Column(name = "end_check")
	private Date endCheck;

	@DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
	@Column(name = "start_creating_csv")
	private Date startCreatingCsv;

	@DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
	@Column(name = "end_creating_csv")
	private Date endCreatingCsv;

	@Column(name = "log_id")
	private Integer log_id;

	@Column(name = "file_type")
	private String fileType;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "log_id", referencedColumnName = "id", nullable = false, updatable = false, insertable = false)
	@JsonBackReference
	private Log log;

}
