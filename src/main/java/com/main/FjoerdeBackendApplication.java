package com.main;

import com.main.model.School;
import com.main.model.userTypes.User;
import com.main.model.userTypes.UserAuthority;
import com.main.model.userTypes.UserData;
import com.main.repository.SchoolRepository;
import com.main.repository.UserRepository;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@EnableScheduling
public class FjoerdeBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(FjoerdeBackendApplication.class, args);
	}

	@Bean
	public CommandLineRunner dataLoader(UserRepository userRepo, PasswordEncoder encoder, SchoolRepository schoolRepo,
			UserData userData) { // user repo for ease of testing with a built-in user
		return new CommandLineRunner() {
			@Override
			public void run(String... args) throws Exception {

				List<User> users = userData.getUserData();
				for (User user : users) {
					user.addAuthority(UserAuthority.byRole(user.getRole()));
					userRepo.save(user);
					System.out.println(user);
				}

				// adds a school for simpler testing
				School school = new School("Holstenschule", "Altonaer Str. 40, 24534 Neumünster");
				schoolRepo.save(school);
			}
		};
	}

}