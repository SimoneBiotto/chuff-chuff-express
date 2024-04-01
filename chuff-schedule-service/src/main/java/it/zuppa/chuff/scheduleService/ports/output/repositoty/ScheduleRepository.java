package it.zuppa.chuff.scheduleService.ports.output.repositoty;

import it.zuppa.chuff.domain.schedule.Schedule;
import java.util.Optional;
import java.util.UUID;

public interface ScheduleRepository {
  Optional<Schedule> findById(UUID id);

  Schedule createSchedule(Schedule schedule);

  void deleteSchedule(Schedule schedule);

  Optional<Schedule> updateSchedule(Schedule schedule);
}
