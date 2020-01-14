package com.main.assured;

import com.main.service.UserService;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.restassured.RestAssured;
import io.restassured.module.mockmvc.RestAssuredMockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AbstractAssuredTest {

	@Autowired
	WebApplicationContext context;

	@Autowired
	protected UserService userService;

	@LocalServerPort
	private int port;


	protected ObjectMapper objectMapper;

	public void setUp() throws Exception {
		RestAssured.port = port;
		RestAssuredMockMvc.webAppContextSetup(context);
		objectMapper = new ObjectMapper();
	}

	protected String mapToJson(Object obj) throws JsonProcessingException {
		return objectMapper.writeValueAsString(obj);
	}
	

}
