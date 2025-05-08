package com.dsllt.oTravel_api.infra.adapter.schedule.in.web.model;

import com.dsllt.oTravel_api.domain.model.schedule.WeekDay;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.OffsetTime;
import java.util.UUID;

@Builder(toBuilder = true)
public record UpdateScheduleRequestIn(
        @NotNull(message = "O id deve ser definido.") Long id,
        @NotNull(message = "O dia da semana deve ser definido.") WeekDay weekDay,
        @NotNull(message = "O horário de abertura deve ser definido.") OffsetTime openAt,
        @NotNull(message = "O horário de fechamento deve ser definido..") OffsetTime closeAt
) {
}
