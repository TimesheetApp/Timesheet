package jbc.timesheet.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;


@Configuration
@EnableWebSecurity
public class JediSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private RequestMappingHandlerMapping requestMappingHandlerMapping;

    @Value("${spring.h2.console.enabled:false}")
    private boolean h2ConsoleEnabled;

    @Value("${spring.h2.console.path:/h2-console}")
    private String h2ConsolePath;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {


        auth    .userDetailsService(userDetailsService)
                .passwordEncoder(JediPasswordEncoder.getInstance());

    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {

        httpSecurity    .authorizeRequests()
                .antMatchers(
                        "/",
                        "/login",
                        "/logout",

                        "/js/**",
                        "/css/**",
                        "/images/**",
                        "/image/**",

                        "/**"
                )
                .permitAll();

        if (h2ConsoleEnabled) {
            httpSecurity.authorizeRequests().antMatchers(h2ConsolePath + "/**").permitAll();

            httpSecurity.csrf().disable();

            httpSecurity.headers().frameOptions().disable();
        }

        httpSecurity     .authorizeRequests()
                    .antMatchers(
                            "/blogEntry/edit/**",
                            "/blogEntry/edit/process",
                            "/blogEntry/delete/**")
                    .access("hasAnyRole('USER', 'ADMIN')");

        httpSecurity    .authorizeRequests()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin();

//        httpSecurity    .authorizeRequests()
//                .anyRequest()
//                .authenticated()
//                .and()
//                .formLogin().loginPage("/login").permitAll();

        httpSecurity    .authorizeRequests()
                .and()
                .exceptionHandling()
                .accessDeniedPage("/403");

        httpSecurity    .logout();





//        http.authorizeRequests().and().formLogin()//
//                // Submit URL of login page.
//                .loginProcessingUrl("/j_spring_security_check") // Submit URL
//                .loginPage("/login")//
//                .defaultSuccessUrl("/userAccountInfo")//
//                .failureUrl("/login?error=true")//
//                .usernameParameter("username")//
//                .passwordParameter("password")
//                // Config for Logout Page
//                .and().logout().logoutUrl("/logout").logoutSuccessUrl("/logoutSuccessful");

    }
}
