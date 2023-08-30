package com.codesquad.secondhand.domain.member.dto.request;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.codesquad.secondhand.domain.member.entity.Member;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SignupRequest {
	@NotBlank(message = "이름을 입력해 주세요.")
	private String nickname;
	private String profileImg;
	@NotNull(message = "필수로 하나의 지역은 선택 해야 합니다.")
	private List<Long> regionsId;
	public static final String TEST_EMAIL = "test@naver.com";

	public Member toEntity() {
		return Member.builder()
			.email(TEST_EMAIL)
			.nickname(this.getNickname())
			.profileImg(this.getProfileImg())
			.selectedRegion(this.getRegionsId().get(0))
			.build();
	}
}
