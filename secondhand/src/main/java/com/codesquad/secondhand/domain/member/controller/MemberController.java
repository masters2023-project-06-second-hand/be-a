package com.codesquad.secondhand.domain.member.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codesquad.secondhand.domain.member.dto.request.SignupRequest;
import com.codesquad.secondhand.domain.member.service.MemberService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemberController {

	private final MemberService memberService;

	@PostMapping("/members/signup")
	public ResponseEntity signUp(@RequestBody @Valid SignupRequest signupRequest) {
		memberService.signUp(signupRequest);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

}
