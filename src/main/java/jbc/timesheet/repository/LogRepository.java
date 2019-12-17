package jbc.timesheet.repository;

import jbc.timesheet.model.Log;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface LogRepository extends CrudRepository<Log, Long> {
    List<Log> findAllByTimesheetIdOrderByTimestampDesc(long timesheetId);
}
