package com.example.demo.userTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.example.demo.controller.UserController;

@SpringBootTest
public class UserControllerTest {
	
	private MockMvc mvc;
	@Autowired
	UserController userControler;
	
	@BeforeEach
	void setup() {
		mvc = MockMvcBuilders.standaloneSetup(userControler).build();
	}
	
	@Test
	void VarifyId() throws Exception {
		MockHttpServletRequestBuilder builder = get("/user").param("id", "575492q@gmail.com");
		mvc.perform(builder).andExpect(status().isOk());
	}
}
