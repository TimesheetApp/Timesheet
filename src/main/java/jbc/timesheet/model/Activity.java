package jbc.timesheet.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@Entity
public class Activity {

    @Id
    @SequenceGenerator(name = "Activity", sequenceName = "ActivityId", initialValue = 1001, allocationSize = 1)
    @GeneratedValue(generator = "Activity")
    private long id;

    private LocalDateTime startTime;

    @Transient
    private String isoStartTime;

    private LocalDateTime endTime;

    @Transient
    private String isoEndTime;

    private double payRate;

    @Enumerated(EnumType.STRING)
    private PayCode payCode;

    @ManyToOne (
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER
    )
    private Timesheet timesheet;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
        this.isoStartTime = startTime.format(DateTimeFormatter.ISO_DATE_TIME);
    }

    public String getIsoStartTime() {
        if (startTime == null)
            return "";
        return startTime.format(DateTimeFormatter.ISO_DATE_TIME);
    }

    public void setIsoStartTime(String isoStartTime) {

        this.startTime  = LocalDateTime.parse(this.isoStartTime = isoStartTime, DateTimeFormatter.ISO_DATE_TIME);
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
                this.endTime = endTime;
        this.isoEndTime = endTime.format(DateTimeFormatter.ISO_DATE_TIME);
    }

    public String getIsoEndTime() {
        if (endTime == null)
            return "";
        return endTime.format(DateTimeFormatter.ISO_DATE_TIME);
    }

    public void setIsoEndTime(String isoEndTime) {

        this.endTime = LocalDateTime.parse(this.isoEndTime = isoEndTime, DateTimeFormatter.ISO_DATE_TIME);;
    }

    public double getPayRate() {
        return payRate;
    }

    public void setPayRate(double payRate) {
        this.payRate = payRate;
    }

    public PayCode getPayCode() {
        return payCode;
    }

    public void setPayCode(PayCode payCode) {
        this.payCode = payCode;
    }

    public Timesheet getTimesheet() {
        return timesheet;
    }

    public void setTimesheet(Timesheet timesheet) {
        this.timesheet = timesheet;
    }

    @Transient
    public double getPay() {
        /**
         * Floating point numbers will most of the times return an answer with a small error
         * (around 10^-19) This is the reason why we end up with 0.009999999999999998 as the result of 0.04-0.03.
         *
         * BigDecimal provides us with the exact answer!
         */
        return BigDecimal.valueOf(getHours())
                .setScale(2, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(payRate))
                    .multiply(BigDecimal.valueOf(payCode.getRateFactor()))
                .doubleValue(); // Convert BigDecimal to double
    }

    @Transient
    public double getHours() {
        /**
         * Floating point numbers will most of the times return an answer with a small error
         * (around 10^-19) This is the reason why we end up with 0.009999999999999998 as the result of 0.04-0.03.
         *
         * BigDecimal provides us with the exact answer!
         */

        /**
         * Get time different in seconds
         */
        BigDecimal seconds = BigDecimal.valueOf(startTime.until(endTime, ChronoUnit.SECONDS));

        /**
         * To calculate hour from given seconds
         * There are 3600 seconds (60*60) in one hour
         */
        return seconds
                    .divide(BigDecimal.valueOf(3600), RoundingMode.UNNECESSARY)
                    .doubleValue();

    }


}
