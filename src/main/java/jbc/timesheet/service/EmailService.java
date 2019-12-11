package jbc.timesheet.service;



public interface EmailService {

    boolean send(String email, String subject, String message);

}
