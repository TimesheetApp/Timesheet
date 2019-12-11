package jbc.timesheet.repository;

import jbc.timesheet.model.Timesheet;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimesheetRepository extends CrudRepository<Timesheet, Long> {

}
