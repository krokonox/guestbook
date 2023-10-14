package guestbook.util.config;

import guestbook.model.UserEntity;
import guestbook.persistence.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.ArrayList;

@EnableWebSecurity
public class WebSecurityConfig {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public WebSecurityConfig(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/login").permitAll()
                .antMatchers("/register").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/custom_login")
                .loginProcessingUrl("/perform_login")
                .failureUrl("/custom_login?error=true")
                .permitAll()
                .and()
                .logout()
                .permitAll();
        return http.build();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(username -> {
            UserEntity user = userRepository.findByUsername(username);
            if (user != null) {
                return new User(user.getUsername(), user.getPassword(), new ArrayList<>());
            } else {
                throw new UsernameNotFoundException("User not found");
            }
        }).passwordEncoder(passwordEncoder);
    }
}

