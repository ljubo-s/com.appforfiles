package com.appforfiles.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "app_role")
@Data
public class Role implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "app_role_sequence")
	@Column(name = "id", insertable = false, updatable = false, nullable = false)
	@SequenceGenerator(name = "app_role_sequence", sequenceName = "APP_ROLE_SEQUENCE", allocationSize = 1)
	private Integer id;

	@Column(name = "role_name")
	private String name;

}
