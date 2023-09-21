package com.codesquad.secondhand.redis.util;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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

	@Scheduled(cron = "* */3 * * * ?")
	@Transactional
	public void applyViewCountToDB() {
		Set<String> productKeys = redisViewTemplate.keys("product:*:current");

		for (String currentKey : productKeys) {
			String productIdStr = currentKey.split(":")[1];
			Long productId = Long.valueOf(productIdStr);

			Long viewCount = getViewCount(productId);

			if (!viewCount.equals(0L)) {
				productQueryService.applyViewCntToDB(productId, viewCount);

				// currentKey 에만 있는 value를 previousKey 에 복사한다.
				redisViewTemplate.opsForSet()
					.unionAndStore(currentKey, getCurrentSetKey(productId), getPreviousSetKey(productId));
				redisViewTemplate.delete(currentKey);
			}
		}
	}

	private static String getCurrentSetKey(Long productId) {
		return "product:" + productId + ":current";
	}

	private static String getPreviousSetKey(Long productId) {
		return "product:" + productId + ":previous";
	}
}
