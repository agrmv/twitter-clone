package ru.agrmv.twitter.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import ru.agrmv.twitter.repository.UserRepository;
import ru.agrmv.twitter.user.Role;
import ru.agrmv.twitter.user.User;

import java.util.Collections;

@Controller
public class RegistrationController {

    @Autowired
    private UserRepository userRepository;

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
