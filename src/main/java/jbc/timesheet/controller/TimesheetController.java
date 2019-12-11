package jbc.timesheet.controller;

import jbc.timesheet.service.EmailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/timesheet")
public class TimesheetController extends TimesheetControllerAbstract {

    @Autowired
    EmailServiceImpl emailService;


    @Override
    void doSomething() {

    }
}
