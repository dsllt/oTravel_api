package com.dsllt.oTravel_api.infra.adapter.place.in.web;

import com.dsllt.oTravel_api.domain.place.service.PlaceService;
import com.dsllt.oTravel_api.domain.model.CustomPage;
import com.dsllt.oTravel_api.domain.place.model.PlaceCategory;
import com.dsllt.oTravel_api.infra.adapter.place.in.web.mapper.PlaceResponseOutMapper;
import com.dsllt.oTravel_api.infra.adapter.place.in.web.model.CreatePlaceRequestIn;
import com.dsllt.oTravel_api.infra.adapter.place.in.web.model.PlaceResponseOut;
import com.dsllt.oTravel_api.infra.adapter.place.in.web.model.UpdatePlaceRequestIn;
import jakarta.annotation.Nonnull;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/place")
public class PlaceController {

    private final PlaceService placeService;
    private final PlaceResponseOutMapper createPlaceResponseOutMapper;

    @PostMapping
    public ResponseEntity<PlaceResponseOut> create(@RequestBody @Valid CreatePlaceRequestIn createPlaceRequestIn,
                                                   UriComponentsBuilder uriComponentsBuilder){
        var newPlace = placeService.create(createPlaceRequestIn);
        var placeResponseOut = createPlaceResponseOutMapper.toPlaceResponseOut(newPlace);
        var uri = URI.create("/place/" + placeResponseOut.id());
        return ResponseEntity.created(uri).body(placeResponseOut);
    }

    @GetMapping
    public  ResponseEntity<List<PlaceResponseOut>> get(){
        List<PlaceResponseOut> retrievedPlaces = placeService.get();
        return ResponseEntity.ok().body(retrievedPlaces);
    }

    @GetMapping("/{placeUuid}")
    public  ResponseEntity<PlaceResponseOut> getPlaceById(@Nonnull @PathVariable UUID placeUuid){
        var retrievedPlace = placeService.getById(placeUuid);
        return ResponseEntity.ok().body(retrievedPlace);
    }

    @PutMapping("/{placeUuid}")
    public ResponseEntity<PlaceResponseOut> updatePlace(@Nonnull @PathVariable UUID placeUuid,
                                                        @RequestBody UpdatePlaceRequestIn updatePlaceRequestIn){
        var updatedPlace = placeService.update(placeUuid, updatePlaceRequestIn);
        return ResponseEntity.ok().body(updatedPlace);
    }
}
