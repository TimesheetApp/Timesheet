package jbc.timesheet.security;

import jbc.timesheet.controller.util.ActionType;
import jbc.timesheet.controller.util.JediModelAttributes;
import jbc.timesheet.model.Employee;
import jbc.timesheet.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Controller
@RequestMapping("/login")
public class JediSecurityController {

    @Autowired
    EmployeeRepository employeeRepository;




    @GetMapping()
    public String login(Model model, HttpServletRequest request, @RequestParam Optional<String> logout,@RequestParam Optional<String> auto) {

        if (logout.isPresent())
            return "jedi/login";

        if (!auto.isPresent())
            auto = Optional.of("admin@example.com");

        Employee user = jediAutoLogin(request, auto.get());
        if (user != null)
            model.addAttribute("jediSuccess",
                "Jedi used the force and logged you in automatically as '"+user.getUsername()+"'");

       return "/jedi/login";
    }

    private Employee jediAutoLogin(HttpServletRequest request, String username) {
        Optional<Employee> user = employeeRepository.findByUsername(username);

        if (!user.isPresent())
            return null;

        request.getSession();
        Authentication authentication = new UsernamePasswordAuthenticationToken(user.get(), null, user.get().getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return user.orElse(null);
    }
}
