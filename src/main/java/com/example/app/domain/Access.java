package com.example.app.domain;

import java.util.Date;

import lombok.Data;

@Data
public class Access {

	private Integer id;
	private Integer documentId;
	private Integer memberId;
	private Date created;
	
}
