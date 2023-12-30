package com.example.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.app.login.LoginStatus;
import com.example.app.service.DocumentService;
import com.example.app.service.RemindService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/doc")
public class DocumentController {

	// 1 ページ当たりの表示件数
	private static final int NUM_PER_PAGE = 5;
	
	@Autowired
	DocumentService documentService;
	
	@Autowired
	RemindService remindService;
	
	@GetMapping("/list")
	public String list(
			@RequestParam(name="fileName", defaultValue = "") String fileName,
			@RequestParam(name="page", defaultValue = "1") Integer page,
			HttpSession session,
			Model model) throws Exception {
		LoginStatus status = (LoginStatus) session.getAttribute("loginStatus");	
		int RemindCount = remindService.getRemindByTargetId(status.getId()).size();
		model.addAttribute("documentList", documentService.getDocumentListByPage(status.getId(), fileName, page, NUM_PER_PAGE));
		model.addAttribute("totalPages", documentService.getTotalPages(status.getId(),fileName , NUM_PER_PAGE));
		model.addAttribute("page", page);
		model.addAttribute("fileName", fileName);
		model.addAttribute("RemindCount", RemindCount);
		return "list-document";
	}
	
	@GetMapping("/member/show")
	public String show(Model model,
			HttpSession session) throws Exception {
		LoginStatus status = (LoginStatus) session.getAttribute("loginStatus");	
		model.addAttribute("remindList", remindService.getRemindByTargetId(status.getId()));
		return "show-member";
	}
	
	@GetMapping("/member/show/{id}")
	public String delete(@PathVariable("id") Integer id, 
			RedirectAttributes redirectAttributes,
			Model model) throws Exception {
		remindService.deleteRemind(id);
		redirectAttributes.addFlashAttribute("message", "リマインドを更新しました。");
		return "redirect:/doc/member/show";
	}
}
