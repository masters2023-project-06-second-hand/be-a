package com.codesquad.secondhand.common.filter;

import java.util.Set;
import java.util.regex.Pattern;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * whiteList를 체크할때 사용하는 패턴을 저장하는 객체이다.
 */
@Getter
@RequiredArgsConstructor
public class WhiteListUri {
	private final String pattern;
	private final Set<String> allowedMethods;

	public boolean matches(String url, String method) {
		return Pattern.matches(this.pattern, url) && allowedMethods.contains(method);
	}
}
