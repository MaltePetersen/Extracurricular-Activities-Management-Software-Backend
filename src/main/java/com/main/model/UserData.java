package com.main.model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Veraltet, da auf Konstruktoren verzichtet werden sollte
 * 
 * @author Markus
 *
 */

@Component
@Deprecated
public class UserData {

	PasswordEncoder encoder;
	
	@Autowired
	public UserData(PasswordEncoder encoder) {
		this.encoder = encoder;
	}
	
	@Deprecated
	public Map<User, String> getUserData() {
		User parent = new User("Parent_Test", encoder.encode("password"), "Craig Walls", "craig@walls.com",
				"123-123-1234");
		
		User employee = new User("Employee_Test", encoder.encode("password"), "Malte Petersen", "malte.petersen@web.de",
				"123-123-1234", "Geschichte", "1374816241982437", "Schloßstraße 33", false);
		User employeeSchoolCoordinator = new User("Employee_SchoolCoordinator_Test", encoder.encode("password"),
				"Malte Petersen", "malte.petersen11@gmail.com", "123-123-1234", "Geschichte", "1374816241982437",
				"Schloßstraße 33", true);
		User management = new User("Management_Test", encoder.encode("password"), "Malte Petersen",
				"malte.petersen@web.de", "123-123-1234", "Street 7");
		User teacher = new User("Teacher_Test", encoder.encode("password"), "Malte Petersen", "malte.petersen@web.de",
				"123-123-1234", "Geschichte", false);
		User teacherSchoolCoordinator = new User("Teacher_SchoolCoordinator_Test", encoder.encode("password"),
				"Malte Petersen", "malte.petersen@web.de", "123-123-1234", "Sport", true);
		User user = new User("User_Test", encoder.encode("password"), "Malte Petersen");
		User child = new User("Child_Test", encoder.encode("password"), "Malte Petersen", "7a");
        parent.setVerified(true);
        employee.setVerified(true);
        employeeSchoolCoordinator.setVerified(true);
        teacher.setVerified(true);
        teacherSchoolCoordinator.setVerified(true);
        child.setVerified(true);
        management.setVerified(true);
        Map<User, String> users = new HashMap<>();
        users.put(employee, "EMPLOYEE");
        users.put(teacher, "TEACHER");
        users.put(user, "USER");
        users.put(child, "CHILD");
		users.put(management, "MANAGEMENT");
		return users;
	}
}
