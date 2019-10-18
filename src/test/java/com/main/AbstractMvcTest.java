package com.main;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.main.repository.VerificationTokenRepository;
import com.main.service.UserService;
import com.main.util.UserDTOValidator;

/**
 * 
 * Bietet als abstrakte Klasse die Möglichkeit REST-Componenten
 * zu testen.
 * 
 * @author Markus
 * @since 16.10.2019
 * @version 1.0
 *
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = FjoerdeBackendApplication.class)
@WebAppConfiguration
public abstract class AbstractMvcTest {

	protected MockMvc mockMvc;
	
	protected ObjectMapper objectMapper;

	@Autowired
	WebApplicationContext context;

	protected void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
		objectMapper = new ObjectMapper();
	}
	
	protected String mapToJson(Object obj) throws JsonProcessingException {
		return objectMapper.writeValueAsString(obj);
	}

	
}
