package com.example.demo.userTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.demo.dto.UserDto;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.UserService;

@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
public class signUpTest {
	@Autowired
	UserService userService;
	@Autowired
	UserMapper userMapper;
	
	static UserDto userDto;
	
	@BeforeEach
	public void init() {
		userDto = new UserDto();
		userDto.setId("admin@naver.con");
		userDto.setPassword("lolo5050");
		userDto.setName("관리자");
	}
	@Test
	@Order(1)
	public void verifyId1() {
		assertTrue(userService.verifyId(userDto.getId()));
	}
	@Test
	@Order(2)
	public void signup() {
		assertTrue(userService.signUp(userDto));
	}
	@Test
	@Order(3)
	public void verifyId2() {
		assertFalse(userService.verifyId(userDto.getId()));
	}
	@Test
	@Order(4)
	public void deleteUser() {
		assertEquals(1, userMapper.deleteUserById(userDto));
	}
}
