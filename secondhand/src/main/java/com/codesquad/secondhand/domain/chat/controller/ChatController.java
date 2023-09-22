package com.codesquad.secondhand.domain.chat.controller;

import static com.codesquad.secondhand.common.util.RequestParser.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.codesquad.secondhand.domain.chat.dto.request.ChatRequest;
import com.codesquad.secondhand.domain.chat.dto.request.MessageRequest;
import com.codesquad.secondhand.domain.chat.dto.response.ChatRoomDetailsResponse;
import com.codesquad.secondhand.domain.chat.dto.response.ChatRoomListResponse;
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
		simpMessageSendingOperations.convertAndSend("/sub/room/" + messageRequest.getChatRoomId(),
			messageRequest.getMessage());
	}

	@GetMapping("/api/chats/room-id")
	public ResponseEntity<Map<String, Long>> getChatRoom(@RequestBody ChatRequest chatRequest,
		HttpServletRequest request) {
		Long participantId = extractMemberId(request);
		Long chatRoomId = chatService.getChatRoom(chatRequest, participantId);
		return ResponseEntity.ok(Collections.singletonMap("chatRoomId", chatRoomId));
	}

	@GetMapping("/api/chats/{chatRoomId}")
	public ResponseEntity<ChatRoomDetailsResponse> getChatDetails(@PathVariable Long chatRoomId,
		HttpServletRequest request) {
		Long participantId = extractMemberId(request);
		ChatRoomDetailsResponse chatRoomDetailsResponse = chatService.getChatRoomDetail(chatRoomId, participantId);
		return ResponseEntity.ok(chatRoomDetailsResponse);
	}

	@GetMapping("/api/members/{memberId}/chats")
	public ResponseEntity<List<ChatRoomListResponse>> getChatRoomList(@PathVariable Long memberId) {
		List<ChatRoomListResponse> response = chatService.getChatRoomList(memberId);
		return ResponseEntity.ok(response);
	}

}
