package com.appforfiles.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "app_log")
@Getter
@Setter
public class Log implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "app_log_sequence")
	@Column(name = "id", insertable = false, updatable = false, nullable = false)
	@SequenceGenerator(name = "app_log_sequence", sequenceName = "APP_LOG_SEQUENCE", allocationSize = 1)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Getter
	@Setter
	@Column(name = "sid")
	private String sid;

	@Getter
	@Setter
	@DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
	@Column(name = "login_time")
	private Date loginTime;

	@Getter
	@Setter
	@DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
	@Column(name = "logout_time", nullable = true)
	private Date logoutTime;

	@Getter
	@Setter
	private Integer user_id;
	@Column(name = "user_id", nullable = false)

	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false, updatable = false, insertable = false)
	@JsonBackReference
	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public Set<File> fileSet = new HashSet<File>(0);
	
	@ElementCollection(targetClass = com.appforfiles.model.File.class)
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "log", fetch=FetchType.LAZY)
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@JsonIgnoreProperties("fileSet")
	@Column(name = "log_id")
//	@BatchSize(size=3)
	public Set<File> getFileSet() {
		return this.fileSet;
	}
	
	public void setFileSet(Set<File> fileSet) {
		this.fileSet = fileSet;
	}

}
