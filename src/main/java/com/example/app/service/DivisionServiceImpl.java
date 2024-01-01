package com.example.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.app.dao.DivisionDao;
import com.example.app.domain.Division;

@Service
public class DivisionServiceImpl implements DivisionService {

	@Autowired
	DivisionDao dao;
	
	@Override
	public List<Division> getDivisionList() throws Exception {
		return dao.selectAll();
	}

}
