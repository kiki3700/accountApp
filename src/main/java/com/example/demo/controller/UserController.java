package com.example.demo.controller;

import java.io.IOException;
import java.security.SignatureException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.dto.TokenDto;
import com.example.demo.dto.UserDto;
import com.example.demo.service.UserService;
import com.example.demo.utils.AuthUtil;

@RestController
public class UserController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	UserService userService;
	
	@Autowired
	AuthUtil authUtil;
	
	@GetMapping("/signin")
	public ModelAndView getLoginPage() {
//		logger.debug("=======================================");
//		logger.debug("get login Page");
//		logger.debug("=======================================");
		ModelAndView mv = new ModelAndView();
		mv.setViewName("signin");
		return mv;
	}
	
	@GetMapping("/signup")
	public ModelAndView getSignupPage() {
//		logger.debug("=======================================");
//		logger.debug("get signup Page");
//		logger.debug("=======================================");
		ModelAndView mv = new ModelAndView();
		mv.setViewName("signup");
		return mv;
	}
	
	@PostMapping("/signin")
	public ResponseEntity<Void> signin(@RequestBody UserDto userDto, HttpServletResponse res){
//		logger.debug("=======================================");
//		logger.debug("signin start");
//		logger.debug("=======================================");
		boolean validate = userService.signIn(userDto);
		logger.debug("infor mation validate result : "+ validate);
		if(validate) {
			logger.debug("token generation start...");
			TokenDto tokenDto = userService.enrollTokens(userDto);
			Cookie accessToken = new Cookie("access-token", tokenDto.getAsscessToken());
			accessToken.setMaxAge(1000*60*30);
			accessToken.setHttpOnly(true);
			accessToken.setPath("/");
			Cookie refreshToken = new Cookie("refresh-token", tokenDto.getRefreshToken());
			refreshToken.setMaxAge(1000*60*24*7);
			refreshToken.setHttpOnly(true);
			refreshToken.setPath("/");
			logger.debug("access-token "+tokenDto.getAsscessToken());
			logger.debug("refresh-token" +tokenDto.getRefreshToken());
			res.addCookie(accessToken);
			res.addCookie(refreshToken);
//			logger.debug("=======================================");
//			logger.debug("sign in success");
//			logger.debug("=======================================");
			return ResponseEntity.ok().build();
		}
//		logger.debug("=======================================");
//		logger.debug("sign in fail");
//		logger.debug("=======================================");
		return ResponseEntity.badRequest().build();
	}
	

	@GetMapping("/verifyUser")
	public ResponseEntity<Boolean> checkIdVerification(@RequestParam String id) {
//		logger.debug("=======================================");
//		logger.debug("verify user start");
//		logger.debug("=======================================");
		boolean verification =  userService.verifyId(id);
		logger.debug("result : " + verification);
//		logger.debug("=======================================");
//		logger.debug("verify user finish");
//		logger.debug("=======================================");
		return ResponseEntity.ok().body(verification);		
	}
	@GetMapping("/user")
	public ResponseEntity<UserDto> getUserInfo(@CookieValue(name = "access-token", required = false) String accessToken,@CookieValue(name = "refresh-token", required = false) String refreshToken){
//		logger.debug("=======================================");
//		logger.debug("get user brif infomation start");
//		logger.debug("=======================================");
		TokenDto tokenDto = new TokenDto();
		tokenDto.setAsscessToken(accessToken);
		tokenDto.setRefreshToken(refreshToken);
		UserDto userDto;
		try {
			userDto = userService.getUserInfo(tokenDto);
		} catch (SignatureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ResponseEntity.badRequest().build();
		}
		logger.debug("user is" +userDto);
//		logger.debug("=======================================");
//		logger.debug("get user brif infomation finish");
//		logger.debug("=======================================");
		return ResponseEntity.ok().body(userDto);
	}
	@PostMapping("/user")
	public ResponseEntity<Boolean> signup(@RequestBody UserDto userDto) {
//		logger.debug("=======================================");
//		logger.debug("post new user start");
//		logger.debug("=======================================");
		boolean result =  userService.signUp(userDto);
		logger.debug("result is " +result);
//		logger.debug("=======================================");
//		logger.debug("post new user finish");
//		logger.debug("=======================================");
		return ResponseEntity.ok().body(result);	
	}
	@GetMapping("/signout")
	public ResponseEntity<Void> UserController(@CookieValue(name = "refresh-token", required = false) String refreshToken){
//		logger.debug("=======================================");
//		logger.debug("sign out start");
//		logger.debug("=======================================");
		if(refreshToken==null) {
//			logger.debug("=======================================");
//			logger.debug("sign out finish");
//			logger.debug("=======================================");
			return ResponseEntity.badRequest().build();
		}
		TokenDto tokenDto = new TokenDto();
		tokenDto.setRefreshToken(refreshToken);
		boolean result = userService.signOut(tokenDto);		
//		logger.debug("=======================================");
//		logger.debug("sign out finish");
//		logger.debug("=======================================");
		return ResponseEntity.ok().build();
		
	}
}
