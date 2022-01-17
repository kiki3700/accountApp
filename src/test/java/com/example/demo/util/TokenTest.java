package com.example.demo.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.demo.dto.UserDto;
import com.example.demo.utils.AuthUtil;

@SpringBootTest
public class TokenTest {
	@Autowired
	AuthUtil authUtil;
	
	UserDto userDto;
	
	String accessToken;
	
	@BeforeEach
	void intit() {
		userDto = new UserDto();
		userDto.setId("5575492q@gmail.com");
	}
	
	
	@Test
	@Order(1)
	void Test() throws InterruptedException {
		accessToken = authUtil.generateAccessToken(userDto);
		
		Thread.sleep(10000);
		String id = authUtil.getUserIdFromJWT(accessToken);
		
		Assertions.assertEquals(id, userDto.getId());
	}
	
	
}
