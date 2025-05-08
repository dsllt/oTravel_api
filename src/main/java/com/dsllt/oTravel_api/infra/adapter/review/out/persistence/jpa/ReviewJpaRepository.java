package com.dsllt.oTravel_api.infra.adapter.out.persistence.jpa;

import com.dsllt.oTravel_api.domain.model.review.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ReviewJpaRepository extends JpaRepository<Review, UUID>, JpaSpecificationExecutor<Review> {
}
