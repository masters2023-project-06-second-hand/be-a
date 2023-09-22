package com.codesquad.secondhand.domain.product.dto.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ProductStatResponse {
	private Long chattingCount;
	private Long likeCount;
	private Long viewCount;
	private Boolean isLiked;

	public static ProductStatResponse of(Long viewCount, Long reactionCount, Long chattingCount, Boolean isLiked) {
		return ProductStatResponse.builder()
			.viewCount(viewCount)
			.likeCount(reactionCount)
			.chattingCount(chattingCount)
			.isLiked(isLiked)
			.build();
	}
}
