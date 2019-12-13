package jbc.timesheet.configuration;

import jbc.timesheet.model.Authority;
import jbc.timesheet.model.PayCode;
import jbc.timesheet.model.Stage;
import jbc.timesheet.repository.AuthorityRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Jedi {

    public static String expressionObjectName = "jedi";

    @Autowired
    AuthorityRepository authorityRepository;

    public Set<Map.Entry<Stage, String>> getAllStage() {
        HashMap<Stage, String> map = new HashMap<>();

        for (Stage stage : Stage.values()) {
            map.put(stage, stage.toString());
        }

        return  map.entrySet();
    }

    public Set<Map.Entry<PayCode, String>> getAllPayCode() {
        HashMap<PayCode, String> map = new HashMap<>();

        for (PayCode payCode : PayCode.values()) {
            map.put(payCode, String.format("%s (%3.2f)", payCode.toString(), payCode.getRateFactor()));
        }
        return  map.entrySet();
    }

    public Set<Map.Entry<Authority, String>> getAllAuthority() {
        HashMap<Authority, String> map = new HashMap<>();

        for (Authority authority : authorityRepository.findAll()) {
            map.put(authority, authority.getAuthority());
        }
        return  map.entrySet();
    }
}
