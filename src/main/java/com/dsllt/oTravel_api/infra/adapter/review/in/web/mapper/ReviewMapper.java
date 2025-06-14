package com.dsllt.oTravel_api.infra.adapter.review.in.web.mapper;

import com.dsllt.oTravel_api.domain.review.model.Review;
import com.dsllt.oTravel_api.infra.adapter.review.out.persistence.jpa.ReviewEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReviewMapper {

    Review toReview(ReviewEntity reviewEntity);
}
