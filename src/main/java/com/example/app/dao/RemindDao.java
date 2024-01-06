package com.example.app.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.app.domain.Remind;

@Mapper
public interface RemindDao {

	List<Remind> selectByTargetId(Integer targetId) throws Exception;
	
	void delete(Integer id) throws Exception;
	
	void insert(Remind remind) throws Exception;

	List<Remind> selectByDocumentId(Integer documentId) throws Exception;
	
	void deleteByDocumentId(Integer documentId) throws Exception;
}
