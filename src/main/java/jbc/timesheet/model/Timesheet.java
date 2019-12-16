package jbc.timesheet.model;


import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Entity
public class Timesheet {
    @Id
    @SequenceGenerator(name = "Timesheet", sequenceName = "TimesheetId", initialValue = 1001, allocationSize = 1)
    @GeneratedValue(generator = "Timesheet")
    private long id;

    @OneToOne(
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH},
            fetch = FetchType.EAGER,
            orphanRemoval = false
    )
    private Employee employee;

    private LocalDate startDate;

    @Transient
    private String isoStartDate;

    private LocalDate endDate;

    @Transient
    private String isoEndDate;

    private LocalDate creationDate = LocalDate.now();

    @OneToMany (
            cascade = {CascadeType.PERSIST, CascadeType.REFRESH},
            fetch = FetchType.EAGER,
            orphanRemoval = true,
            mappedBy = "timesheet"
    )
    private List<Activity> activityList = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private Stage stage = Stage.EDITING;



    public Timesheet() {

        LocalDate now = LocalDate.now();

        // Get next Sunday
        this.startDate = now.with(TemporalAdjusters.next(DayOfWeek.SUNDAY));
        this.endDate = startDate.plus(6,ChronoUnit.DAYS);

    }


    public Timesheet(Employee employee, LocalDate startDate, LocalDate endDate, List<Activity> activityList, Stage stage) {
        this();
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



    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
        this.isoStartDate = startDate.format(DateTimeFormatter.ISO_DATE);
    }

    @Transient
    public String getIsoStartDate() {
        return startDate.format(DateTimeFormatter.ISO_DATE);
    }

    public void setIsoStartDate(String isoStartDate) {
        this.isoStartDate = isoStartDate;
        startDate = LocalDate.parse(isoStartDate, DateTimeFormatter.ISO_DATE);
    }

    public LocalDate getEndDate() {
        return endDate;
    }
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
        this.isoEndDate = endDate.format(DateTimeFormatter.ISO_DATE);
    }
    @Transient
    public String getIsoEndDate() {
        return endDate.format(DateTimeFormatter.ISO_DATE);
    }

    public void setIsoEndDate(String isoEndDate) {
        this.isoEndDate = isoEndDate;
        endDate = LocalDate.parse(isoEndDate, DateTimeFormatter.ISO_DATE);
    }



    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
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

    public double getPay() {
        BigDecimal sumPay = BigDecimal.ZERO;
        for (Activity eachActivity : getActivityList()) {
            sumPay = sumPay.add(BigDecimal.valueOf(eachActivity.getPay()));
        }

        return sumPay.doubleValue();
    }

    public double getHours() {
        BigDecimal sumHours = BigDecimal.ZERO;
        for (Activity eachActivity : getActivityList()) {
            sumHours = sumHours.add(BigDecimal.valueOf(eachActivity.getHours()));
        }

        return sumHours.doubleValue();
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
