package com.example.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@GetMapping("/")
	public String getMainPage() {
		logger.debug("=======================================");
		logger.debug("get main Page");
		logger.debug("=======================================");
		return "index";
	}
}
