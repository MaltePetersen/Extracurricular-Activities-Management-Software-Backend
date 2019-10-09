package com.main.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import com.main.dto.MemberDTO;

@RestController
public class UserController {



    @PostMapping("/register")
    public ResponseEntity<Void> registration(@Valid @RequestBody MemberDTO memberForm, BindingResult bindingResult,
                                             WebRequest request) {

        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }


    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No Such Member")
    public class MemberNotFoundException extends RuntimeException {
        private static final long serialVersionUID = 10443642453485954L;

        public MemberNotFoundException() {
            super("No such Member found");
        }

    }
}
