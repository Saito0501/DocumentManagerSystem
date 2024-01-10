package com.example.app.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.app.domain.Document;

@Mapper
public interface DocumentDao {
	
	List<Document> selectAll(int memberId) throws Exception;

	List<Document> selectLimitedByMemberId(
			@Param("memberId") int memberId,
			@Param("offset") int offset,@Param("num") int num) throws Exception;
	Long countSelect(int memberId) throws Exception;
	
	List<Document> searchByFileName(
			@Param("memberId") int memberId,@Param("fileName") String fileName,
			@Param("offset") int offset,@Param("num") int num) throws Exception;
	Long countSearch(@Param("memberId") int memberId,@Param("fileName") String fileName) throws Exception;
	
	void insert(Document document) throws Exception;
	
	Document selectById(Integer id) throws Exception;
	
	void update(Document document) throws Exception;
	
	void delete(Integer id) throws Exception;
}
