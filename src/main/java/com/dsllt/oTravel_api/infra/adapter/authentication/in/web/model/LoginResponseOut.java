package com.dsllt.oTravel_api.infra.adapter.authentication.in.web.model;

import lombok.Builder;

import java.util.UUID;

@Builder(toBuilder = true)
public record LoginResponseOut(String token, UUID userId) {}
