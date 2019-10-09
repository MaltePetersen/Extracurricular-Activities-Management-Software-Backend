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
import org.springframework.security.crypto.password.StandardPasswordEncoder;

@Configuration
public class WebSecurityConfig  extends WebSecurityConfigurerAdapter {

    @Qualifier("userDetailsServiceImpl")
    @Autowired
        private UserDetailsService userDetailsService;

        @Override
        protected void configure(HttpSecurity http) throws Exception {

            http
                    //HTTP Basic authentication
                    .httpBasic()
                    .and()
                    .authorizeRequests()
                    .antMatchers(HttpMethod.GET, "/api/**").hasRole("ADMIN")
                    .antMatchers(HttpMethod.POST, "/register").hasRole("ADMIN")
                    .and()
                    .csrf().disable()
                    .formLogin().disable();
        }
        @Bean
        public PasswordEncoder encoder() {
            return new BCryptPasswordEncoder();
        }


        @Override
        protected void configure(AuthenticationManagerBuilder auth)
                throws Exception {

            auth
                    .userDetailsService(userDetailsService)
                    .passwordEncoder(encoder());

        }

    }
