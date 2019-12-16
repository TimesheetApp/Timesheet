package jbc.timesheet.service.email;

import jbc.timesheet.model.Employee;
import jbc.timesheet.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;

@Service
public class EmailService implements EmailServiceIface {

    @Autowired
    Environment environment;

    @Autowired
    EmployeeRepository employeeRepository;

    @Value("${spring.mail.host}")
    private String smtpHost;

    @Value("${spring.mail.port}")
    private int smtpPort;

    @Value("${spring.mail.properties.mail.smtp.auth}")
    private boolean smtpAuth;

    @Value("${spring.mail.properties.mail.smtp.starttls.enable}")
    private boolean smtpStartTls;

    /* TODO: Update the sender email in EmailServiceIface */

    @Override
    public boolean send(Set<String> recipients, String subject, String message) {

        /* TODO: Verify sender
         *  Verify if the emails ( both sender and recipients ) are in compliant to RFC 5322 official standard
         *  Please note that the recipients is the "Set" (more than one email address)
         *  @see https://www.tutorialspoint.com/java/java_string_matches.htm
         *  Regex:
         *      (?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|"(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21\x23-\x5b\x5d-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])*")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21-\x5a\x53-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])+)\])
         */

        for (String eachRecipient : recipients) {
            /* TODO: Verify all recipients */
        }

        /* TODO: Verify if the subject is contain only readable character (a-z, A-Z, 0-9) */

        /* TODO: Verify that message has no java script embedded */


        for (String eachRecipient : recipients) {
            /* TODO: Send the email to all recipients */
        }

        /* TODO: return value
         *      return true if success
         *      return false if failed
         */
        return false;

        /* TODO: remove the word "TODO:" when done */

    }

    public boolean sendOne(String recipient, String subject, String messageBody) {
        try {
            MimeMessage message = new MimeMessage(GetSession());

            //Get current user email

            Optional<Employee> optionalCurrentUser =
                    employeeRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

            if (!optionalCurrentUser.isPresent())
                return false;

            String senderEmail = optionalCurrentUser.get().getUsername();
            //model.addAttribute("email", currentUserEmail);

            //Send email to the currentUser's email (In this case the supervisor)
            message.setFrom(new InternetAddress(senderEmail));


            //Set the employee email
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));



            //email Subject
            message.setSubject(subject);

            //Email content - Hard coded message
            //message.setText("Your timesheet has been rejected");

            //Email content - Non hardcoded message
            message.setText(messageBody, "UTF-8");


            Transport.send(message);

            return true;

        } catch (MessagingException e) {
            return false;
        }

    }

    private Properties GetProperties() {  //properties class imported

        Properties properties = new Properties();
        properties.put("mail.smtp.starttls.enable", smtpStartTls);
        properties.put("mail.smtp.auth", smtpAuth);
        properties.put("mail.smtp.host", smtpHost);
        properties.put("mail.smtp.port", smtpPort);
      //  properties.put("mail.smtp.ssl.trust", environment.getProperty("mail.smtp.ssl.trust"));

        return properties;

    }
    private Session GetSession() {

        final String username = "supersupervisor89@gmail.com";
        final String password = "superpassword";

        //Create session
        Session session = Session.getInstance(GetProperties(),
                new Authenticator() {

                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        return session;
    }
}
