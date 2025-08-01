package com.example.demoShop.dto;

public class LoginResponse {
	private String token;
    private MemberDTO member;

    public LoginResponse(String token, MemberDTO member) {
        this.token = token;
        this.member = member;
    }

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public MemberDTO getMember() {
		return member;
	}

	public void setMember(MemberDTO member) {
		this.member = member;
	}
}
