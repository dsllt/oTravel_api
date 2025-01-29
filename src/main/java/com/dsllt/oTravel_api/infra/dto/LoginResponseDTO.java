package com.dsllt.oTravel_api.infra.dto;

import java.util.UUID;

public record LoginResponseDTO(String token, UUID userId) {}
