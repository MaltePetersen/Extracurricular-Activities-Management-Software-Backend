package com.main;

import com.main.model.School;
import com.main.model.userTypes.User;
import com.main.repository.SchoolRepository;
import com.main.repository.UserRepository;
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
    public CommandLineRunner dataLoader(UserRepository userRepo, PasswordEncoder encoder, SchoolRepository schoolRepo) { // user repo for ease of testing with a built-in user
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                User parent = new User("Parent_Test", encoder.encode("password"), "Craig Walls", "craig@walls.com", "123-123-1234");
                User employee = new User("Employee_Test", encoder.encode("password"), "Malte Petersen", "malte.petersen@web.de", "123-123-1234", "Geschichte", "1374816241982437", "Schloßstraße 33", false);
                User employeeSchoolCoordinator = new User("Employee_SchoolCoordinator_Test", encoder.encode("password"), "Malte Petersen", "malte.petersen11@gmail.com", "123-123-1234", "Geschichte", "1374816241982437", "Schloßstraße 33", true);
                User management = new User("Management_Test", encoder.encode("password"), "Malte Petersen", "malte.petersen@web.de", "123-123-1234", "Street 7");
                User teacher = new User("Teacher_Test", encoder.encode("password"), "Malte Petersen", "malte.petersen@web.de", "123-123-1234", "Geschichte", false);
                User teacherSchoolCoordinator = new User("Teacher_SchoolCoordinator_Test", encoder.encode("password"), "Malte Petersen", "malte.petersen@web.de", "123-123-1234", "Sport", true);
                User user = new User("User_Test", encoder.encode("password"), "Malte Petersen");
                User child = new User("Child_Test", encoder.encode("password"), "Malte Petersen", "7a");
                parent.setEnabled(true);
                employee.setEnabled(true);
                employeeSchoolCoordinator.setEnabled(true);
                teacher.setEnabled(true);
                teacherSchoolCoordinator.setEnabled(true);
                userRepo.save(parent);
                userRepo.save(employee);
                userRepo.save(employeeSchoolCoordinator);
                userRepo.save(management);
                userRepo.save(teacher);
                userRepo.save(teacherSchoolCoordinator);
                userRepo.save(user);
                userRepo.save(child);

                // adds a school for simpler testing
                School school = new School("Holstenschule", "Altonaer Str. 40, 24534 Neumünster");
                schoolRepo.save(school);
            }
        };
    }

}