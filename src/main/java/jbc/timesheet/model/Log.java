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

    private String username;

    private Long timesheetId;

    private LocalDateTime timestamp;

    private String message;

    public Log() {
    }

    public static Log newLog(Action action, String username, Long timesheetId, String message) {
        Log log = new Log();
        log.action = action;
        log.username = username;
        log.timesheetId = timesheetId;
        log.timestamp = LocalDateTime.now();
        log.message = message;
        return log;
    }

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getTimesheetId() {
        return timesheetId;
    }

    public void setTimesheetId(Long timesheetId) {
        this.timesheetId = timesheetId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
