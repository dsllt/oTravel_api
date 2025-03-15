package com.dsllt.oTravel_api.infra.dto.schedule;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateScheduleDTO(
        @NotNull(message = "Os horários devem ser definidos.") List<ScheduleInfoDTO> scheduleInfo,
        @NotNull(message = "O local deve ser definido.") String placeId
) {
}
