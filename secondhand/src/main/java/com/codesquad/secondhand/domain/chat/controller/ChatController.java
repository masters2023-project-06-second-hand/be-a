package com.codesquad.secondhand.domain.chat.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

import com.codesquad.secondhand.domain.chat.dto.Message;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatController {

	private final SimpMessageSendingOperations simpMessageSendingOperations;

	@MessageMapping("/hello")
	public void message(Message message) {
		simpMessageSendingOperations.convertAndSend("/sub/channel/" + message.getChatRoomId(), message.getMessage());
	}
}
