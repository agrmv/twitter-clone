package ru.agrmv.twitter.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ru.agrmv.twitter.model.File;
import ru.agrmv.twitter.model.Message;
import ru.agrmv.twitter.model.user.User;
import ru.agrmv.twitter.repository.MessageRepository;
import ru.agrmv.twitter.service.DBFileStorageService;

@Controller
public class MainController {

    private final MessageRepository messageRepository;
    private final DBFileStorageService DBFileStorageService;


    public MainController(MessageRepository messageRepository, DBFileStorageService DBFileStorageService) {
        this.messageRepository = messageRepository;
        this.DBFileStorageService = DBFileStorageService;
    }

    @GetMapping("/")
    public String startPage() {
        return "startPage";
    }

    @GetMapping("/main")
    public String main(Model model) {
        model.addAttribute("messages", messageRepository.findAll());
        return "mainPage";
    }

    @PostMapping("/main")
    public String add(@AuthenticationPrincipal User user, @RequestParam String text, Model model,
                      @RequestParam("file") MultipartFile file) {

        if (file != null && !file.getOriginalFilename().isEmpty()) {
            File dbFile = DBFileStorageService.storeFile(file);
            messageRepository.save(new Message(text, user, dbFile));
        } else {
            messageRepository.save(new Message(text, user));
        }
        model.addAttribute("messages", messageRepository.findAll());
        return "mainPage";
    }

    @PostMapping("filter")
    public String filter(@RequestParam String text, Model model) {
        if (text != null && !text.isEmpty()) {
            model.addAttribute("messages", messageRepository.findByText(text));
        } else {
            model.addAttribute("messages", messageRepository.findAll());
        }
        return "mainPage";
    }
}
