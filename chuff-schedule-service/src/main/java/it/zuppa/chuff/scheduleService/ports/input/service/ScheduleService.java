package it.zuppa.chuff.scheduleService.ports.input.service;

import it.zuppa.chuff.exception.DomainException;
import it.zuppa.chuff.scheduleService.dto.schedule.CreateScheduleRequest;
import it.zuppa.chuff.scheduleService.dto.schedule.ScheduleResponse;
import jakarta.validation.Valid;
import java.security.InvalidParameterException;
import java.util.UUID;

public interface ScheduleService {
  ScheduleResponse createSchedule(@Valid CreateScheduleRequest createScheduleRequest)
      throws DomainException;

  void deleteSchedule(UUID id) throws DomainException, InvalidParameterException;

  ScheduleResponse toggleEnabledStatusSchedule(UUID id, boolean enabled) throws DomainException;
}