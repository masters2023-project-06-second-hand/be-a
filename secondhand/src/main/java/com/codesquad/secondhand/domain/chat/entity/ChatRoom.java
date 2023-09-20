package com.codesquad.secondhand.domain.chat.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.codesquad.secondhand.domain.member.entity.Member;
import com.codesquad.secondhand.domain.product.entity.Product;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoom {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id")
	private Product product;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;

	@OneToMany(mappedBy = "chatRoom")
	private List<ChatMessage> chatMessages = new ArrayList<>();

	@Builder
	public ChatRoom(Long id, Product product, Member member, List<ChatMessage> chatMessages) {
		this.id = id;
		this.product = product;
		this.chatMessages = chatMessages;
		this.member = member;
	}

	public static ChatRoom of(Product product, Member member) {
		return ChatRoom.builder()
			.product(product)
			.member(member)
			.build();
	}

	public Long findOpponentId(Member sender) {
		Long productOwnerId = this.getProduct().getMember().getId();
		Long purchaserId = this.getMember().getId();

		return sender.getId().equals(productOwnerId) ? purchaserId : productOwnerId;
	}
}
