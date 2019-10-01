package com.main.utility;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.main.dto.MemberDTO;
import com.main.model.Member;
import com.main.service.MemberService;

@Component
public class MemberValidator implements Validator {

	@Autowired
	private MemberService memberService;

	@Override
	public boolean supports(Class<?> clazz) {
		return Member.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		MemberDTO member = (MemberDTO) target;

		Optional<Member> oMember = memberService.findByUsername(member.getUsername());

		if (oMember.isPresent())
			errors.rejectValue("username", "username.duplicate");
		oMember = memberService.findByEmail(member.getEmail());
		if (oMember.isPresent())
			errors.rejectValue("email", "email.notduplicated");

		if (!member.isAgb())
			errors.rejectValue("agb", "agb.musttrue");

		if (member.getPassword() != null) {
			if (!member.getPassword().equals(member.getPasswordConfirm())) {
				errors.rejectValue("password", "password.notequal");
			}
		}else {
			errors.rejectValue("password", "password.null");
		}

	}

}
