package com.dsllt.oTravel_api.infra.dto.schedule;

import com.dsllt.oTravel_api.core.entity.place.Place;
import com.dsllt.oTravel_api.infra.enums.WeekDay;
import jakarta.validation.constraints.NotNull;

import java.time.OffsetTime;

public record CreateScheduleDTO(
        @NotNull(message = "O dia da semana deve ser definido.") WeekDay weekDay,
        @NotNull(message = "A hora de abertura ser definida.") OffsetTime openAt,
        @NotNull(message = "A hora de fechamento ser definida.")OffsetTime closeAt,
        @NotNull(message = "O local deve ser definido.") Place place
) {
}
