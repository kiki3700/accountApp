package com.example.demo.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.example.demo.dto.UserDto;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class AuthUtil {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass()); 
	
	   private static final String JWT_SECRET = "secretKey";

	    public String generateAccessToken(UserDto user) {
	        Date now = new Date();
	        Date expiryDate = new Date(now.getTime() + 1000*60*30L);
	        return Jwts.builder()
	        	.setHeaderParam("type", "JWT")
	            .setSubject(user.getId()) // 사용자
	            .setIssuedAt(new Date()) // 현재 시간 기반으로 생성
	            .setExpiration(new Date(now.getTime() + 1000*60*30L)) // 만료 시간 세팅
	            .signWith(SignatureAlgorithm.HS512, JWT_SECRET) // 사용할 암호화 알고리즘, signature에 들어갈 secret 값 세팅
	            .compact();
	    }
	    
	    public String generateRefreshToken() {
	        Date now = new Date();
	        Date expiryDate = new Date(now.getTime() + 1000*60*60*24*7L);
	        return Jwts.builder()
		        	.setHeaderParam("type", "JWT")
		            .setIssuedAt(new Date()) // 현재 시간 기반으로 생성
		            .setExpiration(expiryDate) // 만료 시간 세팅
		            .signWith(SignatureAlgorithm.HS512, JWT_SECRET) // 사용할 암호화 알고리즘, signature에 들어갈 secret 값 세팅
		            .compact();
	    }

	    public String getUserIdFromJWT(String token){
	    	try{
	    		Claims claims = Jwts.parser()
	    				.setSigningKey(JWT_SECRET)
	    				.parseClaimsJws(token)
	    				.getBody();
	    		logger.error("get user id from access token success");
	    		return claims.getSubject();
	    	}catch(ExpiredJwtException e) {
	    		logger.error("Token can't parse because Expired JWT token");
	    	}
	    	return null;
	    }

	    // Jwt 토큰 유효성 검사
	    public boolean validateToken(String token) throws SignatureException{
	        try {
	            Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token);
	            return true;
	        } catch (MalformedJwtException e) {
	        	logger.error("Invalid JWT token");
	        } catch (ExpiredJwtException e) {
	        	logger.error("Expired JWT token");
	        } catch (UnsupportedJwtException e) {
	        	logger.error("Unsupported JWT token");
	        } catch (IllegalArgumentException e) {
	        	logger.error("JWT claims string is empty.");
	        }
	        return false;
	    }
	
	public String encypt(String text) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		md.update(text.getBytes());
		return bytesToHex(md.digest());
	}
	
	private String bytesToHex(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for(byte b : bytes) {
			sb.append(String.format("%02x", b));
		}
		return sb.toString();
	}
}
