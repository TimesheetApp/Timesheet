package jbc.timesheet.service;

import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {
    @Override
    public boolean send(String email, String subject, String message) {
        return false;
    }
}
