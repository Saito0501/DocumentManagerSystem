package com.example.app.domain;

import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class DocumentForm {

	private MultipartFile upfile;
	private Integer documentId;	//ドキュメント編集用
	private String fileName;	//ドキュメント編集用
	private String description;
	private Integer groupId;
	private List<Integer> accessDivisionList;
	@Size(min=1)
	private List<Integer> accessMemberIdList;
	private List<Integer> remindDivisionList;
	private List<Integer> remindMemberIdList;
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date remindStartDate;
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date remindEndDate;
	private String remindDescription;
	
	//ファイルの形式(MIME タイプ)のチェック
	//対応ドキュメント「doc, docx, xls, xlsx, ppt, pptx,pdf,csv」
	public boolean isFileContentType(String type) {
		String regex = "officedocument|ms-excel|msword|ms-powerpoint|pdf|csv"; //正規表現「pdf|(または)csv」
		Pattern p = Pattern.compile(regex);
		 Matcher m1 = p.matcher(type);
		return m1.find();
	}
}
