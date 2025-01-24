package com.dsllt.oTravel_api.core.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValidationError implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Instant timestamp;
    private Integer status;
    private String error;
    private ArrayList<String> errors;
    private String path;

}