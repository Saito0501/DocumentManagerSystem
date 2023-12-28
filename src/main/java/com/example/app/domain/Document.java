package com.example.app.domain;

import java.util.Date;

import lombok.Data;

@Data
public class Document {

	private Integer id; 
	private String fileName;
	private String description;
	private String createdName;
	private Date created;
	private String updatedName;
	private Date updated;
	
}
