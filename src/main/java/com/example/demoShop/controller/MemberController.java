package com.example.demoShop.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demoShop.dto.MemberDTO;
import com.example.demoShop.service.MemberService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/members")
public class MemberController {
	private final MemberService memberService;
	
	public MemberController(MemberService memberService) {
		this.memberService = memberService;
	}
	
	//회원 가입
    @PostMapping("/join")
    public ResponseEntity<?> registerMember(@RequestBody MemberDTO memberDTO) {
        int result = memberService.insertMember(memberDTO);
        if(result == -1) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이미 사용 중인 이메일입니다.");
        } else if(result == -2) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이미 사용 중인 휴대폰 번호입니다.");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("회원 가입 성공");
    }

    //이메일 중복 확인
    @GetMapping("/check-email")
    public boolean checkEmail(@RequestParam String email) {
        return memberService.checkEmail(email);
    }

    //전체 회원 조회
    @GetMapping("/all")
    public ResponseEntity<List<MemberDTO>> getAllMembers() {
        return ResponseEntity.ok(memberService.selectAllMember());
    }

    //회원 검색
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
        if(loggedInMember != null) {
            return ResponseEntity.ok(loggedInMember);
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
            //링크 이메일 보내는 로직 추가
        	
        	
            return ResponseEntity.ok("비밀번호 재설정 링크를 이메일로 전송했습니다.");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("회원 정보를 찾을 수 없습니다.");
    }
}
