package com.example.demo.mapper;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.dto.UserDto;

@Mapper
public interface UserMapper {
	boolean selectSignOut(UserDto userDto);
	List<HashMap<String, Object>> selectTest();
	int insertUser(UserDto userDto);
	UserDto selectUserDtoById(String id);
	UserDto selectUserDtoByRefreshToken(String refreshToken);
	int updateLogOutStatus(UserDto userDto);
	int deleteUserById(UserDto userDto);
	int updateRefreshToken(UserDto userDto);
}
