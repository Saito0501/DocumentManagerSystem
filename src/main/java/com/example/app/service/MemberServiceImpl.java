package com.example.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.app.dao.MemberDao;
import com.example.app.domain.Member;

@Service
public class MemberServiceImpl implements MemberService {

	@Autowired
	MemberDao dao;
	
	@Override
	public List<Member> getMemberList() throws Exception {
		return dao.selectAll();
	}
	
	@Override
	public Member getMemberByLoginId(String logingId) throws Exception {
		return dao.selectByLoginId(logingId);
	}

}
