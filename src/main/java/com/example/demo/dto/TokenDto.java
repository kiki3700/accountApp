package com.example.demo.dto;

public class TokenDto {
	String asscessToken;
	String refreshToken;
	public TokenDto() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getAsscessToken() {
		return asscessToken;
	}
	public void setAsscessToken(String asscessToken) {
		this.asscessToken = asscessToken;
	}
	public String getRefreshToken() {
		return refreshToken;
	}
	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
	@Override
	public String toString() {
		return "TokenDto [asscessToken=" + asscessToken + ", refreshToken=" + refreshToken + "]";
	}
}
