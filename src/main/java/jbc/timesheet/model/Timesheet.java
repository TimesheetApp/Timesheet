package jbc.timesheet.model;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
public class Timesheet implements Serializable {
    @Id
    private long id;

    private LocalDate startDate;

    private LocalDate endDate;

    private List<Activity> activityList;

    @Enumerated(EnumType.STRING)
    private Stage stage;

    @Transient
    public double getGrossPay() {
        BigDecimal pay = BigDecimal.ZERO;
        for (Activity activity : activityList) {
            pay = pay.add(BigDecimal.valueOf(activity.getPay()));
        }
        return pay.doubleValue();
    }

    public Timesheet fromJson(String json) {
        //TODO: https://springframework.guru/processing-json-jackson/
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(json, Timesheet.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
