package com.example.demoShop.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.demoShop.dto.MemberDTO;

@Mapper
public interface MemberMapper {
	//전체 회원 조회
	List<MemberDTO> selectAllMember();
	//회원 조회
	List<MemberDTO> selectMemberByNo(int no);				//no
	List<MemberDTO> selectMemberByName(String name);		//이름
	List<MemberDTO> selectMemberByEmail(String email);		//이메일
	List<MemberDTO> selectMemberByPhone(String phone);		//이메일
	List<MemberDTO> selectMemberByGrade(int grade);			//등급(관리자 : 1, 일반회원 : 9)
	List<MemberDTO> selectMember(String keyword);			//전체 검색
	//회원 등록
	int insertMember(MemberDTO mbrDto);
	//회원 수정
	int updateMember(MemberDTO mbrDto);
	//회원 탈퇴
	int deleteMember(int no);
	//중복 확인
	int checkEmail(String email);		//이메일
	int checkPhone(String phone);		//휴대폰
	//로그인
	MemberDTO loginMember(String email);
	//이메일 찾기
	MemberDTO findMemberByEmail(String phone, String name);
	//비밀번호 찾기
	MemberDTO findMemberByPw(String phone, String name, String email);
}
