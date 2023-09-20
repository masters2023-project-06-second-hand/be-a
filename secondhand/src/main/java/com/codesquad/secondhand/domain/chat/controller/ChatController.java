package com.codesquad.secondhand.domain.chat.controller;

import static com.codesquad.secondhand.common.util.RequestParser.*;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.codesquad.secondhand.domain.chat.dto.request.ChatRequest;
import com.codesquad.secondhand.domain.chat.dto.request.MessageRequest;
import com.codesquad.secondhand.domain.chat.dto.response.ChatRoomDetailsResponse;
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
	public void sendMessage(MessageRequest messageRequest) {
		chatService.sendMessage(messageRequest);
		// 채널아이디 만들기
		simpMessageSendingOperations.convertAndSend("/sub/channel/" + messageRequest.getChatRoomId(),
			messageRequest.getMessage());
	}

	@GetMapping("/api/chats")
	public ResponseEntity<ChatRoomDetailsResponse> getChatRoom(@RequestBody ChatRequest chatRequest,
		HttpServletRequest request) {
		Long memberId = extractMemberId(request);
		ChatRoomDetailsResponse chatRoomDetailsResponse = chatService.getChatRoom(chatRequest, memberId);
		return ResponseEntity.ok(chatRoomDetailsResponse);
	}

}
