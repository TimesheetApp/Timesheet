package jbc.timesheet.configuration;

import jbc.timesheet.model.Authority;
import jbc.timesheet.model.PayCode;
import jbc.timesheet.model.Stage;
import jbc.timesheet.repository.AuthorityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class Jedi {

    public static String expressionObjectName = "jedi";



    public Set<Map.Entry<String, String>> getAllStage() {
        HashMap<String, String> map = new HashMap<>();

        for (Stage stage : Stage.values()) {
            map.put(stage.name(), stage.toString());
        }

        return  map.entrySet();
    }

    public Set<Map.Entry<PayCode, String>> getAllPayCode() {
        HashMap<PayCode, String> map = new HashMap<>();

        for (PayCode payCode : PayCode.values()) {
            map.put(payCode, String.format("%s (x%3.2f)", payCode.toString(), payCode.getRateFactor()));
        }
        return  map.entrySet();
    }


}
