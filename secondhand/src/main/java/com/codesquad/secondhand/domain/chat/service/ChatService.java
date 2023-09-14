package com.codesquad.secondhand.domain.chat.service;

import org.springframework.stereotype.Service;

import com.codesquad.secondhand.domain.chat.repository.ChatMemberJpaRepository;
import com.codesquad.secondhand.domain.chat.repository.ChatMessageJpaRepository;
import com.codesquad.secondhand.domain.chat.repository.ChatRoomJpaRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ChatService {

	private final ChatMemberJpaRepository memberJpaRepository;
	private final ChatMessageJpaRepository chatMessageJpaRepository;
	private final ChatRoomJpaRepository chatRoomJpaRepository;
}
