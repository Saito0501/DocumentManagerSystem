package com.example.app.service;

import java.util.List;

import com.example.app.domain.Member;

public interface MemberService {

	List<Member> getMemberList() throws Exception;
	
	Member getMemberByLoginId(String logingId) throws Exception;
}
