package com.example.demo.util;

import java.security.NoSuchAlgorithmException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.demo.utils.AuthUtil;

@SpringBootTest 
public class EcryptTest {
	@Autowired
	AuthUtil auth;
	
	@Test
	public void encryptTest1() throws NoSuchAlgorithmException {
		String pw = "lolo5050";
		String encryptedPw = auth.encypt(pw);
		Assertions.assertTrue(encryptedPw.equals(auth.encypt(pw)));
	}
	
	@Test
	public void encryptTest2() throws NoSuchAlgorithmException {
		String pw = "lolo5050";
		String encryptedPw = auth.encypt(pw);
		Assertions.assertFalse(encryptedPw.equals(auth.encypt("kiki3700")));
	}
}
