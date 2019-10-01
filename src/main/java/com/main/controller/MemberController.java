package com.main.controller;

import java.security.Principal;
import java.util.Calendar;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.main.dto.MemberDTO;
import com.main.model.Member;
import com.main.model.VerificationToken;
import com.main.service.MemberService;
import com.main.utility.MemberValidator;
import com.main.utility.OnRegistrationCompleteEvent;

@RestController
@PreAuthorize("hasRole('ROLE_ANONYMOUS')")
public class MemberController {

	@Autowired
	private MemberService memberService;

	@Autowired
	private MemberValidator memberValidator;

	@Autowired
	private ApplicationEventPublisher eventPublisher;

	@PostMapping("/register")
	public ResponseEntity<Void> registration(@Valid @RequestBody MemberDTO memberForm, BindingResult bindingResult,
			WebRequest request) {
		memberValidator.validate(memberForm, bindingResult);
		String password = memberForm.getPassword();
		String confirmPassword = memberForm.getPasswordConfirm();

		if (!password.equals(confirmPassword))
			bindingResult.addError(new ObjectError("password", "password.notequal"));
		HttpHeaders headers = new HttpHeaders();
		
		if (bindingResult.hasErrors()) {
			headers.add("Successfull", "False");
			return new ResponseEntity<Void>(headers, HttpStatus.UNAUTHORIZED );
		}

		Member member = memberService.createAndRegister(memberForm);
		try {
			String appUrl = request.getContextPath();
			eventPublisher.publishEvent(new OnRegistrationCompleteEvent(member, request.getLocale(), appUrl));
		} catch (Exception e) {
			e.printStackTrace();
		}
		headers.add("Successfull", "True");
		
		return new ResponseEntity<Void>(headers, HttpStatus.ACCEPTED);
	}

	@GetMapping("/registrationConfirm")
	public ResponseEntity<Void> confirmRegistration(WebRequest request, @RequestParam("token") String token,
			RedirectAttributes attributes) {
		HttpHeaders headers = new HttpHeaders();
		
		VerificationToken myToken = memberService.getToken(token);
		if (myToken == null) {

			headers.add("Successfull", "False");
			return new ResponseEntity<Void>(headers, HttpStatus.UNAUTHORIZED );
		}

		Member member = myToken.getMember();
		Calendar cal = Calendar.getInstance();
		if ((myToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
			headers.add("Successfull", "False");
			return new ResponseEntity<Void>(headers, HttpStatus.UNAUTHORIZED );
		}

		member.setEnabled(true);
		memberService.refresh(member);
		headers.add("Successfull", "True");
		return new ResponseEntity<Void>(headers, HttpStatus.ACCEPTED );
	}

	@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No Such Member")
	public class MemberNotFoundException extends RuntimeException {
		private static final long serialVersionUID = 10443642453485954L;

		public MemberNotFoundException() {
			super("No such Member found");
		}

	}
}
