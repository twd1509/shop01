package com.example.demoShop.service;

import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demoShop.dto.MemberDTO;
import com.example.demoShop.mapper.MemberMapper;

@Service
public class MemberService {
	private final MemberMapper mbrMapper;
	private final BCryptPasswordEncoder passwordEncoder;
	
	public MemberService(MemberMapper mbrMapper, BCryptPasswordEncoder passwordEncoder) {
		this.mbrMapper = mbrMapper;
		this.passwordEncoder = passwordEncoder;
	}
	
	//회원 가입
	public int insertMember(MemberDTO mbrDto) {
		if(!checkEmail(mbrDto.getEmail())) return -1;
		if(!checkPhone(mbrDto.getPhone())) return -2;
		
		mbrDto.setPassword(passwordEncoder.encode(mbrDto.getPassword()));
		return mbrMapper.insertMember(mbrDto);
	}
	
	//이메일 중복 확인
	public boolean checkEmail(String email) {
		return mbrMapper.checkEmail(email) > 0;
	}
	
	//휴대폰 중복 확인
	public boolean checkPhone(String phone) {
		return mbrMapper.checkPhone(phone) > 0;
	}
	
	//전체 회원 조회
	public List<MemberDTO> selectAllMember() {
		return mbrMapper.selectAllMember();
	}
	
	//회원 조회
	public List<MemberDTO> selectMember(String searchType, String keyword) {
		List<MemberDTO> result = null;
		keyword = keyword.replace(" ", "");
		
		switch (searchType) {
		case "email": 
			result = mbrMapper.selectMemberByEmail(keyword);
			break;
		case "name":
			result = mbrMapper.selectMemberByName(keyword);
			break;
		case "phone":
			keyword = keyword.replace("-", "");
			result = mbrMapper.selectMemberByPhone(keyword);
			break;
		case "grade":
			result = mbrMapper.selectMemberByGrade(Integer.parseInt(keyword));
			break;
		case "no":
			result = mbrMapper.selectMemberByNo(Integer.parseInt(keyword));
			break;
		default:
			result = mbrMapper.selectMember(keyword);
			break;
		}
		
		return result;
	}
	
	//회원 수정
	public int updateMember(MemberDTO mbrDto) {
		return mbrMapper.updateMember(mbrDto);
	}
	
	//회원 탈퇴
	public int deleteMember(int no) {
		return mbrMapper.deleteMember(no);
	}
	
	//로그인
	public MemberDTO loginMember(String email, String password) {
		MemberDTO member = mbrMapper.loginMember(email, passwordEncoder.encode(password));
//        if(member != null && passwordEncoder.matches(password, member.getPassword())) {
//            return member;
//        }
		
		return member;
	}
	
	//아이디 찾기
	public MemberDTO findMemberByEmail(String phone, String name) {
		return mbrMapper.findMemberByEmail(phone, name);
	}
	
	//비밀번호 찾기
	public MemberDTO findMemberByPw(String phone, String name, String email) {
		return mbrMapper.findMemberByPw(phone, name, email);
	}
	
	//이메일로 조회
	public MemberDTO selectMemberByEmail(String email) {
		return mbrMapper.selectMemberByEmail(email).get(0);
	}
}
