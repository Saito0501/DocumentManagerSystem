package com.example.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.app.dao.DocumentDao;
import com.example.app.domain.Document;

@Service
public class DocumentServiceImpl implements DocumentService {

	@Autowired
	DocumentDao dao;

	@Override
	public List<Document> getDocumentListByPage(
			int memberId, String fileName, int page, int numPerPage) throws Exception {
		int offset = numPerPage * (page - 1);
		List<Document> DocumentList;
		if (fileName.equals("")) {
			DocumentList = dao.selectLimitedByMemberId(memberId, offset, numPerPage);
		}else {
			DocumentList = dao.searchByFileName(memberId, fileName, offset, numPerPage);
		}
		return DocumentList;
	}

	@Override
	public int getTotalPages(int memberId, String fileName, int numPerPage) throws Exception {
		double totalNum;
		if (fileName.equals("")) {
			totalNum = (double) dao.countSelect(memberId);
		}else {
			totalNum = (double) dao.countSearch(memberId,fileName);
		}
		return (int) Math.ceil(totalNum / numPerPage);
	}

}
