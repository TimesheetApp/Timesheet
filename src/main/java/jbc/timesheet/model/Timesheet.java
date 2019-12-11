package jbc.timesheet.model;


import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Entity
public class Timesheet {
    @Id
    @SequenceGenerator(name = "Timesheet", sequenceName = "TimesheetId", initialValue = 1001, allocationSize = 1)
    @GeneratedValue(generator = "Timesheet")
    private long id;

    private Employee employee;

    private LocalDate startDate;

    private LocalDate endDate;

    @OneToMany (
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER,
            orphanRemoval = false
    )
    private List<Activity> activityList;

    @Enumerated(EnumType.STRING)
    private Stage stage;

    public Timesheet() {
    }


    public Timesheet(Employee employee, LocalDate startDate, LocalDate endDate, List<Activity> activityList, Stage stage) {
        this.employee = employee;
        this.startDate = startDate;
        this.endDate = endDate;
        this.activityList = activityList;
        this.stage = stage;
    }

    @Transient
    public double getGrossPay() {
        BigDecimal pay = BigDecimal.ZERO;
        for (Activity activity : activityList) {
            pay = pay.add(BigDecimal.valueOf(activity.getPay()));
        }
        return pay.doubleValue();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    @Transient
    public String getISOStartDate() {
        return startDate.format(DateTimeFormatter.ISO_DATE_TIME);
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    @Transient
    public String getISOEndDate() {
        return endDate.format(DateTimeFormatter.ISO_DATE_TIME);
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public List<Activity> getActivityList() {
        return activityList;
    }

    public void setActivityList(List<Activity> activityList) {
        this.activityList = activityList;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    //    public Timesheet fromJson(String json) {
//        //TODO: https://springframework.guru/processing-json-jackson/
//        ObjectMapper objectMapper = new ObjectMapper();
//        try {
//            return objectMapper.readValue(json, Timesheet.class);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
}
