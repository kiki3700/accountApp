package com.example.demo.interceptor;

import java.io.IOException;
import java.security.SignatureException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.example.demo.dto.UserDto;
import com.example.demo.mapper.UserMapper;
import com.example.demo.utils.AuthUtil;

import io.jsonwebtoken.ExpiredJwtException;

@Component
public class AuthInterceptor implements HandlerInterceptor{
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	UserMapper userMapper;
	@Override
	
	public boolean preHandle(HttpServletRequest request,HttpServletResponse response, Object handler) throws SignatureException, IOException {
		String redirectUri = "/signin";
		String targetUri = request.getRequestURI();
//		String[] uriWithAuthArr = { "/signup", "signin"};
//		for(int i = 0 ; i < uriWithAuthArr.length; i++) {
//			if(uriWithAuthArr[i].equals(targetUri)) redirectUri = "/accounting";
//		}
//		
		logger.debug("================================");
		logger.debug(targetUri+ " AuthInterceptor start");
		logger.debug("================================");
		Cookie[] cookies= request.getCookies();
		String accessToken = new String();
		String refreshToken = new String();
		if(cookies==null) {
			logger.debug("there are no cookies");
			logger.debug("=================================");
			logger.debug("AuthInterceptor finish");
			logger.debug("=================================");
			response.sendRedirect(redirectUri);
			return false;
		}
		for(Cookie cookie : cookies) {
			String key = cookie.getName();
			if(key.equals("access-token")) accessToken = cookie.getValue();
			if(key.equals("refresh-token")) refreshToken = cookie.getValue();
		}
		if(refreshToken== null&&accessToken==null) {
			logger.debug("there are no tokens");
			logger.debug("=================================");
			logger.debug("AuthInterceptor finish");
			logger.debug("=================================");
			response.sendRedirect(redirectUri);
			return false;
		}
		logger.debug("access-token : "+ !accessToken.isEmpty());
		logger.debug("refresh-token : "+ !refreshToken.isEmpty());

		UserDto userDto = userMapper.selectUserDtoByRefreshToken(refreshToken);
		

		logger.debug("user are not sign out");
		logger.debug("token validation start");
		if(userDto==null) {
			logger.debug("refresh token doesn't math with users info or user already signout");
			logger.debug("=================================");
			logger.debug("AuthInterceptor finish");
			logger.debug("=================================");
			response.sendRedirect(redirectUri);
			return false;
		}

			if(AuthUtil.validateToken(accessToken)) {
				logger.debug("access token is valid");
				logger.debug("=================================");
				logger.debug("AuthInterceptor finish");
				logger.debug("=================================");
				return true;
			}
			logger.debug("access-token is expired");


			if(AuthUtil.validateToken(refreshToken)) {
				logger.debug("refresh token is validated");
				accessToken = AuthUtil.generateAccessToken(userDto);
				Cookie cookie = new Cookie("access-token", accessToken);
				cookie.setMaxAge(1000*60*30);
				cookie.setPath("/");
				cookie.setHttpOnly(true);
				response.addCookie(cookie);
				logger.debug("generate new access token");
				logger.debug("=================================");
				logger.debug("AuthInterceptor finish");
				logger.debug("=================================");
				return true;
			}
			logger.debug("refresh token is expired too");
			logger.debug("=================================");
			logger.debug("AuthInterceptor finish");
			logger.debug("=================================");
		response.sendRedirect(redirectUri);
		return false;
	}
}
