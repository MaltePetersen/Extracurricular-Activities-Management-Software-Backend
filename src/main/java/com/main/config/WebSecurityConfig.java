package com.main.config;

import com.main.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.main.model.user.UserPrivilege;
import com.main.model.user.UserRole;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	private UserDetailsService userDetailsService;

	@Autowired
	WebSecurityConfig(@Qualifier("userDetailsServiceImpl") UserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
				// HTTP Basic authentication
				.httpBasic().and().authorizeRequests().antMatchers(HttpMethod.GET, "/api/parent/**")
				.hasAnyAuthority(UserRole.ROLE_PARENT.toString()).antMatchers(HttpMethod.POST, "/api/parent/**")
				.hasAnyAuthority(UserRole.ROLE_PARENT.toString()).antMatchers(HttpMethod.PUT, "/api/parent/**")
				.hasAnyAuthority(UserRole.ROLE_PARENT.toString()).antMatchers(HttpMethod.DELETE, "/api/parent/**")
				.hasAnyAuthority(UserRole.ROLE_PARENT.toString())

				.antMatchers(HttpMethod.GET, "/api/employee/**").hasAnyAuthority(UserRole.ROLE_EMPLOYEE.toString())
				.antMatchers(HttpMethod.POST, "/api/employee/**").hasAnyAuthority(UserRole.ROLE_EMPLOYEE.toString())
				.antMatchers(HttpMethod.PUT, "/api/employee/**").hasAnyAuthority(UserRole.ROLE_EMPLOYEE.toString())
				.antMatchers(HttpMethod.DELETE, "/api/employee/**").hasAnyAuthority(UserRole.ROLE_EMPLOYEE.toString())

				.antMatchers(HttpMethod.GET, "/api/management/**").hasAnyAuthority(UserRole.ROLE_MANAGEMENT.toString())
				.antMatchers(HttpMethod.POST, "/api/management/**").hasAnyAuthority(UserRole.ROLE_MANAGEMENT.toString())
				.antMatchers(HttpMethod.PUT, "/api/management/**").hasAnyAuthority(UserRole.ROLE_MANAGEMENT.toString())
				.antMatchers(HttpMethod.DELETE, "/api/management/**")
				.hasAnyAuthority(UserRole.ROLE_MANAGEMENT.toString())

				.antMatchers(HttpMethod.GET, "/api/child/**").hasAnyAuthority(UserRole.ROLE_CHILD.toString())
				.antMatchers(HttpMethod.POST, "/api/child/**").hasAnyAuthority(UserRole.ROLE_CHILD.toString())
				.antMatchers(HttpMethod.PUT, "/api/child/**").hasAnyAuthority(UserRole.ROLE_CHILD.toString())
				.antMatchers(HttpMethod.DELETE, "/api/child/**").hasAnyAuthority(UserRole.ROLE_CHILD.toString())

				.antMatchers(HttpMethod.GET, "/api/teacher/**").hasAnyAuthority(UserRole.ROLE_TEACHER.toString())
				.antMatchers(HttpMethod.POST, "/api/teacher/**").hasAnyAuthority(UserRole.ROLE_TEACHER.toString())
				.antMatchers(HttpMethod.PUT, "/api/teacher/**").hasAnyAuthority(UserRole.ROLE_TEACHER.toString())
				.antMatchers(HttpMethod.DELETE, "/api/teacher/**").hasAnyAuthority(UserRole.ROLE_TEACHER.toString())

				.antMatchers(HttpMethod.POST, "/user/register").permitAll().antMatchers(HttpMethod.GET, "/user/auth")
				.permitAll().antMatchers(HttpMethod.GET, "/user/login").permitAll()
				.antMatchers(HttpMethod.GET, "/user/registrationConfirm").permitAll()
				.antMatchers(HttpMethod.GET, "/user/profile").permitAll()
				.antMatchers(HttpMethod.GET, "/user/resendToken").hasAnyAuthority("ROLE_NEW_USER")
				.antMatchers(HttpMethod.GET, "/user/resetPassword")
				.hasAnyAuthority(UserPrivilege.RESET_PASSWORD.toString(), UserPrivilege.RESET_CHILD_PASSWORD.toString())
				.and().csrf().disable().formLogin().disable();
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