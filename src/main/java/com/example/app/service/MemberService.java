package com.example.app.service;

import com.example.app.domain.Member;

public interface MemberService {

	Member getMemberByLoginId(String logingId) throws Exception;
}
