package com.dsllt.oTravel_api.core.usecase;

import com.dsllt.oTravel_api.core.entity.favorite.Favorite;
import com.dsllt.oTravel_api.core.exceptions.FavoriteAlreadyExistsException;
import com.dsllt.oTravel_api.core.exceptions.ObjectNotFoundException;
import com.dsllt.oTravel_api.infra.dto.favorite.CreateFavoriteDTO;
import com.dsllt.oTravel_api.infra.repository.FavoriteRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    public Favorite save(@Valid CreateFavoriteDTO favoriteDTO){
       if(favoriteRepository.existsByUserIdAndPlaceId(favoriteDTO.userId(), favoriteDTO.placeId())){
          throw new FavoriteAlreadyExistsException("Favorito já incluído");
       }
       Favorite newFavorite = new Favorite(favoriteDTO);
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
}
