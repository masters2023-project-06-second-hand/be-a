package com.codesquad.secondhand.domain.chat.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;

import com.codesquad.secondhand.domain.member.entity.Member;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
public class ChatMessage {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "chat_room_id")
	private ChatRoom chatRoom;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;
	private String message;
	@Column(updatable = false)
	@CreationTimestamp
	private LocalDateTime sendAt;
	private Boolean isRead;

	@Builder
	public ChatMessage(ChatRoom chatRoom, Member member, String message, Boolean isRead) {
		this.chatRoom = chatRoom;
		this.member = member;
		this.message = message;
		this.isRead = isRead;
	}

	public static ChatMessage of(String message, ChatRoom chatRoom, Member sendMember) {
		return ChatMessage.builder()
			.message(message)
			.chatRoom(chatRoom)
			.member(sendMember)
			.build();
	}

	public void updateReadStatusToTrue() {
		this.isRead = true;
	}

	public boolean isSentByOpponent(Long participantId) {
		return !(this.getMember().getId().equals(participantId));
	}
}
