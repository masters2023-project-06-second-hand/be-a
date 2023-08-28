package com.codesquad.secondhand.domain.product.dto.response;

import com.codesquad.secondhand.domain.member.entity.Member;

import lombok.Getter;

@Getter
public class Writer {
	private Long id;
	private String nickname;

	public Writer(Long id, String nickname) {
		this.id = id;
		this.nickname = nickname;
	}

	public static Writer from(Member member) {
		return new Writer(member.getId(), member.getNickname());
	}
}
