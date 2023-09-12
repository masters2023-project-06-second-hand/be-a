package com.codesquad.secondhand.domain.reaction.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ReactionUpdateRequest {
	@JsonProperty("isLiked")
	private Boolean isLiked;

	public ReactionUpdateRequest(Boolean isLiked) {
		this.isLiked = isLiked;
	}

	public Boolean isLiked() {
		return isLiked;
	}
}
