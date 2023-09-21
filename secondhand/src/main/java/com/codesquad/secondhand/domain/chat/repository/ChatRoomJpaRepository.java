package com.codesquad.secondhand.domain.chat.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codesquad.secondhand.domain.chat.entity.ChatRoom;
import com.codesquad.secondhand.domain.product.entity.Product;

public interface ChatRoomJpaRepository extends JpaRepository<ChatRoom, Long> {
	Optional<ChatRoom> findByMemberIdAndProductId(Long memberId, Long productId);

	Long countByProduct(Product product);
}
