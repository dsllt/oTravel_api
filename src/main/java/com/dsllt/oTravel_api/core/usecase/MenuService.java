package com.dsllt.oTravel_api.core.usecase;

import com.dsllt.oTravel_api.core.entity.menu.Menu;
import com.dsllt.oTravel_api.core.entity.place.Place;
import com.dsllt.oTravel_api.core.exceptions.BusinessException;
import com.dsllt.oTravel_api.core.exceptions.ObjectNotFoundException;
import com.dsllt.oTravel_api.infra.dto.menu.CreateMenuDTO;
import com.dsllt.oTravel_api.infra.dto.menu.MenuDTO;
import com.dsllt.oTravel_api.infra.repository.MenuRepository;
import com.dsllt.oTravel_api.infra.repository.PlaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class MenuService {

    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private PlaceRepository placeRepository;

    public MenuDTO save(CreateMenuDTO createMenuDTO){
        if(menuRepository.existsByNameAndPlaceId(createMenuDTO.name(), createMenuDTO.placeId())){
            String message = String.format("Item %s já cadastrado para %s.", createMenuDTO.name(), createMenuDTO.placeId());
            throw new BusinessException(message);
        }
        Place place = placeRepository.findById(createMenuDTO.placeId()).orElseThrow(() -> new ObjectNotFoundException("Lucar não encontrado."));

        Menu menu = new Menu(createMenuDTO, place);
        Menu persistedMenu = menuRepository.save(menu);
        return MenuDTO.from(persistedMenu);
    }

    public List<MenuDTO> getByPlaceId(UUID placeId){
        if(!menuRepository.existsByPlaceId(placeId)){
            throw new ObjectNotFoundException("Menus não encontrados para este local.");
        }
        List<Menu> menus = menuRepository.findAllByPlaceId(placeId);
        return List.of(menus.stream()
                .map(MenuDTO::from)
                .toArray(MenuDTO[]::new));
    }

    public MenuDTO update(MenuDTO menuDTO){
        Place place = placeRepository.findById(menuDTO.placeId()).orElseThrow(() -> new ObjectNotFoundException("Local não encontrado."));
        if(!menuRepository.existsByPlaceId(menuDTO.placeId())){
            throw new ObjectNotFoundException("Menus não encontrados para este local.");
        }
        Menu menu = new Menu(menuDTO, place);
        Menu updatedMenu = menuRepository.save(menu);
        return MenuDTO.from(updatedMenu);
    }
}
