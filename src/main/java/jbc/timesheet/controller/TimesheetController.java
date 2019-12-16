
package jbc.timesheet.controller;


import jbc.timesheet.controller.iface.JediController;
import jbc.timesheet.controller.util.ActionType;
import jbc.timesheet.controller.util.JediModelAttributes;
import jbc.timesheet.model.Authority;
import jbc.timesheet.model.Employee;
import jbc.timesheet.model.Stage;
import jbc.timesheet.model.Timesheet;
import jbc.timesheet.repository.AuthorityRepository;
import jbc.timesheet.repository.EmployeeRepository;
import jbc.timesheet.repository.TimesheetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.net.ssl.HttpsURLConnection;
import javax.validation.Valid;
import java.security.Principal;
import java.util.Collections;
import java.util.Optional;

@Controller
@RequestMapping("/timesheet")
public class TimesheetController implements JediController<TimesheetRepository, Timesheet, Long> {

    @Autowired
    TimesheetRepository timesheetRepository;

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    AuthorityRepository authorityRepository;

    @Override
    public Timesheet newEntity() {
        return new Timesheet();
    }

    @Override
    public TimesheetRepository getRepository() {
        return timesheetRepository;
    }

    @Override
    public String getTemplatePrefix() {
        return "timesheet";
    }

    @Override
    public Long getId(Timesheet timesheet) {
        return timesheet.getId();
    }


    @Override
    public void preProcess(Timesheet timesheet, BindingResult result) {

        Optional<Employee> optionalEmployee = employeeRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());


        if (!optionalEmployee.isPresent()) {
            result.rejectValue("employee", "error.employee", "could not load current logged in user credential");
            return;
        }

        if (timesheet.getId() == 0) {
            timesheet.setEmployee(optionalEmployee.get());
            return;
        }
        Optional<Timesheet> optionalTimesheet = timesheetRepository.findById(timesheet.getId());
        if (!optionalTimesheet.isPresent()) {
            result.rejectValue("id", "error.id", "Timesheet not found id=" + timesheet.getId());
            return;
        }
        System.out.printf("Current Timesheet owner = %d\n", optionalTimesheet.get().getEmployee().getId());

        // Pull value from database not from client!
        timesheet.setEmployee(optionalTimesheet.get().getEmployee());
        timesheet.setStage(optionalTimesheet.get().getStage());
        timesheet.setActivityList(optionalTimesheet.get().getActivityList());
    }

    @GetMapping("/update/{id}/stage")
    public String updateStage(@RequestParam Stage updateTo, @PathVariable Long id, Model model) {
        Optional<Timesheet> optionalTimesheet = timesheetRepository.findById(id);
        Optional<Employee> optionalEmployee = employeeRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        JediModelAttributes<Timesheet> jediModelAttributes =
                new JediModelAttributes<Timesheet>(HttpsURLConnection.HTTP_OK,newEntity(), ActionType.UPDATE, HttpMethod.GET);
        if (!optionalTimesheet.isPresent()) {
            jediModelAttributes.setError("Could change stage because could not find the timesheet by id=" + id);
            return jediModelAttributes.view(model);
        }
        if (!optionalEmployee.isPresent()) {
            jediModelAttributes.setError("Could change stage because could not load current logged in user credential");
            return jediModelAttributes.view(model);
        }

        boolean isPrincipalAnAdmin = optionalEmployee.get().hasAuthority("ADMIN");

        if (!optionalTimesheet.get().getEmployee().getUsername().equals(optionalEmployee.get().getUsername())
            && !isPrincipalAnAdmin
        ) {
            jediModelAttributes.setError("Cannot change stage because current user is not the owner of timesheet, or not an 'ADMIN'");
            return jediModelAttributes.view(model);
        }


        // TODO: Log
        if ((optionalTimesheet.get().getStage() == Stage.EDITING)&&(updateTo==Stage.PENDING))
            optionalTimesheet.get().setStage(Stage.PENDING);

        else if ((optionalTimesheet.get().getStage() == Stage.PENDING)&&(updateTo==Stage.EDITING))
            optionalTimesheet.get().setStage(Stage.EDITING);

        else if ((optionalTimesheet.get().getStage() == Stage.REJECTED)&&(updateTo==Stage.EDITING))
            optionalTimesheet.get().setStage(Stage.EDITING);

        else if ((isPrincipalAnAdmin) && (optionalTimesheet.get().getStage() == Stage.PENDING)&&(updateTo==Stage.APPROVED))
            optionalTimesheet.get().setStage(Stage.APPROVED);

        else if ((isPrincipalAnAdmin) && (optionalTimesheet.get().getStage() == Stage.PENDING)&&(updateTo==Stage.REJECTED))
            optionalTimesheet.get().setStage(Stage.REJECTED);

        else if ((isPrincipalAnAdmin) && (optionalTimesheet.get().getStage() == Stage.APPROVED)&&(updateTo==Stage.PENDING))
            optionalTimesheet.get().setStage(Stage.PENDING);

        else if ((isPrincipalAnAdmin) && (optionalTimesheet.get().getStage() == Stage.REJECTED)&&(updateTo==Stage.PENDING))
            optionalTimesheet.get().setStage(Stage.PENDING);

        else {
            jediModelAttributes.setError("unauthorized stage change, maybe you are not login");

            return jediModelAttributes.view(model);
        }

        timesheetRepository.save(optionalTimesheet.get());
        jediModelAttributes.setSuccess("Stage updated");

        return jediModelAttributes.redirect("/"+getTemplatePrefix()+"/retrieve/"+optionalTimesheet.get().getId());
    }


    @Override
    public Iterable<Timesheet> searchEntity(Model model, Principal user, SecurityContextHolderAwareRequestWrapper requestWrapper, MultiValueMap<String, String> parameters) {

        if (user == null)
            return (Iterable<Timesheet>) Collections.EMPTY_LIST;

        Employee employee = employeeRepository.findByUsername(user.getName()).orElse(new Employee());

        Authority admin = authorityRepository.findByAuthority("ADMIN").orElse(new Authority());

        if (employee.getAuthorities().contains(admin)) {
            return getRepository().findAllByOrderByEmployeeLastNameAscEmployeeFirstNameAscStartDateDesc();
        } else {
            return getRepository().findAllByEmployeeOrderByStartDateDesc(employee);
        }

    }
}
