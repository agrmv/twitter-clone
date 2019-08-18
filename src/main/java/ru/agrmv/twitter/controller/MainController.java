package ru.agrmv.twitter.controller;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.agrmv.twitter.model.Message;
import ru.agrmv.twitter.model.user.User;
import ru.agrmv.twitter.service.MessageService;

import javax.validation.Valid;


@Controller
public class MainController {

    private final MessageService messageService;


    public MainController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("/")
    public String startPage() {
        return "startPage";
    }

    @GetMapping("/main")
    public String main(@RequestParam(required = false, defaultValue = "") String filter, @ModelAttribute("message")
            Message message, Model model) {
        model.addAttribute("messages", messageService.getMessage(filter));
        model.addAttribute("filter", filter);
        return "mainPage";
    }

    @PostMapping("/main")
    public String add(@AuthenticationPrincipal User user, @Valid Message message,
                      BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("messageError", "messageError");
        } else {
            messageService.save(user, message);
            model.addAttribute("messageError", null);
        }
        model.addAttribute("messages", messageService.getMessage(null));
        return "mainPage";
    }


    @GetMapping("/downloadFile/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Integer fileId) {
        return messageService.getFileUrl(fileId);
    }
}