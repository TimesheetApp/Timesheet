package jbc.timesheet.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Entity
public class Activity {

    @Id
    private long id;

    private LocalDateTime start;

    private LocalDateTime end;

    private double payRate;

    @Enumerated(EnumType.STRING)
    private PayCode payCode;

    @OneToOne (
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER
    )
    private Employee employee;

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
        BigDecimal seconds = BigDecimal.valueOf(start.until(end, ChronoUnit.SECONDS));

        /**
         * To calculate hour from given seconds
         * There are 3600 seconds (60*60) in one hour
         */
        return seconds
                    .divide(BigDecimal.valueOf(3600), RoundingMode.UNNECESSARY)
                    .doubleValue();

    }
}
