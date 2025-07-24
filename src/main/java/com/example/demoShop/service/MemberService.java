package com.example.demoShop.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demoShop.dto.MemberDTO;
import com.example.demoShop.mapper.MemberMapper;

@Service
public class MemberService {
	private final MemberMapper mbrMapper;
	
	public MemberService(MemberMapper mbrMapper) {
		this.mbrMapper = mbrMapper;
	}
	
	public int insertMember(MemberDTO mbrVo) {
		return mbrMapper.insertMember(mbrVo);
	}
	
	public List<MemberDTO> selectAllMember() {
		return mbrMapper.selectAllMember();
	}
	
	public MemberDTO loginMember(String email, String password) {
		return mbrMapper.loginMember(email, password);
	}
}
