package com.codesquad.secondhand.domain.region.repository;

import com.codesquad.secondhand.domain.product.entity.Product;
import com.codesquad.secondhand.domain.region.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegionJpaRepository extends JpaRepository<Region,Long> {
}
