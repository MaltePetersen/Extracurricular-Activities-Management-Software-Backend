package com.main.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.main.model.Member;
import com.main.model.MyUser;
import com.main.repository.MemberRepository;
import com.main.utility.MemberRole;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private MemberRepository memberRepository;

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Member member = memberRepository.findByUsername(username).orElse(null);

		if (member == null)
			throw new UsernameNotFoundException(username);
		if( !member.isEnabled())
			throw new UsernameNotFoundException(username);
		
		
		Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
		for (MemberRole role : member.getRoles()) {
			grantedAuthorities.add(new SimpleGrantedAuthority(role.toString()));
		}

		return new MyUser(member, member.isEnabled(), true, true, true, grantedAuthorities);

	}

}
