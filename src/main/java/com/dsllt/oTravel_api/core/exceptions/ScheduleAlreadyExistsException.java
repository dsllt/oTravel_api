package com.dsllt.oTravel_api.core.exceptions;

import java.io.Serial;

public class ScheduleAlreadyExistsException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public ScheduleAlreadyExistsException(String message){
        super(message);
    }
}