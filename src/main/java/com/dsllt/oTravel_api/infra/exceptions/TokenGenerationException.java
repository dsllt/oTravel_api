package com.dsllt.oTravel_api.infra.exceptions;

import java.io.Serial;

public class TokenGenerationException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public TokenGenerationException(String message){
        super(message);
    }
}