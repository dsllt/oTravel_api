package com.dsllt.oTravel_api.entity.place;

import com.dsllt.oTravel_api.dtos.enums.PlaceCategory;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Converter
public class PlaceCategoryArrayConverter implements AttributeConverter<List<PlaceCategory>, String[]> {

    @Override
    public String[] convertToDatabaseColumn(List<PlaceCategory> attribute) {
        if (attribute == null || attribute.isEmpty()) {
            return new String[0];
        }
        return attribute.stream().map(Enum::name).toArray(String[]::new);
    }

    @Override
    public List<PlaceCategory> convertToEntityAttribute(String[] dbData) {
        if (dbData == null || dbData.length == 0) {
            return List.of();
        }
        return Arrays.stream(dbData)
                .map(PlaceCategory::valueOf)
                .collect(Collectors.toList());
    }
}
