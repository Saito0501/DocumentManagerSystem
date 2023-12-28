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
		//ログイン認証した社員を対象とするリマインド情報を全て取得する
		List<Remind> remidList = remindDao.selectByTargetId(targetId);
		for (Remind remind : remidList) {
			//登録者の氏名を取得する
			remind.setMemberName(memberDao.selectById(remind.getMemberId()).getName());
		}
		return remidList;
	}

	@Override
	public void deleteRemind(Integer id) throws Exception {
		remindDao.delete(id);
	}

}
