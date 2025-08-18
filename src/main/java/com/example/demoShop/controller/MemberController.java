package com.example.demoShop.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demoShop.JwtUtil;
import com.example.demoShop.dto.LoginResponse;
import com.example.demoShop.dto.MemberDTO;
import com.example.demoShop.dto.ResetPasswordRequest;
import com.example.demoShop.service.EmailService;
import com.example.demoShop.service.MemberService;

import jakarta.mail.MessagingException;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/members")
public class MemberController {
	private final MemberService memberService;
	private final JwtUtil jwtUtil;
	private final EmailService emailService;
    private final BCryptPasswordEncoder passwordEncoder;
	
	public MemberController(MemberService memberService, JwtUtil jwtUtil, EmailService emailService, BCryptPasswordEncoder passwordEncoder) {
        this.memberService = memberService;
        this.jwtUtil = jwtUtil;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }
	
	//회원 가입
    @PostMapping("/join")
    public ResponseEntity<?> registerMember(@RequestBody MemberDTO memberDTO) {
    	try {
	        int result = memberService.insertMember(memberDTO);
	        
	        if(result > 0) {
	        	return ResponseEntity.status(HttpStatus.CREATED).body("회원 가입 성공");
	        }
	        
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("회원 가입 실패");
    	} catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    //이메일 중복 확인
    @GetMapping("/check-email")
    public boolean checkEmail(@RequestParam String email) {
        return memberService.checkEmail(email);
    }

    //전체 회원 조회
    @GetMapping("/list")
    public ResponseEntity<List<MemberDTO>> getAllMembers() {
        return ResponseEntity.ok(memberService.selectAllMember());
    }

    //회원 검색(name, email)
    @GetMapping("/search")
    public ResponseEntity<List<MemberDTO>> searchMember(
            @RequestParam String searchType,
            @RequestParam String keyword) {
        return ResponseEntity.ok(memberService.selectMember(searchType, keyword));
    }

    //회원 수정
    @PostMapping("/modify/{no}")
    public ResponseEntity<?> updateMember(@PathVariable int no, @RequestBody MemberDTO memberDTO) {
        memberDTO.setNo(no); 		//DTO에 회원 번호 설정
        int result = memberService.updateMember(memberDTO);
        if(result > 0) {
            return ResponseEntity.ok("회원 정보 수정 성공");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("회원 정보를 찾을 수 없습니다.");
    }

    //회원 탈퇴
    @PostMapping("/delete/{no}")
    public ResponseEntity<?> deleteMember(@PathVariable int no) {
        int result = memberService.deleteMember(no);
        
        if(result > 0) {
            return ResponseEntity.ok("회원 탈퇴 성공");
        }
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("회원 정보를 찾을 수 없습니다.");
    }

    //로그인
    @PostMapping("/login")
    public ResponseEntity<?> loginMember(@RequestBody MemberDTO memberDTO) {
    	MemberDTO loggedInMember = memberService.loginMember(memberDTO.getEmail(), memberDTO.getPassword());
        if (loggedInMember != null) {
            String token = jwtUtil.generateToken(memberDTO.getEmail());
            return ResponseEntity.ok().body(new LoginResponse(token, loggedInMember));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("이메일 또는 비밀번호가 잘못되었습니다.");
    }

    //아이디 찾기
    @PostMapping("/find-email")
    public ResponseEntity<?> findEmail(@RequestBody MemberDTO memberDTO) {
        MemberDTO foundMember = memberService.findMemberByEmail(memberDTO.getPhone(), memberDTO.getName());
        if(foundMember != null) {
            return ResponseEntity.ok(foundMember.getEmail());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("회원 정보를 찾을 수 없습니다.");
    }

    //비밀번호 찾기
    @PostMapping("/find-password")
    public ResponseEntity<?> findPassword(@RequestBody MemberDTO memberDTO) {
        MemberDTO foundMember = memberService.findMemberByPw(
                memberDTO.getPhone(), memberDTO.getName(), memberDTO.getEmail());
        if(foundMember != null) {
            //비밀번호 재설정 링크 이메일 전송
        	try {
                String resetToken = jwtUtil.generatePasswordResetToken(foundMember.getEmail());
                emailService.sendPasswordResetEmail(foundMember.getEmail(), resetToken);
                
                return ResponseEntity.ok("비밀번호 재설정 링크를 이메일로 전송했습니다.");
            } catch (MessagingException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("이메일 전송에 실패했습니다.");
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("회원 정보를 찾을 수 없습니다.");
    }
    
    //비밀번호 재설정
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) {
    	//로그인 확인
        if(!jwtUtil.validateToken(request.getToken()) || !jwtUtil.isPasswordResetToken(request.getToken())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("유효하지 않은 토큰입니다.");
        }

        String email = jwtUtil.getEmailFromToken(request.getToken());
        MemberDTO member = memberService.selectMemberByEmail(email);
        
        if(member == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 이메일로 회원을 찾을 수 없습니다.");
        }

        member.setPassword(passwordEncoder.encode(request.getNewPassword()));
        int result = memberService.updateMember(member);
        
        if(result > 0) {
            return ResponseEntity.ok("비밀번호가 성공적으로 변경되었습니다.");
        }
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("비밀번호 변경에 실패했습니다.");
    }
    
    //현재 사용자 정보 조회
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.replace("Bearer ", "");
            if (!jwtUtil.validateToken(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 토큰입니다.");
            }
            String email = jwtUtil.getEmailFromToken(token);
            MemberDTO memberDTO = memberService.selectMemberByEmail(email);
            if (memberDTO == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("회원 정보를 찾을 수 없습니다.");
            }
            return ResponseEntity.ok(memberDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("토큰 처리 중 오류가 발생했습니다.");
        }
    }
}
