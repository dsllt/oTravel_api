package com.dsllt.oTravel_api.core.usecase.place;

import com.dsllt.oTravel_api.infra.dto.CustomPageDTO;
import com.dsllt.oTravel_api.infra.dto.place.CreatePlaceDTO;
import com.dsllt.oTravel_api.infra.dto.place.PlaceDTO;
import com.dsllt.oTravel_api.core.entity.place.Place;
import com.dsllt.oTravel_api.core.entity.place.PlaceFilter;
import com.dsllt.oTravel_api.core.entity.place.PlaceSpecification;
import com.dsllt.oTravel_api.infra.repository.PlaceRepository;
import com.dsllt.oTravel_api.core.exceptions.ObjectNotFoundException;
import com.dsllt.oTravel_api.core.exceptions.PlaceAlreadyExistsException;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PlaceServiceImpl implements PlaceService {

    private PlaceRepository placeRepository;

    public PlaceServiceImpl(PlaceRepository placeRepository){
        this.placeRepository = placeRepository;
    }

    @Override
    public PlaceDTO save(@Valid CreatePlaceDTO createPlaceDTO){
        if(placeRepository.existsByName(createPlaceDTO.name()) || placeRepository.existsBySlug(createPlaceDTO.slug())){
            throw new PlaceAlreadyExistsException("Lugar já cadastrado.");
        }
        Place newPlace = new Place(createPlaceDTO);
        Place savedPlace = placeRepository.save(newPlace);

        return PlaceDTO.from(savedPlace);
    }

    @Override
    public List<PlaceDTO> get() {
        List<Place> places = placeRepository.findAll();

        return places.stream()
                .map(PlaceDTO::from)
                .collect(Collectors.toList());
    }

    @Override
    public PlaceDTO getPlaceById(UUID placeUuid) {
        Place retrievedPlace = placeRepository.findById(placeUuid).orElseThrow(() -> new ObjectNotFoundException("Lugar não encontrado."));

        return PlaceDTO.from(retrievedPlace);
    }

    @Override
    public PlaceDTO updatePlace(UUID placeUuid, PlaceDTO placeDTO) {
        Place retrievedPlace = placeRepository.findById(placeUuid).orElseThrow(() -> new ObjectNotFoundException("Lugar não encontrado."));
        if(placeDTO.name() != null && !retrievedPlace.getName().isEmpty()){
            retrievedPlace.setName(placeDTO.name());
        }
        if(placeDTO.imageUrl() != null && !placeDTO.imageUrl().isBlank()){
            retrievedPlace.setImageUrl(placeDTO.imageUrl());
        }
        if(placeDTO.description() != null && !placeDTO.description().isBlank()){
            retrievedPlace.setDescription(placeDTO.description());
        }
        if(placeDTO.address() != null && !placeDTO.address().isEmpty()){
            retrievedPlace.setAddress(placeDTO.address());
        }
        if(placeDTO.latitude() != null && !placeDTO.latitude().isNaN()){
            retrievedPlace.setLatitude(placeDTO.latitude());
        }
        if(placeDTO.longitude() != null && !placeDTO.longitude().isNaN() ){
            retrievedPlace.setLongitude(placeDTO.longitude());
        }
        if(placeDTO.slug() != null && !placeDTO.slug().isBlank()){
            retrievedPlace.setSlug(placeDTO.slug());
        }
        if(placeDTO.phone() != null && !placeDTO.phone().isBlank()){
            retrievedPlace.setPhone(placeDTO.phone());
        }
        if(placeDTO.category() != null){
            retrievedPlace.setCategory(placeDTO.category());
        }
        if(placeDTO.rating() != null && !placeDTO.rating().isNaN()){
            retrievedPlace.setRating(placeDTO.rating());
        }

        Place savedPlace = placeRepository.save(retrievedPlace);

        return PlaceDTO.from(savedPlace);
    }

    @Override
    public CustomPageDTO<PlaceDTO> filter(PlaceFilter placeFilter, Pageable pageable) {
        Page<Place> placesPage = placeRepository.findAll(PlaceSpecification.withFilter(placeFilter), pageable);
        List<PlaceDTO> placesContent = placesPage.getContent()
                .stream()
                .map(PlaceDTO::from)
                .collect(Collectors.toList());

        return new CustomPageDTO<>(
                placesPage.getTotalPages(),
                placesPage.getTotalElements(),
                placesContent,
                placesPage.getNumber(),
                placesPage.getNumberOfElements(),
                placesPage.isFirst(),
                placesPage.isLast()
        );
    }

}
