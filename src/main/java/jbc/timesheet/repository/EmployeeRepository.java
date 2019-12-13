package jbc.timesheet.repository;

import jbc.timesheet.model.Employee;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface EmployeeRepository extends CrudRepository<Employee, Long> {
    Optional<Employee> findByUsername(String username);
}
