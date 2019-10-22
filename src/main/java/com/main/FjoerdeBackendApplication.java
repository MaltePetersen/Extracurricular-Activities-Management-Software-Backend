package com.main;

import com.main.model.AfterSchoolCare;
import com.main.model.School;
import com.main.model.userTypes.User;
import com.main.model.userTypes.UserData;
import com.main.repository.SchoolRepository;
import com.main.repository.UserRepository;
import com.main.service.AfterSchoolCareService;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class FjoerdeBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(FjoerdeBackendApplication.class, args);
    }


    @Bean
    public CommandLineRunner dataLoader(UserRepository userRepo, PasswordEncoder encoder, SchoolRepository schoolRepo, UserData userData, AfterSchoolCareService afterSchoolCareService) { // user repo for ease of testing with a built-in user
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {

                List<User> users = userData.getUserData();
                for (User user : users) {
					userRepo.save(user);
				}

                // adds a school for simpler testing
                School school = new School("Holstenschule", "Altonaer Str. 40, 24534 Neumünster");
                schoolRepo.save(school);

                AfterSchoolCare afterSchoolCare = new AfterSchoolCare();
                afterSchoolCare.setParticipatingSchool(school);
                afterSchoolCareService.save(afterSchoolCare);
            }
        };
    }

}