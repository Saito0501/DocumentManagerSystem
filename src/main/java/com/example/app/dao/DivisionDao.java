package com.example.app.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.app.domain.Division;

@Mapper
public interface DivisionDao {

	List<Division> selectAll() throws Exception;
	
}
