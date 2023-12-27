package com.example.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.app.domain.Login;
import com.example.app.domain.Member;
import com.example.app.login.LoginAuthority;
import com.example.app.login.LoginStatus;
import com.example.app.service.MemberService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class MemberLoginController {

	@Autowired
	MemberService service;
	
	@GetMapping("/login")
	public String loginGet(Model model) {
		model.addAttribute("login", new Login());
		return "login";
	} 
	
	@PostMapping("/login")
	public String loginPost(
			@Valid @ModelAttribute("login") Login login,
			Errors errors,
			HttpSession session) throws Exception {
		// バリデーション有無
		if (errors.hasErrors()) {
			return "login";
		}
		// ログイン認証
		Member member = service.getMemberByLoginId(login.getLoginId());
		if ((member==null) || (!login.isCorrectPassword(member.getLoginPass()))) {
			errors.rejectValue("loginId", "error.incorrect_id_password");
			return "login";
		}
		// セッションに認証情報を格納
		LoginStatus loginStatus = new LoginStatus(
				member.getId(), member.getMemberNumber(), member.getName(), member.getLoginId(),LoginAuthority.MEMBER);
		session.setAttribute("loginStatus", loginStatus);
		return "redirect:/doc/list";
	}
	
	@GetMapping("/logout")
	public String logout(
			HttpSession session,
			RedirectAttributes redirectAttributes) {
		session.removeAttribute("loginStatus");
		redirectAttributes.addFlashAttribute("message", "ログアウトしました。");
		return "redirect:/login";
	}
}
