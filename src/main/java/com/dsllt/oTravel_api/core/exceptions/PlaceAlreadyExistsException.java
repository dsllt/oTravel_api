package com.dsllt.oTravel_api.core.exceptions;

import java.io.Serial;

public class PlaceAlreadyExistsException extends RuntimeException{
    @Serial
    private static final long serialVersionUID = 1L;

    public PlaceAlreadyExistsException(String message){
        super(message);
    }
}