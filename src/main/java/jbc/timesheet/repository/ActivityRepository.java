package jbc.timesheet.repository;

import jbc.timesheet.model.Activity;
import jbc.timesheet.model.Timesheet;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityRepository extends CrudRepository<Activity, Long> {

    List<Activity> findAllByTimesheetOrderByStartTimeDesc(Timesheet timesheet);
}
