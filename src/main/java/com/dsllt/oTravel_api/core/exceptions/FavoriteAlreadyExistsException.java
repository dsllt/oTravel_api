package com.dsllt.oTravel_api.core.exceptions;

import java.io.Serial;

public class FavoriteAlreadyExistsException extends RuntimeException{
    @Serial
    private static final  long serialVersionUID = 1L;

    public  FavoriteAlreadyExistsException(String message){super(message);}
}
