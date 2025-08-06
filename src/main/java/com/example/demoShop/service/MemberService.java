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
		if (mbrDto.getEmail() == null || mbrDto.getEmail().isEmpty()) {
            throw new IllegalArgumentException("이메일은 필수 입력입니다.");
        }
		if (mbrDto.getPassword() == null || mbrDto.getPassword().isEmpty()) {
            throw new IllegalArgumentException("비밀번호를 입력해 주세요.");
        }
		if (mbrDto.getName() == null || mbrDto.getName().isEmpty()) {
            throw new IllegalArgumentException("이름을 입력해 주세요.");
        }
		if (mbrDto.getPhone() == null || mbrDto.getPhone().isEmpty()) {
            throw new IllegalArgumentException("휴대폰 번호은 필수 입력입니다.");
        }
		if (mbrDto.getAddress1() == null || mbrDto.getAddress1().isEmpty()) {
            throw new IllegalArgumentException("주소를 입력해 주세요.");
        }
		
		if(checkEmail(mbrDto.getEmail())) {
			throw new IllegalArgumentException("이미 등록된 이메일 입니다.");
		}
		if(checkPhone(mbrDto.getPhone())) {
			throw new IllegalArgumentException("이미 등록된 휴대폰 번호 입니다.");
		}
		
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
		mbrDto.setPassword(passwordEncoder.encode(mbrDto.getPassword()));
		
		return mbrMapper.updateMember(mbrDto);
	}
	
	//회원 탈퇴
	public int deleteMember(int no) {
		return mbrMapper.deleteMember(no);
	}
	
	//로그인
	public MemberDTO loginMember(String email, String password) {
		MemberDTO member = mbrMapper.loginMember(email);
		
		if (member != null && passwordEncoder.matches(password, member.getPassword())) {
            return member;
        }
		
        return null;
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
