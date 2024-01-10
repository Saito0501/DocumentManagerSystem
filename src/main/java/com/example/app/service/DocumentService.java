package com.example.app.service;

import java.util.List;

import com.example.app.domain.Document;
import com.example.app.domain.DocumentForm;

import jakarta.servlet.http.HttpSession;

public interface DocumentService {
	 
	 List<Document> getDocumentList(int memberId) throws Exception;
	
	 List<Document> getDocumentListByPage(
			 int memberId,String fileName,
			 int page, int numPerPage) throws Exception;
	 
	 int getTotalPages(int memberId,String fileName, int numPerPage) throws Exception;
	 
	 void addDocument(DocumentForm formData, HttpSession session) throws Exception;
	 
	 DocumentForm getDocumentById(Integer id) throws Exception;
	 
	 void editDocument(DocumentForm formData, HttpSession session) throws Exception;
	 
	 void deleteDocument(Integer id) throws Exception;
}
