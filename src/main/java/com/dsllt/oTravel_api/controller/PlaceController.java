package com.dsllt.oTravel_api.controller;

import com.dsllt.oTravel_api.dtos.CustomPageDTO;
import com.dsllt.oTravel_api.dtos.enums.PlaceCategory;
import com.dsllt.oTravel_api.dtos.place.CreatePlaceDTO;
import com.dsllt.oTravel_api.dtos.place.PlaceDTO;
import com.dsllt.oTravel_api.entity.place.Place;
import com.dsllt.oTravel_api.entity.place.PlaceFilter;
import com.dsllt.oTravel_api.service.place.PlaceService;
import jakarta.annotation.Nonnull;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/place")
public class PlaceController {

    private PlaceService placeService;

    public PlaceController(PlaceService placeService){
        this.placeService = placeService;
    }

    @PostMapping
    public ResponseEntity<PlaceDTO> create(@RequestBody @Valid CreatePlaceDTO createPlaceDTO, UriComponentsBuilder uriComponentsBuilder){
        PlaceDTO newPlace = placeService.save(createPlaceDTO);

        var uri = uriComponentsBuilder.path("/api/v1/place/{placeUuid}").buildAndExpand(newPlace.id()).toUri();

        return ResponseEntity.created(uri).body(newPlace);
    }

    @GetMapping
    public  ResponseEntity<List<PlaceDTO>> get(){
        List<PlaceDTO> retrievedPlaces = placeService.get();

        return ResponseEntity.ok().body(retrievedPlaces);
    }

    @GetMapping("/{placeUuid}")
    public  ResponseEntity<PlaceDTO> getPlaceById(@Nonnull @PathVariable UUID placeUuid){
        PlaceDTO retrievedPlace = placeService.getPlaceById(placeUuid);

        return ResponseEntity.ok().body(retrievedPlace);
    }

    @GetMapping("/filter")
    public  ResponseEntity<CustomPageDTO<PlaceDTO>> filter(
            @RequestParam(required = false) UUID placeUuid,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) List<PlaceCategory> category,
            @RequestParam(required = false) String slug,
            @RequestParam(required = false) Double rating,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer perPage,

            @RequestParam(required = false, defaultValue = "name") String sortBy,
            @RequestParam(required = false, defaultValue = "DESC") Sort.Direction direction
    ){
        int pageNumber = (page != null) ? page : 0;
        int pageSize = (perPage != null) ? perPage : Integer.MAX_VALUE;
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        PlaceFilter placeFilter = new PlaceFilter(placeUuid, name, city, country, category, slug, rating);

        CustomPageDTO<PlaceDTO> places = placeService.filter(placeFilter, pageable);

        return ResponseEntity.ok().body(places);
    }

    @PutMapping("/{placeUuid}")
    public ResponseEntity<PlaceDTO> updatePlace(@Nonnull @PathVariable UUID placeUuid, @RequestBody PlaceDTO placeDTO){
        PlaceDTO updatedPlace = placeService.updatePlace(placeUuid, placeDTO);

        return ResponseEntity.ok().body(updatedPlace);
    }
}
