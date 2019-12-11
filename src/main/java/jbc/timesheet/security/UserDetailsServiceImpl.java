package jbc.timesheet.security;


import jbc.timesheet.model.Authority;
import jbc.timesheet.model.MyUserDetail;
import jbc.timesheet.repository.MyUserDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private MyUserDetailRepository myUserDetailRepository;

    Authority aAdmin = new Authority("ADMIN");
    Authority aUser = new Authority("USER");
    MyUserDetail uAdmin = new MyUserDetail("admin",JediPasswordEncoder.getInstance().encode("password"),
            Collections.unmodifiableList(Arrays.asList(aAdmin, aUser)));

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

        if (userName.equals("admin"))
            return uAdmin;

        Optional<MyUserDetail> optionalMyUserDetail =  myUserDetailRepository.findByUsername(userName);

        if (!optionalMyUserDetail.isPresent())
            throw new UsernameNotFoundException("User '"+userName+"' not found.");

        return optionalMyUserDetail.get();
    }


}
