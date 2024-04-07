package it.zuppa.chuff.scheduleService;

import it.zuppa.chuff.domain.schedule.Schedule;
import it.zuppa.chuff.exception.DomainException;
import it.zuppa.chuff.scheduleService.dto.schedule.CreateScheduleRequest;
import it.zuppa.chuff.scheduleService.dto.schedule.ScheduleResponse;
import it.zuppa.chuff.scheduleService.mapper.ScheduleDataMapper;
import it.zuppa.chuff.scheduleService.ports.input.service.ScheduleService;
import it.zuppa.chuff.scheduleService.ports.output.repositoty.ScheduleRepository;
import java.security.InvalidParameterException;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ScheduleServiceImpl implements ScheduleService {
  private final ScheduleRepository repository;
  private final ScheduleDataMapper mapper;

  public ScheduleServiceImpl(ScheduleRepository repository, ScheduleDataMapper mapper) {
    this.repository = repository;
    this.mapper = mapper;
  }

  @Override
  public ScheduleResponse createSchedule(CreateScheduleRequest createScheduleRequest)
      throws DomainException {
    log.info("Creating schedule: " + createScheduleRequest.toString());
    Schedule schedule =
        repository.createSchedule(mapper.createScheduleRequestoToSchedule(createScheduleRequest));
    if (schedule == null) {
      String message = "Could not save schedule";
      log.error(message);
      throw new DomainException(
          message, DomainException.Reason.ERROR_DURING_SAVING, Schedule.class);
    }
    log.info("Schedule created successfully with id {}", schedule.getId());
    return mapper.scheduleToScheduleResponse(schedule);
  }

  @Override
  public void deleteSchedule(UUID id) throws DomainException, InvalidParameterException {
    if (id == null) {
      throw new InvalidParameterException("Id cannot be null");
    }
    Schedule schedule =
        repository
            .findById(id)
            .orElseThrow(
                () ->
                    new DomainException(
                        "Schedule not found",
                        DomainException.Reason.RESOURCE_NOT_FOUND,
                        Schedule.class));
    log.info("Deleting schedule with id {}", id);
    repository.deleteSchedule(schedule);
    log.info("Schedule deleted successfully with id {}", id);
  }

  @Override
  public ScheduleResponse toggleEnabledStatusSchedule(UUID id, boolean enabled)
      throws DomainException {
    Schedule schedule =
        repository
            .findById(id)
            .orElseThrow(
                () ->
                    new DomainException(
                        "Schedule not found",
                        DomainException.Reason.RESOURCE_NOT_FOUND,
                        Schedule.class));
    log.info("Toggling enabled status of schedule with id {} to {}", id, enabled);
    if (schedule.isEnabled() == enabled) {
      log.info("Schedule already has the desired status");
      return mapper.scheduleToScheduleResponse(schedule);
    }
    schedule.setEnabled(enabled);
    Schedule updatedSchedule =
        repository
            .updateSchedule(schedule)
            .orElseThrow(
                () ->
                    new DomainException(
                        "Could not update schedule",
                        DomainException.Reason.ERROR_DURING_SAVING,
                        Schedule.class));
    log.info("Schedule updated successfully with id {}", id);
    return mapper.scheduleToScheduleResponse(updatedSchedule);
  }

  @Override
  public Schedule getSchedule(UUID id) {
    if (id == null) return null;
    return repository.findById(id).orElse(null);
  }
}
