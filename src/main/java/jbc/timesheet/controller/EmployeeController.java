package jbc.timesheet.controller;

import jbc.timesheet.controller.iface.JediController;
import jbc.timesheet.model.Employee;
import jbc.timesheet.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

@Component
@RequestMapping("/employee")
public class EmployeeController implements JediController<EmployeeRepository, Employee, Long> {

    @Autowired
    EmployeeRepository employeeRepository;

    @Override
    public Employee newEntity() {
        return new Employee();
    }

    @Override
    public EmployeeRepository getRepository() {
        return employeeRepository;
    }

    @Override
    public String getTemplatePrefix() {
        return "employee";
    }

    @Override
    public Long getId(Employee employee) {
        return employee.getId();
    }

    @Override
    public Iterable<Employee> searchEntity(String... query) {
        return employeeRepository.findAll();
    }


}
