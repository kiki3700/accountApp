package com.example.demo.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.demo.interceptor.AuthInterceptor;


@Configuration
public class WebSecuriteConfig implements WebMvcConfigurer {
	
		@Autowired
		AuthInterceptor authInterceptor;
	   @Override
	   public void addInterceptors(InterceptorRegistry registry) {
		   registry.addInterceptor(authInterceptor)
		   .addPathPatterns("/accounting/**");
//		   .addPathPatterns("/")
//		   .addPathPatterns("/signin")
//		   .addPathPatterns("/signup");
	   }
}