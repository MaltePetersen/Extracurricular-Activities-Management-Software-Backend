package com.main.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.main.model.IRole;
import com.main.model.Privilege;
import com.main.model.Role;
import com.main.model.User;
import com.main.model.interfaces.IPrivilege;
import com.main.repository.PrivilegeRepository;
import com.main.repository.RoleRepository;
import com.main.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PrivilegeRepository privilegeRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username);

		if (user == null) {
			List<SimpleGrantedAuthority> authorities = new ArrayList<>();
			IRole role = roleRepository.findByName("ROLE_USER");
			List<Privilege> privileges = privilegeRepository.findByRoles_Name(role.getName());

			authorities.add(new SimpleGrantedAuthority(role.getName()));

			for (IPrivilege privilege : privileges) {
				authorities.add(new SimpleGrantedAuthority(privilege.getName()));
			}
			System.out.println(authorities);
			return new org.springframework.security.core.userdetails.User(" ", " ", true, true, true, true,
					authorities );
		}

		List<String> authorities = new ArrayList<>();
		List<Role> roles = roleRepository.findByUsers_Username(username);

		for (IRole role : roles) {
			List<Privilege> privileges = privilegeRepository.findByRoles_Name(role.getName());
			for (IPrivilege privilege : privileges) {
				authorities.add(privilege.getName());
			}
			authorities.add(role.getName());
		}

		List<SimpleGrantedAuthority> simpleGrantedAuthorities = authorities.stream().map(SimpleGrantedAuthority::new)
				.collect(Collectors.toList());
		
		org.springframework.security.core.userdetails.User userdetail = new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
				true, true, true, true, simpleGrantedAuthorities);
		
		System.out.println(userdetail);
		return userdetail;
	}

}
