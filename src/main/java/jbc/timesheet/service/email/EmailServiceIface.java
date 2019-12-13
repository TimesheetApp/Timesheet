package jbc.timesheet.service.email;


import java.util.Set;

public interface EmailServiceIface {

    boolean send(Set<String> recipients, String subject, String message);

    /* TODO: Update sender */
     default String getSender() {
        return "sender@example.com";
    }

}
