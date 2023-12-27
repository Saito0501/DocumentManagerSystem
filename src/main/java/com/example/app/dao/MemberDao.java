package com.example.app.dao;

import org.apache.ibatis.annotations.Mapper;

import com.example.app.domain.Member;

@Mapper
public interface MemberDao {

	Member selectByLoginId(String loginId) throws Exception;
}
