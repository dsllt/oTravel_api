package com.dsllt.oTravel_api.service.exceptions;

import java.io.Serial;

public class TokenVerificationException extends RuntimeException{
    @Serial
    private static final long serialVersionUID = 1L;

    public TokenVerificationException(String message){
        super(message);
    }
}