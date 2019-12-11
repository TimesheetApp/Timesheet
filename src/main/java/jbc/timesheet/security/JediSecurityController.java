package jbc.timesheet.security;

import jbc.timesheet.controller.util.ActionType;
import jbc.timesheet.controller.util.JediModelAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/login")
public class JediSecurityController {

    @GetMapping()
    public String login(Model model) {

        JediModelAttributes jediModelAttributes = new JediModelAttributes(200,null, ActionType.LOGIN);
       return jediModelAttributes.view(model, "login");
    }
}
