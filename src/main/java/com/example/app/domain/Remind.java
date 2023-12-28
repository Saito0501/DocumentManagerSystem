package com.example.app.domain;

import java.util.Date;

import lombok.Data;

@Data
public class Remind {

	private Integer id;
	private Integer documentId;
	private Integer memberId;
	private String memberName;
	private Integer targetId;
	private Date startDate;
	private Date endDate;
	private String description;
	private Date created;
	
}
