package jbc.timesheet.controller;

import jbc.timesheet.controller.iface.JediController;
import jbc.timesheet.model.Activity;
import jbc.timesheet.repository.ActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/activity")
public class ActivityController implements JediController<ActivityRepository, Activity, Long> {

    @Autowired
    ActivityRepository activityRepository;


    @Override
    public Activity newEntity() {
        return new Activity();
    }

    @Override
    public ActivityRepository getRepository() {
        return activityRepository;
    }

    @Override
    public String getTemplatePrefix() {
        return "activity";
    }

    @Override
    public Long getId(Activity activity) {
        return activity.getId();
    }


}

