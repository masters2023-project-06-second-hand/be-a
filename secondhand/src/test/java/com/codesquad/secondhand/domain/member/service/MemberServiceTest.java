package com.codesquad.secondhand.domain.member.service;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import com.codesquad.secondhand.annotation.ServiceIntegrationTest;
import com.codesquad.secondhand.domain.jwt.repository.TokenJpaRepository;
import com.codesquad.secondhand.domain.member.dto.request.SignupRequest;
import com.codesquad.secondhand.domain.member.entity.Member;
import com.codesquad.secondhand.domain.member_region.entity.MemberRegion;
import com.codesquad.secondhand.domain.member_region.service.MemberRegionQueryService;
import com.codesquad.secondhand.exception.CustomRuntimeException;

@ServiceIntegrationTest
class MemberServiceTest {

	@Autowired
	MemberService memberService;

	@Autowired
	MemberQueryService memberQueryService;

	@Autowired
	MemberRegionQueryService memberRegionQueryService;

	@Autowired
	RedisTemplate<String, Object> redisBlackListTemplate;

	@Autowired
	TokenJpaRepository tokenJpaRepository;

	@AfterEach
	void resetBlackList() {
		redisBlackListTemplate.delete(
			"eyJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6InRlc3RAdGVzdC5jb20iLCJtZW1iZXJJZCI6MSwiY3JlYXRlZE1pbGxpcyI6MTY5Mzc5NjE4NTE0NiwiZXhwIjoxNzI1MzMyMTg1fQ.QKKVe4mEOOO8hzVsQ5xyDghyQluxhOAWyNfV3vIJSfI");
	}

	@DisplayName("회원가입에 성공한다.")
	@Test
	void signUpSuccess() {
		// given
		List<Long> ids = List.of(1L, 2L);
		SignupRequest signupRequest = new SignupRequest("test", "test", ids);
		String requestEmail = "test@email.com";

		// when
		memberService.signUp(signupRequest, requestEmail);

		// then
		Member savedMember = memberQueryService.findByEmail(requestEmail).get();
		List<MemberRegion> memberRegions = memberRegionQueryService.findAllMemberRegion(savedMember.getId());

		assertThat(savedMember.getEmail()).isEqualTo(requestEmail);
		assertThat(savedMember.getNickname()).isEqualTo(signupRequest.getNickname());
		assertThat(savedMember.getProfileImg()).isEqualTo(signupRequest.getProfileImg());
		assertThat(memberRegions.get(0).getRegion().getId()).isEqualTo(signupRequest.getRegionsId().get(0));
	}

	@DisplayName("중복된 닉네임 입력시 회원가입에 실패한다 ")
	@Test
	void signUpFailed() {
		// given
		List<Long> ids = List.of(1L, 2L);
		SignupRequest signupRequest = new SignupRequest("test", "test", ids);
		String requestEmail = "test@email.com";
		memberService.signUp(signupRequest, requestEmail);
		String savedMemberNickname = memberQueryService.findByEmail(requestEmail).get().getNickname();
		SignupRequest existNicknameRequest = new SignupRequest(savedMemberNickname, "test", ids);

		// when & then
		assertThatThrownBy(() -> memberService.signUp(existNicknameRequest, requestEmail)).isInstanceOf(
			CustomRuntimeException.class);
	}
}
