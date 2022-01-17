package com.example.demo.service.impl;

import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.example.demo.dto.TokenDto;
import com.example.demo.dto.UserDto;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.UserService;
import com.example.demo.utils.AuthUtil;

@Primary
@Service
public class UserServiceImpl implements UserService{
	 private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	AuthUtil authUtil;
	@Autowired
	UserMapper userMapper;
	
	@Override
	public boolean signUp(UserDto userDto) {
		logger.debug("=======================================");
		logger.debug("sign up service start");
		logger.debug("=======================================");
		logger.debug("new user's id : "+ userDto.getId());
		String password = userDto.getPassword();
		try {
			logger.debug("encrypt password .....");
			String encrypt = authUtil.encypt(password);
			userDto.setPassword(encrypt);
			logger.debug("encrypt password success");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.debug("sign up faild");
			logger.debug("=======================================");
			logger.debug("sign up service finish");
			logger.debug("=======================================");
			return false;
		}
		
		int result = userMapper.insertUser(userDto);
		logger.debug(result+" ");
		logger.debug("=======================================");
		logger.debug("sign up service finish");
		logger.debug("=======================================");
		return result == 0 ? false : true;
	}
	@Override
	public boolean verifyId(String id) {
		logger.debug("=======================================");
		logger.debug("verify id service start");
		logger.debug("=======================================");
		UserDto userDto = new UserDto();
		userDto.setId(id);
		UserDto user = userMapper.selectUserDtoById(userDto.getId());
		boolean result = user==null;
		logger.debug("result : "+result);
		logger.debug("=======================================");
		logger.debug("verify id service finish");
		logger.debug("=======================================");
		return result;
	}
	@Override
	public UserDto getUserInfo(TokenDto tokenDto) throws SignatureException {
		logger.debug("=======================================");
		logger.debug("get user brif infomation service start");
		logger.debug("=======================================");
		UserDto userDto = new UserDto();
		String accessToken = tokenDto.getAsscessToken();
		String refreshToken = tokenDto.getRefreshToken();
		logger.debug("refresh token : "+ (refreshToken==null));
		if(refreshToken==null) return null;
		userDto = userMapper.selectUserDtoByRefreshToken(refreshToken);
		logger.debug("user got by token : "+ (userDto!=null));
		if(userDto==null) return null;
		boolean valid = authUtil.validateToken(refreshToken);
		logger.debug("refreshToken validation : "+ valid);
		logger.debug("=======================================");
		logger.debug("get user brif infomation service finish");
		logger.debug("=======================================");
		if(valid) return userDto;
		else return null;
	}
	
	@Override
	public boolean signIn(UserDto userDto) {
		logger.debug("=======================================");
		logger.debug("signin service start");
		logger.debug("=======================================");
		logger.debug("check user information");
		UserDto user = userMapper.selectUserDtoById(userDto.getId());
		if(user!=null) {
			logger.debug("pass word check....");
			try {
				String encryptedPassword = authUtil.encypt(userDto.getPassword());
				if(encryptedPassword.equals(user.getPassword())) {
					logger.debug("validate user information");
					return true;
				}
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		logger.debug("there is no user id, "+userDto.getId());
		logger.debug("=======================================");
		logger.debug("signin service start");
		logger.debug("=======================================");
		return false;
	}
	@Override
	public TokenDto enrollTokens(UserDto userDto) {		
		logger.debug("=======================");
		logger.debug("enroll token start");
		logger.debug("=======================");
		TokenDto tokenDto = new TokenDto();
		String accessToken = authUtil.generateAccessToken(userDto);
		String refreshToken = authUtil.generateRefreshToken();
		tokenDto.setAsscessToken(accessToken);
		tokenDto.setRefreshToken(refreshToken);
		userDto.setRefreshToken(refreshToken);
		logger.debug("user "+userDto.getId()+";s new token");
		logger.debug("accessToken : "+accessToken);
		logger.debug("refreshToken : "+refreshToken);
		userMapper.updateLogOutStatus(userDto);
		int result =  userMapper.updateRefreshToken(userDto);
		if(result == 1) {		
			logger.debug("=======================");
			logger.debug("enroll token sucess");
			logger.debug("=======================");
			return tokenDto;
		}else {
			logger.debug("=======================");
			logger.debug("enroll token fail");
			logger.debug("=======================");
			return null;
		}
	}
	
	@Override
	public boolean signOut(TokenDto tokenDto) {
		logger.debug("=======================");
		logger.debug("sign out service start");
		logger.debug("=======================");
		UserDto userDto = new UserDto();
		logger.debug("delete refresh token");
		userDto.setRefreshToken(tokenDto.getRefreshToken());
		int result = userMapper.updateLogOutStatus(userDto);
		logger.debug("result : "+result);
		logger.debug("=======================");
		logger.debug("sign out service finish");
		logger.debug("=======================");
		return result == 1 ? true : false;
	}
}
