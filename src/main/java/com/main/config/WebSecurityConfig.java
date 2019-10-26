package com.main.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.main.model.user.UserPrivilege;
import com.main.model.user.UserRole;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Qualifier("userDetailsServiceImpl")
	@Autowired
	private UserDetailsService userDetailsService;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
				// HTTP Basic authentication
				.httpBasic().and().authorizeRequests().antMatchers(HttpMethod.GET, "/api/**")
				.hasAnyAuthority(
						UserRole.ROLE_CHILD.toString(), 
						UserRole.ROLE_PARENT.toString(),
						UserRole.ROLE_EMPLOYEE.toString(),
						UserRole.ROLE_MANAGEMENT.toString(),
						UserRole.ROLE_SCHOOLCOORDINATOR.toString(), 
						UserRole.ROLE_TEACHER.toString(),
						UserRole.ROLE_USER.toString(),
						"READ_PRIVILEGE"
						)
				.antMatchers(HttpMethod.POST, "/register").permitAll().antMatchers(HttpMethod.GET, "/auth").permitAll()
				.antMatchers(HttpMethod.GET, "/registrationConfirm").permitAll()
				.antMatchers(HttpMethod.GET, "/resendToken").hasAnyAuthority("ROLE_NEW_USER")
				.and()
				.csrf().disable().formLogin().disable();
	}

	@Bean
	public PasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {

		auth.userDetailsService(userDetailsService).passwordEncoder(encoder());

	}

}
