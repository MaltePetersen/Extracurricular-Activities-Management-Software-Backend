package com.main.service.interfaces;

import com.main.model.IRole;
import com.main.model.Privilege;
import com.main.model.Role;
import com.main.model.User;
import com.main.model.interfaces.IPrivilege;
import com.main.repository.PrivilegeRepository;
import com.main.repository.RoleRepository;
import com.main.repository.UserRepository;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Log
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	public UserDetailsServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PrivilegeRepository privilegeRepository){
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.privilegeRepository = privilegeRepository;
	}

	private UserRepository userRepository;

	private RoleRepository roleRepository;

	private PrivilegeRepository privilegeRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		log.info(String.format("User with the username %s tries to login.", username));
		System.out.println( userRepository.findAll() );

		User user = userRepository.findByUsername(username);

		List<String> authorities = new ArrayList<>();
		List<Role> roles = roleRepository.findByUsers_Username(username);

		log.info("User " + username + " contains " + roles.toString() + " Roles");

		for (IRole role : roles) {
			List<Privilege> privileges = privilegeRepository.findByRoles_Name(role.getName());
			for (IPrivilege privilege : privileges) {
				authorities.add(privilege.getName());
			}
			authorities.add(role.getName());
		}

		List<SimpleGrantedAuthority> simpleGrantedAuthorities = authorities.stream().map(SimpleGrantedAuthority::new)
				.collect(Collectors.toList());

		log.info("User " + username + " contains " + simpleGrantedAuthorities + " Authorities");

		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
				true, true, true, true, simpleGrantedAuthorities);
	}

}
