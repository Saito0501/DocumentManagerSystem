package com.example.app.dao;

import org.apache.ibatis.annotations.Mapper;

import com.example.app.domain.Access;

@Mapper
public interface AccessDao {

	void insert(Access access) throws Exception;
	
}
