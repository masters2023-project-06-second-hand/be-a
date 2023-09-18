package com.codesquad.secondhand.domain.member.controller;

import static com.codesquad.secondhand.common.util.RequestParser.*;

import java.util.Collections;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codesquad.secondhand.domain.jwt.dto.request.ReissueTokenRequest;
import com.codesquad.secondhand.domain.jwt.dto.response.ReissueTokenResponse;
import com.codesquad.secondhand.domain.jwt.service.JwtService;
import com.codesquad.secondhand.domain.member.dto.request.SignupRequest;
import com.codesquad.secondhand.domain.member.dto.response.MemberInfoResponse;
import com.codesquad.secondhand.domain.member.dto.response.SignUpResponse;
import com.codesquad.secondhand.domain.member.service.MemberService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemberController {

	private final MemberService memberService;
	private final JwtService jwtService;

	@PostMapping("/members/signup")
	public ResponseEntity<SignUpResponse> signUp(@RequestBody @Valid SignupRequest signupRequest,
		HttpServletRequest request) {
		String email = extractEmail(request);
		SignUpResponse signUpResponse = memberService.signUp(signupRequest, email);
		return ResponseEntity.status(HttpStatus.CREATED).body(signUpResponse);
	}

	@PostMapping("/members/signout")
	public ResponseEntity<Map<String, String>> signOut(HttpServletRequest request) {
		String accessToken = extractAccessToken(request);
		Long memberId = extractMemberId(request);
		memberService.signOut(accessToken, memberId);
		return ResponseEntity.ok(Collections.singletonMap("message", "로그아웃 성공"));
	}

	//todo 이럴때 토큰에 있는 memberId랑 해당 memberId랑 같은지 수정해야함
	@GetMapping("/members/{memberId}")
	public ResponseEntity<MemberInfoResponse> getMemberInfo(@PathVariable Long memberId) {
		return ResponseEntity.ok().body(memberService.getMemberInfo(memberId));
	}

	@PostMapping("/oauth2/token")
	public ResponseEntity<ReissueTokenResponse> reissueToken(@RequestBody ReissueTokenRequest reissueTokenRequest) {
		return ResponseEntity.ok().body(jwtService.reissueToken(reissueTokenRequest));
	}

}
