package com.example.app.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.app.domain.DocumentGroup;

@Mapper
public interface DocumentGroupDao {

	List<DocumentGroup> selectAll() throws Exception;
	
}
