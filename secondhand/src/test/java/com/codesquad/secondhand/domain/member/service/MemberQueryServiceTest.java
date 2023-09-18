package com.codesquad.secondhand.domain.member.service;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.codesquad.secondhand.annotation.ServiceIntegrationTest;
import com.codesquad.secondhand.domain.member.entity.Member;
import com.codesquad.secondhand.exception.CustomRuntimeException;

@ServiceIntegrationTest
class MemberQueryServiceTest {

	@Autowired
	MemberQueryService memberQueryService;

	@DisplayName(" id로 회원을 찾을 수 있다.")
	@Test
	void findByIdSuccess() {
		// given
		Long memberId = 1L;

		// when
		Member actual = memberQueryService.findById(memberId);

		// then
		assertThat(actual.getId()).isEqualTo(memberId);

	}

	@DisplayName("존재하지 않는 회원 id를 입력하면 예외가 발생한다.")
	@Test
	void findByIdFailed() {
		// given
		Long memberId = 155L;

		// when&then
		assertThatThrownBy(() -> memberQueryService.findById(memberId)).isInstanceOf(CustomRuntimeException.class);
	}

	@DisplayName("회원을 저장 한다.")
	@Test
	void signup() {
		// given
		Member member = Member.builder()
			.email("test@email.com")
			.nickname("service")
			.selectedRegion(1L)
			.profileImg("test")
			.build();

		// when
		memberQueryService.save(member);
		Member savedMember = memberQueryService.findById(member.getId());

		// then
		assertThat(savedMember.getEmail()).isEqualTo(member.getEmail());
		assertThat(savedMember.getNickname()).isEqualTo(member.getNickname());
		assertThat(savedMember.getSelectedRegion()).isEqualTo(member.getSelectedRegion());
		assertThat(savedMember.getProfileImg()).isEqualTo(member.getProfileImg());
	}

	@DisplayName("이메일로 member 객체를 찾아온다")
	@Test
	void findByEmail() {
		// given
		Long memberId = 1L;
		Member member = memberQueryService.findById(memberId);
		String email = member.getEmail();

		// when
		Member findMember = memberQueryService.findByEmail(email).get();

		// then
		assertThat(findMember.getId()).isEqualTo(member.getId());
		assertThat(findMember.getEmail()).isEqualTo(member.getEmail());
		assertThat(findMember.getSelectedRegion()).isEqualTo(member.getSelectedRegion());
	}
}

