package com.dsllt.oTravel_api.infra.adapter.schedule.in.web.model;

import com.dsllt.oTravel_api.domain.model.schedule.WeekDay;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.OffsetTime;
import java.util.List;

@Builder(toBuilder = true)
public record CreateScheduleRequestIn(
        @NotNull(message = "O dia da semana deve ser definido.") WeekDay weekDay,
        @NotNull(message = "A hora de abertura ser definida.") OffsetTime openAt,
        @NotNull(message = "A hora de fechamento ser definida.") OffsetTime closeAt
) {
}
