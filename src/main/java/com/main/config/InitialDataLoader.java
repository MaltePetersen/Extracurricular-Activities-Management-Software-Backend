package com.main.config;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.main.model.AfterSchoolCare;
import com.main.model.IRole;
import com.main.model.Privilege;
import com.main.model.Role;
import com.main.model.School;
import com.main.model.User;
import com.main.model.UserData;
import com.main.model.interfaces.IPrivilege;
import com.main.model.user.UserPrivilege;
import com.main.model.user.UserRole;
import com.main.repository.AfterSchoolCareRepository;
import com.main.repository.PrivilegeRepository;
import com.main.repository.RoleRepository;
import com.main.repository.SchoolRepository;
import com.main.repository.UserRepository;

import lombok.extern.java.Log;

@Component
@Log
public class InitialDataLoader implements ApplicationListener<ContextRefreshedEvent> {

	boolean alreadySetup = false;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PrivilegeRepository privilegeRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private SchoolRepository schoolRepo;

	@Autowired
	private AfterSchoolCareRepository afterSchoolCareService;

	@Autowired
	private UserData userData;

	@Override
	@Transactional
	public void onApplicationEvent(ContextRefreshedEvent event) {

		if (alreadySetup)
			return;

		log.info("Start initializing the data.");

		Privilege writePrivilege = createPrivilegeIfNotFound("WRITE_PRIVILEGE");
		IPrivilege resetTokenPrivilege = createPrivilegeIfNotFound(UserPrivilege.RESET_TOKEN.toString());
		Privilege resetPassword = createPrivilegeIfNotFound(UserPrivilege.RESET_PASSWORD.toString());

		List<Privilege> adminPrivileges = Arrays.asList(writePrivilege);
		createRoleIfNotFound("ROLE_ADMIN", adminPrivileges);
		createRoleIfNotFound("ROLE_USER", null);
		createRoleIfNotFound(UserRole.ROLE_CHILD.toString(), Arrays.asList());
		createRoleIfNotFound(UserRole.ROLE_EMPLOYEE.toString(), Arrays.asList());
		createRoleIfNotFound(UserRole.ROLE_MANAGEMENT.toString(), Arrays.asList());
		createRoleIfNotFound(UserRole.ROLE_PARENT.toString(), Arrays.asList());
		createRoleIfNotFound(UserRole.ROLE_SCHOOLCOORDINATOR.toString(), Arrays.asList());
		createRoleIfNotFound(UserRole.ROLE_TEACHER.toString(), Arrays.asList());
		createRoleIfNotFound(UserRole.ROLE_USER.toString(), null);

		createRoleIfNotFound("ROLE_NEW_USER", Arrays.asList(resetPassword));

		alreadySetup = true;
		createUsers();
		createSchools();

		log.info("The data has been initialized.");
	}

	@Transactional
	private Privilege createPrivilegeIfNotFound(String name) {

		Privilege privilege = privilegeRepository.findByName(name);
		if (privilege == null) {
			privilege = new Privilege(name);
			privilegeRepository.save(privilege);
		}
		return privilege;
	}

	@Transactional
	private IRole createRoleIfNotFound(String name, List<Privilege> privileges) {

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
	private void createSchools() {
		// adds schools for simpler testing
		School school1 = new School("Holstenschule", "Altonaer Str. 40, 24534 Neumünster");
		schoolRepo.save(school1);

		School school2 = new School("Klaus-Groth-Schule", "Winterbeker Weg 45, 24114 Kiel");
		schoolRepo.save(school2);

		School school3 = new School("Wilhelm-Tanck-Schule", "Färberstraße 25, 24534 Neumünster");
		schoolRepo.save(school3);

		AfterSchoolCare afterSchoolCare = new AfterSchoolCare();
		afterSchoolCare.setParticipatingSchool(school1);
		afterSchoolCareService.save(afterSchoolCare);
	}

	@Transactional
	private void createUsers() {

		Role adminRole = roleRepository.findByName("ROLE_ADMIN");

		for (User user : userData.getUserData()) {
			user.setRoles(Arrays.asList(adminRole));
			userRepository.save(user);
		}

	}

}
