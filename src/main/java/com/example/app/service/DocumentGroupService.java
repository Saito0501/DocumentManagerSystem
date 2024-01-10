package com.example.app.service;

import java.util.List;

import com.example.app.domain.DocumentGroup;

public interface DocumentGroupService {

	List<DocumentGroup> getDocumentGroupList() throws Exception;
	
}
