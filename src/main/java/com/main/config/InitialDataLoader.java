package com.main.config;

import com.main.model.*;
import com.main.model.afterSchoolCare.AfternoonCare;
import com.main.model.afterSchoolCare.Amplification;
import com.main.model.afterSchoolCare.Remedial;
import com.main.model.user.UserPrivilege;
import com.main.model.user.UserRole;
import com.main.repository.*;
import com.main.service.implementations.AfterSchoolCareService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


@Component
@Log
public class InitialDataLoader implements ApplicationListener<ContextRefreshedEvent> {

	boolean alreadySetup = false;

	private UserRepository userRepository;

	private RoleRepository roleRepository;

	private PrivilegeRepository privilegeRepository;

	private PasswordEncoder passwordEncoder;

	private SchoolRepository schoolRepo;

	private AfterSchoolCareService afterSchoolCareService;

	private AttendanceRepository attendanceRepository;

	private UserData userData;

	private PasswordEncoder encoder;

	@Autowired
	public InitialDataLoader(UserRepository userRepository, RoleRepository roleRepository,
			PrivilegeRepository privilegeRepository, PasswordEncoder passwordEncoder, SchoolRepository schoolRepo,
			AfterSchoolCareService afterSchoolCareService, AttendanceRepository attendanceRepository,
			UserData userData, PasswordEncoder encoder) {
		super();
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.privilegeRepository = privilegeRepository;
		this.passwordEncoder = passwordEncoder;
		this.schoolRepo = schoolRepo;
		this.afterSchoolCareService = afterSchoolCareService;
		this.attendanceRepository = attendanceRepository;
		this.userData = userData;
		this.encoder = encoder;
	}

	@Override
	@Transactional
	public void onApplicationEvent(ContextRefreshedEvent event) {

		if (alreadySetup)
			return;

		log.info("Start initializing the data.");

		Privilege resetTokenPrivilege = createPrivilegeIfNotFound(UserPrivilege.RESET_TOKEN.toString());
		Privilege resetPasswordPrivilege = createPrivilegeIfNotFound(UserPrivilege.RESET_PASSWORD.toString());
		Privilege resetChildPassword = createPrivilegeIfNotFound(UserPrivilege.RESET_CHILD_PASSWORD.toString());

		createRoleIfNotFound(UserRole.ROLE_CHILD.toString(), new ArrayList<>());
		createRoleIfNotFound(UserRole.ROLE_EMPLOYEE.toString(), new ArrayList<>());

		createRoleIfNotFound(UserRole.ROLE_MANAGEMENT.toString(), new ArrayList<>());

		createRoleIfNotFound(UserRole.ROLE_PARENT.toString(), new ArrayList<>( Arrays.asList(resetChildPassword) ));


		createRoleIfNotFound(UserRole.ROLE_SCHOOLCOORDINATOR.toString(), new ArrayList<>());
		createRoleIfNotFound(UserRole.ROLE_TEACHER.toString(), new ArrayList<>());
		createRoleIfNotFound(UserRole.ROLE_USER.toString(), new ArrayList<>( Arrays.asList( resetPasswordPrivilege )));
		createRoleIfNotFound("ROLE_NEW_USER", new ArrayList<>( Arrays.asList( resetTokenPrivilege)));

		alreadySetup = true;
		createUsers();
		createSchoolsAndAfterSchoolCares();
		log.info("The data has been initialized.");
	}

	@Transactional
	Privilege createPrivilegeIfNotFound(String name) {

		Privilege privilege = privilegeRepository.findByName(name);
		if (privilege == null) {
			privilege = new Privilege(name);
			privilegeRepository.save(privilege);
		}
		return privilege;
	}

	@Transactional
	IRole createRoleIfNotFound(String name, List<Privilege> privileges) {

		Role role = roleRepository.findByName(name);
		if (role == null) {
			role = new Role(name);
			if (privileges != null) {
				role.setPrivileges(privileges);
				roleRepository.save(role);
			}
		}
		return role;
	}

	@Transactional
	void createSchoolsAndAfterSchoolCares() {
		// adds schools for simpler testing
		School school1 = new School("Holstenschule", "Altonaer Str. 40, 24534 Neumünster");
		school1 = schoolRepo.save(school1);

		School school2 = new School("Klaus-Groth-Schule", "Parkstraße 1, 24534 Neumünster");
		schoolRepo.save(school2);

		School school3 = new School("Wilhelm-Tanck-Schule", "Färberstraße 25, 24534 Neumünster");
		schoolRepo.save(school3);

		//setChildSchools
		User user = userRepository.findByUsername("Child_Test");
		user.setChildSchool(school1);
		User user2 = userRepository.findByUsername("Child_Test2");
		user2.setChildSchool(school2);
		User user3 = userRepository.findByUsername("Child_Test3");
		user3.setChildSchool(school3);
		User user4 = userRepository.findByUsername("Child_Test4");
		user4.setChildSchool(school3);
		User user5 = userRepository.findByUsername("Child_Test5");
		user5.setChildSchool(school1);

		AfternoonCare afternoonCare = new AfternoonCare();
		afternoonCare.setName("Test-Nachmittagsbetreuung");
		afternoonCare.setStartTime(LocalDateTime.of(2020, 4, 3, 12, 0));
		afternoonCare.setEndTime(LocalDateTime.of(2020, 4, 3, 14, 30));
		afternoonCare.setParticipatingSchool(school1);
		afternoonCare.setOwner(userRepository.findByUsername("Employee_Test"));
		afterSchoolCareService.save(afternoonCare);

		Attendance attendance = new Attendance();
		attendance.setNote("Darf früher gehen");
		attendance.setArrivalTime(LocalDateTime.of(2020, 4, 3, 12, 2));
		attendance.setAfterSchoolCare(afternoonCare);
		attendance.setChild(userRepository.findByUsername("Child_Test"));
		attendanceRepository.save(attendance);

		Attendance attendance3 = new Attendance();
		attendance3.setLatestArrivalTime(LocalDateTime.of(2020, 4, 3, 12, 10));
		attendance3.setAfterSchoolCare(afternoonCare);
		attendance3.setChild(userRepository.findByUsername("Child_Test2"));
		attendanceRepository.save(attendance3);

		Attendance attendance5 = new Attendance();
		attendance5.setArrivalTime(LocalDateTime.of(2020, 4, 3, 12, 5));
		attendance5.setPredefinedLeaveTime(LocalDateTime.of(2020, 4, 3, 14, 0));
		attendance5.setAllowedToLeaveAfterFinishedHomework(true);
		attendance5.setAfterSchoolCare(afternoonCare);
		attendance5.setChild(userRepository.findByUsername("Child_Test4"));
		attendanceRepository.save(attendance5);

		afterSchoolCareService.save(afternoonCare);

		Remedial remedial = new Remedial();
		remedial.setName("Mathe Nachhilfe");
		remedial.setStartTime(LocalDateTime.of(2020, 4, 5, 11, 0));
		remedial.setEndTime(LocalDateTime.of(2020, 4, 5, 12, 15));
		remedial.setParticipatingSchool(school2);

		remedial.setOwner(userRepository.findByUsername("Employee_Test"));
		afterSchoolCareService.save(remedial);

		Attendance attendance2 = new Attendance();
		attendance2.setArrivalTime(LocalDateTime.of(2020, 4, 5, 10, 58));
		attendance2.setAfterSchoolCare(remedial);
		attendance2.setChild(userRepository.findByUsername("Child_Test"));
		attendanceRepository.save(attendance2);

		remedial.addAttendance(attendance);
		afterSchoolCareService.save(remedial);

		Amplification amplification = new Amplification();
		amplification.setName("Deutsch Verstärkung");
		amplification.setStartTime(LocalDateTime.of(2020, 6, 17, 10, 30));
		amplification.setEndTime(LocalDateTime.of(2020, 6, 17, 11, 30));
		amplification.setParticipatingSchool(school2);
		amplification.setOwner(userRepository.findByUsername("Employee_Test2"));
		afterSchoolCareService.save(amplification);

		Attendance attendance4 = new Attendance();
		attendance4.setAfterSchoolCare(amplification);
		attendance4.setChild(userRepository.findByUsername("Child_Test3"));
		attendanceRepository.save(attendance4);

		amplification.addAttendance(attendance4);
		afterSchoolCareService.save(amplification);
	}

	@Transactional
	void createUsers() {
		Map<User, String> userRole = userData.getUserData();
		userRole.forEach((user, role) -> {
			user.setRoles(Arrays.asList(roleRepository.findByName(UserRole.byRole(role).toString())));
			userRepository.save(user);
		});

		User parent = userRepository.findByUsername("Parent_Test");
		User child = userRepository.findByUsername("Child_Test");
		parent.addChild(child);
		child.setParent(parent);

		User parent2 = userRepository.findByUsername("Parent_Test2");
		User child2 = userRepository.findByUsername("Child_Test2");
		User child3 = userRepository.findByUsername("Child_Test3");
		User child4 = userRepository.findByUsername("Child_Test4");

		parent2.addChild(child2);
		parent2.addChild(child3);
		parent2.addChild(child4);

		child2.setParent(parent2);
		child3.setParent(parent2);
		child4.setParent(parent2);
	}

}
