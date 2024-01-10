package com.example.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.app.domain.DocumentForm;
import com.example.app.login.LoginStatus;
import com.example.app.service.DivisionService;
import com.example.app.service.DocumentGroupService;
import com.example.app.service.DocumentService;
import com.example.app.service.MemberService;
import com.example.app.service.RemindService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/doc")
public class DocumentController {

	// 1 ページ当たりの表示件数
	private static final int NUM_PER_PAGE = 5;
	
	@Autowired
	MemberService memberService;
	@Autowired
	DivisionService divisionService;
	@Autowired
	DocumentService documentService;
	@Autowired
	DocumentGroupService documentGroupService;
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
		session.setAttribute("isListGroup", false);
		return "list-document";
	}
	
	@GetMapping("/listGroup")
	public String listGroup(
			HttpSession session,
			Model model) throws Exception {
		LoginStatus status = (LoginStatus) session.getAttribute("loginStatus");	
		int RemindCount = remindService.getRemindByTargetId(status.getId()).size();
		model.addAttribute("documentList", documentService.getDocumentList(status.getId()));
		model.addAttribute("groupList", documentGroupService.getDocumentGroupList());
		model.addAttribute("RemindCount", RemindCount);
		session.setAttribute("isListGroup", true);
		return "list-group-document";
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
	
	@GetMapping("/add")
	public String addGet(Model model) throws Exception {
		model.addAttribute("documentForm", new DocumentForm());
		model.addAttribute("groupList", documentGroupService.getDocumentGroupList());
		model.addAttribute("memberList", memberService.getMemberList());
		model.addAttribute("divisionList", divisionService.getDivisionList());
		return "add-document";
	}
	
	@PostMapping("/add")
	public String addPost(
			@Valid @ModelAttribute("documentForm") DocumentForm documentForm,
			Errors errors,
			HttpSession session,
			RedirectAttributes redirectAttributes,
			Model model) throws Exception {
		MultipartFile upfile = documentForm.getUpfile();
		//アップロードファイルのチェック
		if (upfile.isEmpty()) {
			errors.rejectValue("upfile", "error.empty_file");
		}else {
			if (!documentForm.isFileContentType(upfile.getContentType())) {
				errors.rejectValue("upfile", "error.not_contentType_file");
			}
		}
		//リマインド情報のチェック
		if (documentForm.getRemindMemberIdList().size()>0) {
			if ((documentForm.getRemindStartDate()==null) || (documentForm.getRemindEndDate()==null)) {
				errors.rejectValue("remindStartDate", "error.null_remindStartDate");
			}else {
				if (documentForm.getRemindStartDate().after(documentForm.getRemindEndDate())) {
					errors.rejectValue("remindStartDate", "error.fraud_remindStartDate");
				}
			}
			//リマインド対象者に閲覧許可がない社員がいる場合、
			for (Integer remindMember : documentForm.getRemindMemberIdList()) {
				if (!documentForm.getAccessMemberIdList().contains(remindMember)) {
					errors.rejectValue("accessMemberIdList", "error.not_accessMember");
					break;
				}
			}	
		}
		
		if (errors.hasErrors()) {
			model.addAttribute("groupList", documentGroupService.getDocumentGroupList());
			model.addAttribute("memberList", memberService.getMemberList());
			model.addAttribute("divisionList", divisionService.getDivisionList());
			return "add-document";
		}
		documentService.addDocument(documentForm, session);
		redirectAttributes.addFlashAttribute("message", "ドキュメントを追加しました。");
		if ((boolean) session.getAttribute("isListGroup")) {
			return "redirect:/doc/listGroup";
		}else {
			return "redirect:/doc/list";	
		}
	}
	
	@GetMapping("/edit/{id}")
	public String editGet(
			@PathVariable Integer id,
			Model model) throws Exception {
		model.addAttribute("documentForm", documentService.getDocumentById(id));
		model.addAttribute("groupList", documentGroupService.getDocumentGroupList());
		model.addAttribute("memberList", memberService.getMemberList());
		model.addAttribute("divisionList", divisionService.getDivisionList());
		return "edit-document";
	}
	
	@PostMapping("/edit/{id}")
	public String editPost(
			@Valid @ModelAttribute("documentForm") DocumentForm documentForm,
			Errors errors,
			HttpSession session,
			RedirectAttributes redirectAttributes,
			Model model) throws Exception {
		MultipartFile upfile = documentForm.getUpfile();
		//アップロードファイルのチェック
		if (!upfile.isEmpty() && !documentForm.isFileContentType(upfile.getContentType())) {
			errors.rejectValue("upfile", "error.not_contentType_file");
		}
		//リマインド情報のチェック
		if (documentForm.getRemindMemberIdList().size()>0) {
			if ((documentForm.getRemindStartDate()==null) || (documentForm.getRemindEndDate()==null)) {
				errors.rejectValue("remindStartDate", "error.null_remindStartDate");
			}else {
				if (documentForm.getRemindStartDate().after(documentForm.getRemindEndDate())) {
					errors.rejectValue("remindStartDate", "error.fraud_remindStartDate");
				}
			}
			//リマインド対象者に閲覧許可がない社員がいる場合、
			for (Integer remindMember : documentForm.getRemindMemberIdList()) {
				if (!documentForm.getAccessMemberIdList().contains(remindMember)) {
					errors.rejectValue("accessMemberIdList", "error.not_accessMember");
					break;
				}
			}	
		}
		
		if (errors.hasErrors()) {
			model.addAttribute("groupList", documentGroupService.getDocumentGroupList());
			model.addAttribute("memberList", memberService.getMemberList());
			model.addAttribute("divisionList", divisionService.getDivisionList());
			return "edit-document";
		}
		documentService.editDocument(documentForm, session);
		redirectAttributes.addFlashAttribute("message", "ドキュメントを更新しました。");
		if ((boolean) session.getAttribute("isListGroup")) {
			return "redirect:/doc/listGroup";
		}else {
			return "redirect:/doc/list";	
		}
	}
	
	@GetMapping("/delete/{id}")
	public String deleteGet(
			@PathVariable("id") Integer id,
			HttpSession session,
			RedirectAttributes redirectAttributes) throws Exception {
		documentService.deleteDocument(id);
		redirectAttributes.addFlashAttribute("message", "ドキュメントを削除しました。");
		if ((boolean) session.getAttribute("isListGroup")) {
			return "redirect:/doc/listGroup";
		}else {
			return "redirect:/doc/list";	
		}
	}
	
}
