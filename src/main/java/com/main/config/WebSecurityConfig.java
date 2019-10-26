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

import com.main.model.userTypes.UserAuthority;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Qualifier("userDetailsServiceImpl")
    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // HTTP Basic authentication
                .httpBasic().and().authorizeRequests().antMatchers("/api/**")
                .hasAnyAuthority(
                        UserAuthority.ROLE_CHILD.toString(),
                        UserAuthority.ROLE_PARENT.toString(),
                        UserAuthority.ROLE_EMPLOYEE.toString(),
                        UserAuthority.ROLE_MANAGEMENT.toString(),
                        UserAuthority.ROLE_SCHOOLCOORDINATOR.toString(),
                        UserAuthority.ROLE_TEACHER.toString(),
                        UserAuthority.ROLE_USER.toString()
                )
                .antMatchers("/api/admin/**").hasAnyAuthority(UserAuthority.ROLE_MANAGEMENT.toString())
                .antMatchers(HttpMethod.POST, "/register").permitAll().antMatchers(HttpMethod.GET, "/auth").permitAll()
                .antMatchers(HttpMethod.GET, "/registrationConfirm").permitAll()
                .antMatchers(HttpMethod.GET, "/resendToken").hasAnyAuthority(UserAuthority.RESET_TOKEN.toString())
                .antMatchers(HttpMethod.POST, "/login").authenticated()
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
