package com.codesquad.secondhand.redis.util;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Component;

import com.codesquad.secondhand.domain.product.service.ProductQueryService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RedisUtil {

	private final RedisTemplate<String, Object> redisBlackListTemplate;
	private final RedisTemplate<String, Object> redisViewTemplate;
	private final ProductQueryService productQueryService;

	public void setBlackList(String key, Object o, int minutes) {
		//Redis에 저장할 데이터 방식을 설정
		redisBlackListTemplate.setValueSerializer(new Jackson2JsonRedisSerializer(o.getClass()));
		//Redis의 String타입 구조에 작업을 수행하기 위한 연산
		redisBlackListTemplate.opsForValue().set(key, o, minutes, TimeUnit.MINUTES);
	}

	public boolean hasKeyBlackList(String key) {
		return Boolean.TRUE.equals(redisBlackListTemplate.hasKey(key));
	}

	public void addViewCount(Long productId, Long memberId) {
		String currentSetKey = getCurrentSetKey(productId);
		redisViewTemplate.opsForSet().add(currentSetKey, String.valueOf(memberId));
	}

	//RDB 에 apply 이후 redis에 저장된 count 수 반환
	public Long getViewCount(Long productId) {
		String currentSetKey = getCurrentSetKey(productId);
		String previousSetKey = getPreviousSetKey(productId);

		Set<Object> newMemberIds = redisViewTemplate.opsForSet().difference(currentSetKey, previousSetKey);

		return Long.valueOf(newMemberIds.size());
	}

	/**
	 * 조회수 로직에 사용되는 메서드이다.
	 * current에 있는 값들을 previous로 union 한후  current를 삭제한다.
	 *
	 * @param currentKey
	 * @param productId
	 */
	public void copyCurrentToPreviousAndDeleteCurrent(String currentKey, Long productId) {
		redisViewTemplate.opsForSet()
			.unionAndStore(currentKey, getCurrentSetKey(productId), getPreviousSetKey(productId));
		redisViewTemplate.delete(currentKey);
	}

	/**
	 * 조회수 로직에 사용되는 메서드이다.
	 * key가 product:*:current 형태인 모든 key 를 return 한다.
	 *
	 * @return
	 */
	public Set<String> getCurrentProductKeys() {
		Set<String> productKeys = redisViewTemplate.keys("product:*:current");
		return productKeys;
	}

	private static String getCurrentSetKey(Long productId) {
		return "product:" + productId + ":current";
	}

	private static String getPreviousSetKey(Long productId) {
		return "product:" + productId + ":previous";
	}
}
