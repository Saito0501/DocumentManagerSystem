package com.example.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.app.dao.DocumentGroupDao;
import com.example.app.domain.DocumentGroup;

@Service
public class DocumentGroupServiceImpl implements DocumentGroupService {

	@Autowired
	DocumentGroupDao dao;
	
	@Override
	public List<DocumentGroup> getDocumentGroupList() throws Exception {
		return dao.selectAll();
	}

}
