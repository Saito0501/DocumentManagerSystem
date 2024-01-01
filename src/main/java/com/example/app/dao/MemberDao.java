package com.example.app.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.app.domain.Member;

@Mapper
public interface MemberDao {

	List<Member> selectAll() throws Exception;
	
	Member selectByLoginId(String loginId) throws Exception;
	
	Member selectById(Integer id) throws Exception;
}
