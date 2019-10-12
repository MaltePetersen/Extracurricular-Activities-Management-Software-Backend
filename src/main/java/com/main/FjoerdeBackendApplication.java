package com.main;

import com.main.model.userTypes.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import com.main.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class FjoerdeBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(FjoerdeBackendApplication.class, args);
    }


    @Bean
    public CommandLineRunner dataLoader(        UserRepository userRepo, PasswordEncoder encoder) { // user repo for ease of testing with a built-in user
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                userRepo.save(new User("Parent_Test", encoder.encode("password"),"Craig Walls", "craig@walls.com", "123-123-1234"));
                userRepo.save(new User("Employee_Test",encoder.encode("password"),"Malte Petersen","malte.petersen@web.de","123-123-1234","Geschichte","1374816241982437","Schloßstraße 33",false));
                userRepo.save(new User("Employee_SchoolCoordinator_Test",encoder.encode("password"),"Malte Petersen","malte.petersen@web.de","123-123-1234","Geschichte","1374816241982437","Schloßstraße 33",true));
                userRepo.save(new User("Teacher_SchoolCoordinator_Test",encoder.encode("password"),"Malte Petersen","7a","123-123-1234","Schloßstraße 33", true));
                userRepo.save(new User("Management_Test",encoder.encode("password"),"Malte Petersen", "malte.petersen@web.de","123-123-1234","Street 7"));
                userRepo.save(new User("Teacher_Test",encoder.encode("password"),"Malte Petersen","malte.petersen@web.de","123-123-1234","Geschichte",false));
                userRepo.save(new User("Teacher_SchoolCoordinator_Test",encoder.encode("password"),"Malte Petersen","malte.petersen@web.de","123-123-1234","Sport",true));
                userRepo.save(new User("User_Test",encoder.encode("password"),"Malte Petersen", "USER"));
                userRepo.save(new User("Child_Test",encoder.encode("password"),"Malte Petersen", "7a"));


            }
        };
    }

}