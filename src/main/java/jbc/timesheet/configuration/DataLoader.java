package jbc.timesheet.configuration;

import jbc.timesheet.model.Authority;
import jbc.timesheet.model.MyUserDetail;
import jbc.timesheet.repository.AuthorityRepository;
import jbc.timesheet.repository.MyUserDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@Transactional
public class DataLoader implements CommandLineRunner {

    @Autowired
    MyUserDetailRepository myUserDetailRepository;

    @Autowired
    AuthorityRepository authorityRepository;

    @Override
    public void run(String... args) throws Exception {
        initUser("admin@example.com", "ADMIN");
        initUser("user@example.com", null);
        initUser("krissada@example.com", "ADMIN");
        initUser("tony@example.com", "ADMIN");
        initUser("soheila@example.com", "ADMIN");
    }

    private void initUser(String username, String authority) {


        Optional<MyUserDetail> optionalUser = myUserDetailRepository.findByUsername(username);
        Optional<Authority> optionalAuthority = Optional.empty();
        if (authority != null) {
            optionalAuthority = authorityRepository.findByAuthority(authority.toUpperCase());

            if (!optionalAuthority.isPresent()) {
                optionalAuthority = Optional.of(new Authority(authority.toUpperCase()));
                authorityRepository.save(optionalAuthority.get());
            }
        }

        if (!optionalUser.isPresent()) {
            optionalUser = Optional.of(new MyUserDetail(username));
            myUserDetailRepository.save(optionalUser.get());
        }

        if ((authority != null) && (!optionalUser.get().getAuthorities().contains(optionalAuthority.get()))) {
            optionalUser.get().getAuthorities().add(optionalAuthority.get());
            myUserDetailRepository.save(optionalUser.get());
        }


    }
}
