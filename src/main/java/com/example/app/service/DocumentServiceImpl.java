package com.example.app.service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.tomcat.util.http.fileupload.FileUtils;
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
	public List<Document> getDocumentList(int memberId) throws Exception {
		return documentDao.selectAll(memberId);
	}
	
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
		document.setGroupId(formData.getGroupId());
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

	@Override
	public DocumentForm getDocumentById(Integer id) throws Exception {
		Document document = documentDao.selectById(id);
		List<Access> accessList = accessDao.selectByDocumentId(id);
		List<Remind> remindList = remindDao.selectByDocumentId(id);
		//フォームデータの設定
		DocumentForm formData = new DocumentForm();
		formData.setDocumentId(document.getId());
		formData.setFileName(document.getFileName());
		formData.setDescription(document.getDescription());
		formData.setGroupId(document.getGroupId());
		formData.setAccessMemberIdList(
				accessList.stream().map(s -> s.getMemberId()).collect(Collectors.toList()));
		if (remindList.size()>0) {
			formData.setRemindMemberIdList(
					remindList.stream().map(s -> s.getTargetId()).collect(Collectors.toList()));
			formData.setRemindStartDate(remindList.get(0).getStartDate());
			formData.setRemindEndDate(remindList.get(0).getEndDate());
			formData.setRemindDescription(remindList.get(0).getDescription());	
		}
		return formData;
	}

	@Override
	@Transactional
	public void editDocument(DocumentForm formData, HttpSession session) throws Exception {
		LoginStatus status = (LoginStatus) session.getAttribute("loginStatus");	
		MultipartFile upfile = formData.getUpfile();
		boolean isUpfile = !upfile.isEmpty();	//アップロード有無
		String fileName ="";
		if (isUpfile) {
			//ファイルを更新する場合
			fileName = upfile.getOriginalFilename();
		} else {
			fileName = formData.getFileName();	
		}
		//documentsテーブル更新
		Document document = new Document();
		document.setId(formData.getDocumentId());
		document.setFileName(fileName);
		document.setDescription(formData.getDescription());
		document.setGroupId(formData.getGroupId());
		Member updatedMember = new Member();
		updatedMember.setId(status.getId());
		document.setUpdatedMember(updatedMember);
		documentDao.update(document);	//登録処理＋idの設定
		//ドキュメントの保存
		if (isUpfile) {
			File directory = new File("uploads/"+document.getId());
			FileUtils.cleanDirectory(directory);
			Path upfilePath = Paths.get("uploads/"+document.getId()+"/"+document.getFileName());
			upfile.transferTo(upfilePath);	
		}
		
		//accessesテーブル登録
		accessDao.deleteByDocumentId(document.getId());
		Access access = new Access();
		access.setDocumentId(document.getId());
		for (Integer memberId  : formData.getAccessMemberIdList()) {
			access.setMemberId(memberId);
			accessDao.insert(access);
		}
		
		remindDao.deleteByDocumentId(document.getId());
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

	@Override
	public void deleteDocument(Integer id) throws Exception {
		//remindsテーブル削除
		remindDao.deleteByDocumentId(id);
		//accessesテーブル削除
		accessDao.deleteByDocumentId(id);
		//documentsテーブル削除
		documentDao.delete(id);
	}

}
