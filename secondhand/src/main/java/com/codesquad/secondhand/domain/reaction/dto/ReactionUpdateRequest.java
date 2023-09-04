package com.codesquad.secondhand.domain.reaction.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ReactionUpdateRequest {
	private Boolean isLiked;

	public ReactionUpdateRequest(Boolean isLiked) {
		this.isLiked = isLiked;
	}
}
