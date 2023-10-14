package guestbook.controller;

import guestbook.model.UserEntity;
import guestbook.persistence.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RegistrationController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/register")
    public String showRegistrationForm() {
        return "register";
    }

    @PostMapping("/register")
    public String processRegistration(@RequestParam String username, @RequestParam String password) {
        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);

        return "redirect:/custom_login";
    }

    @GetMapping("/user/{username}")
    public String getUser(@PathVariable String username) {
        UserEntity user = userRepository.findByUsername(username);
        if (user != null) {
            return "User " + user.getUsername() + " exists in the database.";
        } else {
            return "User not found.";
        }
    }
}
