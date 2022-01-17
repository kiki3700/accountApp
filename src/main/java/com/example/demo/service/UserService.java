package com.example.demo.service;

import java.security.SignatureException;

import com.example.demo.dto.TokenDto;
import com.example.demo.dto.UserDto;

public interface UserService {
	boolean signUp(UserDto userDto);

	boolean signIn(UserDto userDto);

	boolean signOut(TokenDto tokenDto);
	
	boolean verifyId(String id);

	TokenDto enrollTokens(UserDto userDto);

	UserDto getUserInfo(TokenDto tokenDto) throws SignatureException;



}

