package jbc.timesheet.configuration;

import jbc.timesheet.model.Authority;
import jbc.timesheet.model.Employee;
import jbc.timesheet.model.MyUserDetail;
import jbc.timesheet.repository.AuthorityRepository;
import jbc.timesheet.repository.EmployeeRepository;
import jbc.timesheet.repository.MyUserDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
@Transactional
public class DataLoader implements CommandLineRunner {

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    AuthorityRepository authorityRepository;

    @Override
    public void run(String... args) throws Exception {
        initUser("admin@example.com", "ADMIN");
        initUser("krissada@example.com", "ADMIN");
        initUser("tony@example.com", "ADMIN");
        initUser("soheila@example.com", "ADMIN");

        employeeRepository.findByUsername("admin@example.com").ifPresent(this::autoLogin);

    }

    public void autoLogin(Employee user){

        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private void initUser(String username, String authority) {


        Optional<Employee> optionalUser = employeeRepository.findByUsername(username);
        Optional<Authority> optionalAuthority = Optional.empty();
        if (authority != null) {
            optionalAuthority = authorityRepository.findByAuthority(authority.toUpperCase());

            if (!optionalAuthority.isPresent()) {
                optionalAuthority = Optional.of(new Authority(authority.toUpperCase()));
                authorityRepository.save(optionalAuthority.get());
            }
        }

        if (!optionalUser.isPresent()) {
            optionalUser = Optional.of(new Employee(username));
            employeeRepository.save(optionalUser.get());
        }

        if ((authority != null) && (!optionalUser.get().getAuthorities().contains(optionalAuthority.get()))) {
            optionalUser.get().getAuthorities().add(optionalAuthority.get());
            employeeRepository.save(optionalUser.get());
        }



    }
}
