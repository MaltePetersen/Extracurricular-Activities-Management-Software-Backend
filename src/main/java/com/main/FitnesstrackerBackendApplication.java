package com.main;

import java.util.Arrays;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.main.model.Activeness;
import com.main.model.Member;
import com.main.repository.ActivenessRepository;
import com.main.repository.MemberRepository;
import com.main.utility.MemberRole;

@SpringBootApplication
public class FitnesstrackerBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(FitnesstrackerBackendApplication.class, args);
	}

	@Autowired
	public BCryptPasswordEncoder encoder;

	@Bean
	CommandLineRunner runner(MemberRepository memberRepository, ActivenessRepository activenessRepository) {
		return args -> {
			Member user = new Member(new HashSet<>(Arrays.asList(MemberRole.ROLE_USER, MemberRole.ROLE_ADMIN)),
					"oberstrike", encoder.encode("mewtu123"), "markus.juergens@gmx.de");
			user.setEnabled(true);
			memberRepository.save(user);
			activenessRepository.save(new Activeness(0, "Test", "Date"));

		};
	}

}
