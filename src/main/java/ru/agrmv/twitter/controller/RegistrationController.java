package ru.agrmv.twitter.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import ru.agrmv.twitter.model.user.Role;
import ru.agrmv.twitter.model.user.User;
import ru.agrmv.twitter.repository.UserRepository;

import java.util.Collections;


@Controller
public class RegistrationController {

    private final UserRepository userRepository;

    public RegistrationController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/registration")
    public String registration() {
        return "registrationPage";
    }

    @PostMapping("/registration")
    public String addUser(User user, Model model) {
        if ((userRepository.findByUsername(user.getUsername())) != null) {
            model.addAttribute("message", "User already exists");
            return "registrationPage";
        }

        user.setActive(true);
        user.setRoles(Collections.singleton(Role.ROLE_USER));
        userRepository.save(user);
        return "redirect:/login";
    }
}
