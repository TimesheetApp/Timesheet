package jbc.timesheet.controller;

import jbc.timesheet.controller.iface.JediController;
import jbc.timesheet.controller.util.ActionType;
import jbc.timesheet.controller.util.JediModelAttributes;
import jbc.timesheet.model.Activity;
import jbc.timesheet.model.Timesheet;
import jbc.timesheet.repository.ActivityRepository;
import jbc.timesheet.repository.TimesheetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.net.ssl.HttpsURLConnection;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

@Controller
@RequestMapping("/activity")
public class ActivityController implements JediController<ActivityRepository, Activity, Long> {

    @Autowired
    ActivityRepository activityRepository;

    @Autowired
    TimesheetRepository timesheetRepository;

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

    @GetMapping("/create/in-timesheet/{timesheetId}")
    public String createInTimesheet(Model model, @PathVariable long timesheetId) {
        Optional<Timesheet> optionalTimesheet = timesheetRepository.findById(timesheetId);

        if (!optionalTimesheet.isPresent()) {
            JediModelAttributes<Activity> jediModelAttributes =
                    new JediModelAttributes<>(HttpsURLConnection.HTTP_NOT_FOUND,newEntity(), ActionType.CREATE, HttpMethod.GET);

            return jediModelAttributes.redirect("/timesheet/retrieve/"+timesheetId);
        }

        Activity activity = newEntity();


        activity.setTimesheet(optionalTimesheet.get());
        activity.setStartTime(LocalDateTime.of(optionalTimesheet.get().getStartDate(), LocalTime.of(8,0)));
        activity.setEndTime(LocalDateTime.of(optionalTimesheet.get().getEndDate(), LocalTime.of(17,0)));



        JediModelAttributes<Activity> jediModelAttributes =
                new JediModelAttributes<>(HttpsURLConnection.HTTP_OK,activity, ActionType.CREATE, HttpMethod.GET);

        jediModelAttributes.setAction("/"+getTemplatePrefix()+"/create");
        jediModelAttributes.setMethod(HttpMethod.POST);
        return jediModelAttributes.view(model);

    }

    @Override
    public void preProcess(Activity activity, BindingResult result) {

    }

    @Override
    public void preDelete(Long id) {
        Activity activity = activityRepository.findById(id).orElse(new Activity());

        if (activity.getTimesheet().getActivityList().contains(activity))
            activity.getTimesheet().getActivityList().remove(activity);
    }

    @Override
    public String postProcess(Activity activity, JediModelAttributes jediModelAttributes) {
        Optional<Timesheet> optionalTimesheet = timesheetRepository.findById(activity.getTimesheet().getId());

        if (optionalTimesheet.isPresent() && !optionalTimesheet.get().getActivityList().contains(activity)) {
                optionalTimesheet.get().getActivityList().add(activity);
                timesheetRepository.save(optionalTimesheet.get());
        }
        return "/timesheet/retrieve/"+activity.getTimesheet().getId();
    }
}

