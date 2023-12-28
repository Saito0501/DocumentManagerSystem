package com.example.app.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.app.domain.Remind;

@Mapper
public interface RemindDao {

	List<Remind> selectByTargetId(Integer targetId) throws Exception;
	
	void delete(Integer id) throws Exception;
	
}
