package com.dsllt.oTravel_api.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomPageDTO<T> {
    private int totalPages;
    private long totalElements;
    private List<T> content;
    private int number;
    private int numberOfElements;
    private boolean first;
    private boolean last;
}
