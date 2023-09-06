package com.codesquad.secondhand.domain.member.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 지역 등록, 삭제, 수정 에 쓰이는 request
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RegionRequest {
	private Long id;
}
