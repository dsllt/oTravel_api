package com.dsllt.oTravel_api.core.usecase;

import com.dsllt.oTravel_api.core.entity.favorite.Favorite;
import com.dsllt.oTravel_api.core.entity.place.Place;
import com.dsllt.oTravel_api.core.entity.user.User;
import com.dsllt.oTravel_api.core.exceptions.FavoriteAlreadyExistsException;
import com.dsllt.oTravel_api.core.exceptions.ObjectNotFoundException;
import com.dsllt.oTravel_api.infra.dto.favorite.CreateFavoriteDTO;
import com.dsllt.oTravel_api.infra.dto.favorite.UserFavoritesDTO;
import com.dsllt.oTravel_api.infra.repository.FavoriteRepository;
import com.dsllt.oTravel_api.infra.repository.PlaceRepository;
import com.dsllt.oTravel_api.infra.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;
    private final PlaceRepository placeRepository;
    public Favorite save(@Valid CreateFavoriteDTO favoriteDTO){
       if(favoriteRepository.existsByUserIdAndPlaceId(favoriteDTO.userId(), favoriteDTO.placeId())){
          throw new FavoriteAlreadyExistsException("Favorito já incluído");
       }
       User user = userRepository.findById(favoriteDTO.userId()).orElseThrow(() -> new ObjectNotFoundException("Usuário não encontrado."));
       Place place = placeRepository.findById(favoriteDTO.placeId()).orElseThrow(() -> new ObjectNotFoundException("Local não encontrado."));
       Favorite newFavorite = new Favorite(user, place);
       return favoriteRepository.save(newFavorite);
    }

    public List<Favorite> getByUserId(UUID userUuid){
        return favoriteRepository.findAllByUserId(userUuid);
    }

    public Favorite update(UUID favoriteUUID){
        Favorite savedFavorite = favoriteRepository.findById(favoriteUUID).orElseThrow(() -> new ObjectNotFoundException("Favorito não encontrado."));
        savedFavorite.setActive(!savedFavorite.isActive());
        return  favoriteRepository.save(savedFavorite);
    }

    public List<UserFavoritesDTO> getUsersWithActiveFavorites(){
        List<Favorite> activeFavorites = favoriteRepository.findAllByActiveTrue();
        Map<User, List<Place>> userPlacesMap = activeFavorites.stream()
                .collect(Collectors.groupingBy(Favorite::getUser,
                        Collectors.mapping(Favorite::getPlace, Collectors.toList())));

        return  userPlacesMap.entrySet().stream()
                .map(entry -> new UserFavoritesDTO(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }
}
