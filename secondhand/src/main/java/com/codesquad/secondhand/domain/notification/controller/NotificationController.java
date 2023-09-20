package com.codesquad.secondhand.domain.notification.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.codesquad.secondhand.domain.notification.NotificationService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class NotificationController {

	private final NotificationService notificationService;

	//todo 지금이야 @Pathvariable로 멤버아이디 받는데, 이거 accesstoken 으로 변경해야함
	@GetMapping(value = "/connect/{memberId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public ResponseEntity<SseEmitter> connect(@PathVariable Long memberId) {
		SseEmitter emitter = new SseEmitter(60 * 1000L);
		notificationService.add(emitter, memberId);
		return ResponseEntity.ok(emitter);
	}
}
