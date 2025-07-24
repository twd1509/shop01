package com.example.demoShop.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.demoShop.dto.MemberDTO;

@Mapper
public interface MemberMapper {
	List<MemberDTO> selectAllMember();
	int insertMember(MemberDTO mbrVo);
	MemberDTO loginMember(String email, String password);
}
