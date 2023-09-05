package com.codesquad.secondhand.domain.member.controller;

import static com.codesquad.secondhand.common.util.RequestParser.*;

import java.util.Collections;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codesquad.secondhand.domain.member.dto.request.RegionRequest;
import com.codesquad.secondhand.domain.member.dto.request.SignupRequest;
import com.codesquad.secondhand.domain.member.dto.response.MemberRegionResponse;
import com.codesquad.secondhand.domain.member.service.MemberService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemberController {

	private final MemberService memberService;

	@PostMapping("/members/signup")
	public ResponseEntity signUp(@RequestBody @Valid SignupRequest signupRequest, HttpServletRequest request) {
		String email = extractEmail(request);
		memberService.signUp(signupRequest, email);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@PostMapping("/members/signout")
	public ResponseEntity<Map<String, String>> signOut(HttpServletRequest request) {
		String accessToken = extractAccessToken(request);
		Long memberId = extractMemberId(request);
		memberService.signOut(accessToken, memberId);
		return ResponseEntity.ok(Collections.singletonMap("message", "로그아웃 성공"));
	}

	@PostMapping("/members/{memberId}/regions")
	public ResponseEntity addRegion(@PathVariable Long memberId, @RequestBody RegionRequest regionRequest) {
		memberService.addRegion(memberId, regionRequest);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@DeleteMapping("/members/{memberId}/regions")
	public ResponseEntity deleteRegion(@PathVariable Long memberId, @RequestBody RegionRequest regionRequest) {
		memberService.deleteRegion(memberId, regionRequest);
		return ResponseEntity.noContent().build();
	}

	@PutMapping("/members/{memberId}/regions")
	public ResponseEntity updateSelectedRegion(@PathVariable Long memberId, @RequestBody RegionRequest regionRequest) {
		memberService.updateSelectedRegion(memberId, regionRequest);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/members/{memberId}/regions")
	public ResponseEntity<MemberRegionResponse> getRegions(@PathVariable Long memberId) {
		MemberRegionResponse memberRegionResponse = memberService.getRegion(memberId);
		return ResponseEntity.ok(memberRegionResponse);
	}

}
