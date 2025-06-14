package com.dsllt.oTravel_api.infra.adapter.review.out.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ReviewJpaRepository extends JpaRepository<ReviewEntity, UUID> {
}
