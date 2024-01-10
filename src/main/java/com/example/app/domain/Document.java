package com.example.app.domain;

import java.util.Date;

import lombok.Data;

@Data
public class Document {

	private Integer id; 
	private String fileName;
	private String description;
	private Integer groupId;
	private Member createdMember;
	private Date created;
	private Member updatedMember;
	private Date updated;
	
}
