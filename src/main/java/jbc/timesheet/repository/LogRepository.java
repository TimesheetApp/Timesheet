package jbc.timesheet.repository;

import jbc.timesheet.model.Log;
import org.springframework.data.repository.CrudRepository;

public interface LogRepository extends CrudRepository<Log, Long> {
}
