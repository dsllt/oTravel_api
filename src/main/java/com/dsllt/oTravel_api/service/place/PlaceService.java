package com.dsllt.oTravel_api.service.place;

import com.dsllt.oTravel_api.dtos.CustomPageDTO;
import com.dsllt.oTravel_api.dtos.place.CreatePlaceDTO;
import com.dsllt.oTravel_api.dtos.place.PlaceDTO;
import com.dsllt.oTravel_api.entity.place.PlaceFilter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface PlaceService {
PlaceDTO save(@Valid CreatePlaceDTO createPlaceDTO);
List<PlaceDTO> get();
PlaceDTO getPlaceById(UUID placeUuid);
PlaceDTO updatePlace(UUID placeUuid, PlaceDTO placeDTO);

CustomPageDTO<PlaceDTO> filter(PlaceFilter placeFilter, Pageable pageable);
}
