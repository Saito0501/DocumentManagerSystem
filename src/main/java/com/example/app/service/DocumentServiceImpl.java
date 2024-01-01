package com.example.app.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.app.dao.AccessDao;
import com.example.app.dao.DocumentDao;
import com.example.app.dao.RemindDao;
import com.example.app.domain.Access;
import com.example.app.domain.Document;
import com.example.app.domain.DocumentForm;
import com.example.app.domain.Member;
import com.example.app.domain.Remind;
import com.example.app.login.LoginStatus;

import jakarta.servlet.http.HttpSession;

@Service
public class DocumentServiceImpl implements DocumentService {

	@Autowired
	DocumentDao documentDao;
	@Autowired
	RemindDao remindDao;
	@Autowired
	AccessDao accessDao;

	@Override
	public List<Document> getDocumentListByPage(
			int memberId, String fileName, int page, int numPerPage) throws Exception {
		int offset = numPerPage * (page - 1);
		List<Document> DocumentList;
		if (fileName.equals("")) {
			DocumentList = documentDao.selectLimitedByMemberId(memberId, offset, numPerPage);
		}else {
			DocumentList = documentDao.searchByFileName(memberId, fileName, offset, numPerPage);
		}
		return DocumentList;
	}

	@Override
	public int getTotalPages(int memberId, String fileName, int numPerPage) throws Exception {
		double totalNum;
		if (fileName.equals("")) {
			totalNum = (double) documentDao.countSelect(memberId);
		}else {
			totalNum = (double) documentDao.countSearch(memberId,fileName);
		}
		return (int) Math.ceil(totalNum / numPerPage);
	}

	@Override
	@Transactional
	public void addDocument(DocumentForm formData, HttpSession session) throws Exception {
		LoginStatus status = (LoginStatus) session.getAttribute("loginStatus");	
		MultipartFile upfile = formData.getUpfile();
		String fileName = upfile.getOriginalFilename();
		
		//documentsテーブル登録
		Document document = new Document();
		document.setFileName(fileName);
		document.setDescription(formData.getDescription());
		Member createdMember = new Member();
		createdMember.setId(status.getId());
		document.setCreatedMember(createdMember);
		Member updatedMember = new Member();
		updatedMember.setId(status.getId());
		document.setUpdatedMember(updatedMember);
		documentDao.insert(document);	//登録処理＋idの設定
		//ドキュメントの保存
		Path path = Paths.get("uploads/"+document.getId());
		Files.createDirectory(path);
		Path upfilePath = Paths.get("uploads/"+document.getId()+"/"+document.getFileName());
		upfile.transferTo(upfilePath);
		
		//accessesテーブル登録
		Access access = new Access();
		access.setDocumentId(document.getId());
		for (Integer memberId  : formData.getAccessMemberIdList()) {
			access.setMemberId(memberId);
			accessDao.insert(access);
		}
		
		if (formData.getRemindMemberIdList().size()>0) {
			//remindsテーブル登録
			Remind remind = new Remind();
			remind.setDocumentId(document.getId());
			remind.setMemberId(status.getId());
			remind.setStartDate(formData.getRemindStartDate());
			remind.setEndDate(formData.getRemindEndDate());
			remind.setDescription(formData.getRemindDescription());
			for (Integer targetId : formData.getRemindMemberIdList()) {
				remind.setTargetId(targetId);
				remindDao.insert(remind);
			}			
		}
	}

}
