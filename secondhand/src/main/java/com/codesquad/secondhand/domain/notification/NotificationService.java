package com.codesquad.secondhand.domain.notification;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class NotificationService {

	private final Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();

	public SseEmitter add(SseEmitter emitter, Long memberId) {
		this.emitters.put(memberId, emitter);

		//	더미 데이터를 보내는 이유는 Emitter 생성후 만료시간까지 아무런 데이터를 보내지 않으면 503 Error 발생가능성이 있다.
		try {
			emitter.send(SseEmitter.event()
				.name("connect") //todo 이벤트 이름 지정
				.data("connected!"));// 503 에러 방지를 위한 더미 데이터
		} catch (IOException e) {
			log.info("emitter send error");
		}

		setupEmitterCallbacks(emitter);
		return emitter;
	}

	private void setupEmitterCallbacks(SseEmitter emitter) {
		emitter.onTimeout(() -> {
			log.info("onTimeout callback");
			emitter.complete();
		});

		emitter.onCompletion(() -> {
			log.info("onCompletion callback");
			emitters.remove(emitter);
		});
	}

	public void refreshChatRoomList(Long receiverId) {
		SseEmitter emitter = emitters.get(receiverId);

		if (emitter == null) {
			return;
		}

		try {
			emitter.send(SseEmitter.event()
				.name("refreshChatRoomList")
				.data("refresh"));
		} catch (IOException e) {
			log.info("emitter send error");
		}
	}

}
