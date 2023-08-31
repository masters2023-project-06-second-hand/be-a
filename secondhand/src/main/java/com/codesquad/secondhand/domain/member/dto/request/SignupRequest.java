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

	public Member toEntity(String email) {
		return Member.builder()
			.email(email)
			.nickname(this.getNickname())
			.profileImg(this.getProfileImg())
			.selectedRegion(this.getRegionsId().get(0))
			.build();
	}
}
