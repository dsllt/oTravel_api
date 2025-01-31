package com.dsllt.oTravel_api.infra.dto.favorite;

import com.dsllt.oTravel_api.core.entity.place.Place;
import com.dsllt.oTravel_api.core.entity.user.User;
import com.dsllt.oTravel_api.infra.dto.place.PlaceDTO;
import com.dsllt.oTravel_api.infra.dto.user.UserDTO;

import java.util.List;
import java.util.stream.Collectors;

public record UserFavoritesDTO(
        UserDTO user,
        List<PlaceDTO> favorites
) {
    public UserFavoritesDTO(User user, List<Place> places) {
        this(new UserDTO(user),
                places.stream().map(PlaceDTO::new).collect(Collectors.toList()));
    }
}
