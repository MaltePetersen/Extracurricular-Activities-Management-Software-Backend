package com.main.config;

import com.main.model.*;
import com.main.model.afterSchoolCare.AfternoonCare;
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
import java.lang.reflect.Array;
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

		createRoleIfNotFound(UserRole.ROLE_CHILD.toString(), Arrays.asList());
		createRoleIfNotFound(UserRole.ROLE_EMPLOYEE.toString(), Arrays.asList());

		createRoleIfNotFound(UserRole.ROLE_MANAGEMENT.toString(), Arrays.asList());

		createRoleIfNotFound(UserRole.ROLE_PARENT.toString(), Arrays.asList(resetChildPassword));


		createRoleIfNotFound(UserRole.ROLE_SCHOOLCOORDINATOR.toString(), Arrays.asList());
		createRoleIfNotFound(UserRole.ROLE_TEACHER.toString(), Arrays.asList());
		createRoleIfNotFound(UserRole.ROLE_USER.toString(), Arrays.asList(resetPasswordPrivilege));
		createRoleIfNotFound("ROLE_NEW_USER", Arrays.asList(resetTokenPrivilege));

		alreadySetup = true;
		createUsers();
		createSchools();
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
	void createSchools() {
		// adds schools for simpler testing
		School school1 = new School("Holstenschule", "Altonaer Str. 40, 24534 Neumünster");
		schoolRepo.save(school1);

		School school2 = new School("Klaus-Groth-Schule", "Winterbeker Weg 45, 24114 Kiel");
		schoolRepo.save(school2);

		School school3 = new School("Wilhelm-Tanck-Schule", "Färberstraße 25, 24534 Neumünster");
		schoolRepo.save(school3);
		AfternoonCare afternoonCare = new AfternoonCare();
		afternoonCare.setParticipatingSchool(school1);
		afterSchoolCareService.save(afternoonCare);

		Attendance attendance = new Attendance();
		attendance.setNote("Informations");
		attendance.setArrivalTime(LocalDateTime.now());
		attendance.setAfterSchoolCare(afternoonCare);
		attendanceRepository.save(attendance);

		afternoonCare.addAttendance(attendance);
		afterSchoolCareService.save(afternoonCare);

	}

	@Transactional
	void createUsers() {
		Map<User, String> userRole = userData.getUserData();
		userRole.forEach((user, role) -> {
			user.setRoles(Arrays.asList(roleRepository.findByName(UserRole.byRole(role).toString())));
			userRepository.save(user);
		});

	}

}
