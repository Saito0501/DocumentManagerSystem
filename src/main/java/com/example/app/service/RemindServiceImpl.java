package com.example.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.app.dao.MemberDao;
import com.example.app.dao.RemindDao;
import com.example.app.domain.Remind;

@Service
public class RemindServiceImpl implements RemindService {

	@Autowired
	RemindDao remindDao;
	@Autowired
	MemberDao memberDao;
	
	@Override
	public List<Remind> getRemindByTargetId(Integer targetId) throws Exception {
		return remindDao.selectByTargetId(targetId);
	}

	@Override
	public void deleteRemind(Integer id) throws Exception {
		remindDao.delete(id);
	}

}
