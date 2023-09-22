package com.codesquad.secondhand.domain.chat.repository;

import static com.codesquad.secondhand.domain.chat.entity.QChatMessage.*;

import org.springframework.stereotype.Repository;

import com.codesquad.secondhand.domain.chat.entity.ChatMessage;
import com.codesquad.secondhand.domain.chat.entity.ChatRoom;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ChatMessageQueryRepository {
	private final JPAQueryFactory query;

	public ChatMessage findLatestMessage(ChatRoom chatRoom) {
		ChatMessage latestMessage = query
			.selectFrom(chatMessage)
			.where(chatMessage.chatRoom.eq(chatRoom))
			.orderBy(chatMessage.sendAt.desc())
			.fetchFirst();
		return latestMessage;
	}
}
