package jbc.timesheet.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Map;

@Entity
public class Log {

    @Id
    @SequenceGenerator(name = "Log", sequenceName = "LogId", initialValue = 1001, allocationSize = 1)
    @GeneratedValue(generator = "Log")
    private long id;

    @Enumerated(EnumType.STRING)
    private Action action;

    @OneToOne
    private Employee employee;

    private String target;

    private LocalDateTime localDateTime;

    private String message;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
