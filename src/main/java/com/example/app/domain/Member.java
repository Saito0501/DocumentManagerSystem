package com.example.app.domain;

import lombok.Data;

@Data
public class Member {

	private Integer id;
	private String memberNumber;
	private String name;
	private Division division;
	private String loginId;
	private String loginPass;
	
}
