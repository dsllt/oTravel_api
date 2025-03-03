package com.dsllt.oTravel_api.core.usecase;

import com.dsllt.oTravel_api.core.entity.menu.Menu;
import com.dsllt.oTravel_api.core.exceptions.BusinessException;
import com.dsllt.oTravel_api.core.exceptions.ObjectNotFoundException;
import com.dsllt.oTravel_api.infra.dto.menu.CreateMenuDTO;
import com.dsllt.oTravel_api.infra.dto.menu.MenuDTO;
import com.dsllt.oTravel_api.infra.repository.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.UUID;

@Service
public class MenuService {

    @Autowired
    private MenuRepository menuRepository;

    public MenuDTO save(CreateMenuDTO createMenuDTO){
        if(menuRepository.existsByNameAndPlaceId(createMenuDTO.name(), createMenuDTO.place().getId())){
            String message = String.format("Item %s já cadastrado para %s.", createMenuDTO.name(), createMenuDTO.place().getName());
            throw new BusinessException(message);
        }
        Menu menu = new Menu(createMenuDTO);
        Menu persistedMenu = menuRepository.save(menu);
        return MenuDTO.from(persistedMenu);
    }

    public MenuDTO[] getByPlaceId(UUID placeId){
        if(menuRepository.existsByPlaceId(placeId)){
            throw new ObjectNotFoundException("Menus não encontrados para este local.");
        }
        Menu[] menus = menuRepository.findByPlaceId(placeId);
        return Arrays.stream(menus)
                .map(MenuDTO::from)
                .toArray(MenuDTO[]::new);
    }

    public MenuDTO update(MenuDTO menuDTO){
        if(!menuRepository.existsByPlaceId(menuDTO.place().getId())){
            throw new ObjectNotFoundException("Menus não encontrados para este local.");
        }
        Menu menu = new Menu(menuDTO);
        Menu updatedMenu = menuRepository.save(menu);
        return MenuDTO.from(updatedMenu);
    }
}
