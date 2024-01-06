package com.example.app.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.app.domain.Access;

@Mapper
public interface AccessDao {

	void insert(Access access) throws Exception;
	
	List<Access> selectByDocumentId(Integer documentId) throws Exception;
	
	void deleteByDocumentId(Integer documentId) throws Exception;
}
