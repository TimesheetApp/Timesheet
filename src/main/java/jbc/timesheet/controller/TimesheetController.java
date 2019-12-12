package jbc.timesheet.controller;


import jbc.timesheet.controller.iface.JediController;
import jbc.timesheet.model.Timesheet;
import jbc.timesheet.repository.TimesheetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/timesheet")
public class TimesheetController implements JediController<TimesheetRepository, Timesheet, Long> {

    @Autowired
    TimesheetRepository timesheetRepository;

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


}

