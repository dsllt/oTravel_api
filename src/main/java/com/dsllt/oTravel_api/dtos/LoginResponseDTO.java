package com.dsllt.oTravel_api.dtos;

import java.util.UUID;

public record LoginResponseDTO(String token, UUID userId) {}
