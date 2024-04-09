package it.zuppa.chuff.scheduleService.ports.input.service;

import it.zuppa.chuff.domain.schedule.Schedule;
import it.zuppa.chuff.exception.DomainException;
import it.zuppa.chuff.scheduleService.dto.schedule.CreateScheduleRequest;
import it.zuppa.chuff.scheduleService.dto.schedule.ScheduleResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.security.InvalidParameterException;
import java.util.Optional;
import java.util.UUID;

public interface ScheduleService {
  Schedule createSchedule(@Valid CreateScheduleRequest createScheduleRequest)
      throws DomainException;

  void deleteSchedule(UUID id) throws DomainException, InvalidParameterException;

  Schedule toggleEnabledStatusSchedule(UUID id, boolean enabled) throws DomainException;

  Optional<Schedule> getSchedule(@NotNull UUID id);
}
