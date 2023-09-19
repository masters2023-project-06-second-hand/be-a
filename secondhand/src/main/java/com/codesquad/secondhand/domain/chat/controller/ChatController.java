package com.codesquad.secondhand.domain.chat.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

import com.codesquad.secondhand.domain.chat.dto.MessageRequest;
import com.codesquad.secondhand.domain.chat.service.ChatService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatController {

	private final SimpMessageSendingOperations simpMessageSendingOperations;
	private final ChatService chatService;

	@MessageMapping("/message")
	public void message(MessageRequest messageRequest) {
		chatService.sendMessage(messageRequest);
		// 채널아이디 만들기
		simpMessageSendingOperations.convertAndSend("/sub/channel/" + messageRequest.getChatRoomId(),
			messageRequest.getMessage());
	}
}
