package jbc.timesheet.service.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class EmailServiceImpl implements EmailServiceIface {
    @Autowired
    Environment environment;

    private String smtpHost;



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

    public boolean sendOne(String recipients, String subject, String message) {
        return false;
    }
}
