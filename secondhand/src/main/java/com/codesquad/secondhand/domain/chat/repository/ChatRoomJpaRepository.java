package com.codesquad.secondhand.domain.chat.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.codesquad.secondhand.domain.chat.entity.ChatRoom;
import com.codesquad.secondhand.domain.product.entity.Product;

public interface ChatRoomJpaRepository extends JpaRepository<ChatRoom, Long> {
	Optional<ChatRoom> findByMemberIdAndProductId(Long memberId, Long productId);
	@Query("SELECT DISTINCT cr FROM ChatRoom cr " +
		"LEFT JOIN cr.product p " +
		"LEFT JOIN cr.member m " +
		"WHERE m.id = :memberId OR p.member.id = :memberId")
	List<ChatRoom> findAllByMemberId(@Param("memberId") Long memberId);
	Long countByProduct(Product product);
}
