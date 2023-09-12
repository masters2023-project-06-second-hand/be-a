package com.codesquad.secondhand.domain.jwt.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.codesquad.secondhand.domain.jwt.domain.Jwt;
import com.codesquad.secondhand.domain.member.entity.Member;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
public class Token {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;
	private String refreshToken;

	@Builder
	public Token(Member member, String refreshToken) {
		this.id = id;
		this.member = member;
		this.refreshToken = refreshToken;
	}

	public static Token of(Member member, Jwt jwt) {
		return Token.builder()
			.member(member)
			.refreshToken(jwt.getRefreshToken())
			.build();
	}
}
