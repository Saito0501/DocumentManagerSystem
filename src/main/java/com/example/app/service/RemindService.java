package com.example.app.service;

import java.util.List;

import com.example.app.domain.Remind;

public interface RemindService {

	List<Remind> getRemindByTargetId(Integer targetId) throws Exception;
	
	void deleteRemind(Integer id) throws Exception;
}
