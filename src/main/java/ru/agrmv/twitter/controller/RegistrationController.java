package ru.agrmv.twitter.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.agrmv.twitter.model.user.User;
import ru.agrmv.twitter.service.UserService;

import javax.validation.Valid;



@Controller
public class RegistrationController {

    private final UserService userService;

    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/registration")
    public String registration(@ModelAttribute("user") User user) {
        return "registrationPage";
    }

    @PostMapping("/registration")
    public String addUser(@ModelAttribute("user") @Valid User user, BindingResult bindingResult, Model model) {

        if (user.getPassword() != null) {
            model.addAttribute("passwordError", "Password are different");
        }

        if (bindingResult.hasErrors()) {
            return "registrationPage";
        }

        if(!userService.addUser(user)) {
            model.addAttribute("usernameError", "User already exist!");
            return "registrationPage";
        }
        return "redirect:/login";
    }
}
