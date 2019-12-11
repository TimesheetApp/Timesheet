package jbc.timesheet.controller;


import jbc.timesheet.model.Timesheet;
import jbc.timesheet.repository.TimesheetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/timesheet")
public class TimesheetController  {

    @Autowired
    TimesheetRepository timesheetRepository;

    //Create (addTimesheet)
    @GetMapping("/create")
    public String createTimesheetform(Model model){
        model.addAttribute("timesheet", new Timesheet());
        return "timesheetform";

    }

    @PostMapping("/process")
    public String processTimesheetform(@Valid @ModelAttribute Timesheet timesheet, BindingResult result){

        if(result.hasErrors()){
            return "timesheetform";
        }

        timesheetRepository.save(timesheet);
        return "redirect:/";
    }

    @GetMapping("/update/{id}")
    public String updateTimesheetform(@PathVariable("id") Long id, Model model){
        model.addAttribute("timesheet", timesheetRepository.findById(id).get());
        return "timesheetform";
    }

    @RequestMapping("/delete/{id}")
    public String deleteTimesheetform(@PathVariable("id") long id){
        timesheetRepository.deleteById(id);
        return "redirect:/";
    }

    @GetMapping("/view{id}")
    public String viewTimesheetform(@PathVariable("id") Long id, Model model){
        model.addAttribute("timesheet", timesheetRepository.findById(id).get());
        return "timesheetform";
    }

    @GetMapping("/show")
    public String viewTimesheetform(Model model){
        model.addAttribute("timesheets", timesheetRepository.findAll());
        return "listtimesheetform";
    }


    }

