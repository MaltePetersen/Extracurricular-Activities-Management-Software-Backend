package com.main.model.userTypes;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Deprecated
public class UserData {

	PasswordEncoder encoder;
	
	@Autowired
	public UserData(PasswordEncoder encoder) {
		this.encoder = encoder;
	}
	
	@Deprecated
	public List<User> getUserData() {
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
        parent.setEnabled(true);
        employee.setEnabled(true);
        employeeSchoolCoordinator.setEnabled(true);
        teacher.setEnabled(true);
        teacherSchoolCoordinator.setEnabled(true);
		return Arrays.asList(parent, employee, employeeSchoolCoordinator, management, teacher, teacherSchoolCoordinator, user, child);

	}
}
