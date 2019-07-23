package ru.agrmv.twitter.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.agrmv.twitter.model.Message;
import ru.agrmv.twitter.repository.MessageRepository;

@Controller
public class MainController {

    @Autowired
    private MessageRepository messageRepository;

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
    public String add(@RequestParam String text, @RequestParam String tag, Model model) {
        messageRepository.save(new Message(text, tag));
        model.addAttribute("messages", messageRepository.findAll());
        return "mainPage";
    }

    @PostMapping("filter")
    public String filter(@RequestParam String tag, Model model) {
        if (tag != null && !tag.isEmpty()) {
            model.addAttribute("messages", messageRepository.findByTag(tag));
        } else {
            model.addAttribute("messages", messageRepository.findAll());
        }
        return "mainPage";
    }
}
