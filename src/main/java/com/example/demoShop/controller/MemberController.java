package com.example.demoShop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demoShop.dto.MemberDTO;
import com.example.demoShop.service.MemberService;

import jakarta.servlet.http.HttpSession;

@Controller
public class MemberController {
	private final MemberService memberService;
	
	public MemberController(MemberService memberService) {
		this.memberService = memberService;
	}
	
	//회원가입 페이지
	@GetMapping("/signup")
	public String signUpPage(Model model) {
		model.addAttribute("member", new MemberDTO());
		
		return "login-register";
	}
	
	//회원가입 처리
	@PostMapping("/signup")
	public String signUp(@ModelAttribute MemberDTO mbrDto, Model model){
		try {
			memberService.insertMember(mbrDto);
			return "redirect:/login";
		} catch (Exception e) {
			model.addAttribute("error", "회원 가입에 실패했습니다.");
			return "login-register";
		}
	}
	
	//로그인 페이지
    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("member", new MemberDTO());
        return "login";
    }

    //로그인 처리
    @PostMapping("/login")
    public String login(@ModelAttribute MemberDTO mbrDto, HttpSession session, Model model) {
        MemberDTO loginMember = memberService.loginMember(mbrDto.getEmail(), mbrDto.getPassword());
        if (loginMember != null) {
            session.setAttribute("member", loginMember);
            return "redirect:/";
        } else {
            model.addAttribute("error", "이메일 또는 비밀번호가 잘못되었습니다.");
            return "login";
        }
    }
    
	//로그아웃
	@GetMapping("/logout")
	public String logout(HttpSession session) {
	    session.invalidate();
	    return "redirect:/login";
	}
}
