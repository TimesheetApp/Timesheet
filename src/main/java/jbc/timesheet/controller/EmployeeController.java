package jbc.timesheet.controller;

import jbc.timesheet.controller.iface.JediController;
import jbc.timesheet.model.Employee;
import jbc.timesheet.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
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
    public void preProcess(Employee employee, BindingResult result) {

        if ((employee == null)||(employee.getPasswordRaw() == null)||(employee.getPasswordVerify()== null)) {
            return;
        }

        if (employee.getPasswordRaw().equals("") && employee.getPasswordVerify().trim().equals(""))
            return;

        if (!employee.getPasswordRaw().equals(employee.getPasswordVerify()))
            result.rejectValue("passwordVerify","error.passwordVerify", "must match password");

        if (employee.getPasswordRaw().length() < 8)
            result.rejectValue("passwordRaw","error.passwordRaw", "must be 8 characters or more");
    }
}
