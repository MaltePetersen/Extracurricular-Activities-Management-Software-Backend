package com.main.config;

import com.main.model.*;
import com.main.model.afterSchoolCare.AfternoonCare;
import com.main.model.afterSchoolCare.Remedial;
import com.main.model.user.UserPrivilege;
import com.main.model.user.UserRole;
import com.main.repository.*;
import com.main.service.AfterSchoolCareService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;


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

	@Autowired
	public InitialDataLoader(UserRepository userRepository, RoleRepository roleRepository,
			PrivilegeRepository privilegeRepository, PasswordEncoder passwordEncoder, SchoolRepository schoolRepo,
			AfterSchoolCareService afterSchoolCareService, AttendanceRepository attendanceRepository,
			UserData userData) {
		super();
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.privilegeRepository = privilegeRepository;
		this.passwordEncoder = passwordEncoder;
		this.schoolRepo = schoolRepo;
		this.afterSchoolCareService = afterSchoolCareService;
		this.attendanceRepository = attendanceRepository;
		this.userData = userData;
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

		createRoleIfNotFound(UserRole.ROLE_CHILD.toString(), Collections.emptyList());
		createRoleIfNotFound(UserRole.ROLE_EMPLOYEE.toString(), Collections.emptyList());

		createRoleIfNotFound(UserRole.ROLE_MANAGEMENT.toString(), Collections.emptyList());

		createRoleIfNotFound(UserRole.ROLE_PARENT.toString(), Collections.singletonList(resetChildPassword));


		createRoleIfNotFound(UserRole.ROLE_SCHOOLCOORDINATOR.toString(), Collections.emptyList());
		createRoleIfNotFound(UserRole.ROLE_TEACHER.toString(), Collections.emptyList());
		createRoleIfNotFound(UserRole.ROLE_USER.toString(), Collections.singletonList(resetPasswordPrivilege));
		createRoleIfNotFound("ROLE_NEW_USER", Collections.singletonList(resetTokenPrivilege));

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
		schoolRepo.save(school1);

		School school2 = new School("Klaus-Groth-Schule", "Parkstraße 1, 24534 Neumünster");
		schoolRepo.save(school2);

		School school3 = new School("Wilhelm-Tanck-Schule", "Färberstraße 25, 24534 Neumünster");
		schoolRepo.save(school3);

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
		attendance3.setArrivalTime(LocalDateTime.of(2020, 4, 3, 12, 5));
		attendance3.setAfterSchoolCare(afternoonCare);
		attendance3.setChild(userRepository.findByUsername("Child_Test2"));
		attendanceRepository.save(attendance3);

		afternoonCare.addAttendance(attendance);
		afterSchoolCareService.save(afternoonCare);

		Remedial remedial = new Remedial();
		remedial.setName("Mathe Nachhilfe");
		remedial.setStartTime(LocalDateTime.of(2020, 4, 5, 11, 0));
		remedial.setEndTime(LocalDateTime.of(2020, 4, 5, 12, 15));
		remedial.setParticipatingSchool(school1);

		remedial.setOwner(userRepository.findByUsername("Employee_Test"));
		afterSchoolCareService.save(remedial);

		Attendance attendance2 = new Attendance();
		attendance2.setArrivalTime(LocalDateTime.of(2020, 4, 5, 10, 58));
		attendance2.setAfterSchoolCare(remedial);
		attendance2.setChild(userRepository.findByUsername("Child_Test"));
		attendanceRepository.save(attendance2);

		remedial.addAttendance(attendance);
		afterSchoolCareService.save(remedial);
	}

	@Transactional
	void createUsers() {
		Map<User, String> userRole = userData.getUserData();
		userRole.forEach((user, role) -> {
			user.setRoles(Arrays.asList(roleRepository.findByName(UserRole.byRole(role).toString())));
			userRepository.save(user);
		});

		/*User child = userRepository.findByUsername("Child_Test");
		User parent = userRepository.findByUsername("Parent_Test");
		child.setParent(parent);
		userRepository.save(child);
		parent.addChild(child);
		userRepository.save(parent);*/

	}

}
