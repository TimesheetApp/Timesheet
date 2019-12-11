package jbc.timesheet.controller;

import org.springframework.web.bind.annotation.GetMapping;

public abstract class TimesheetControllerAbstract {

    abstract void doSomething();

    @GetMapping("/create")
    public String create() {
        doSomething();
        return "view";
    }

}
