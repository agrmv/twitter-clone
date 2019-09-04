package ru.agrmv.twitter.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.agrmv.twitter.model.File;
import ru.agrmv.twitter.model.Message;
import ru.agrmv.twitter.model.user.User;
import ru.agrmv.twitter.service.DBFileStorageService;
import ru.agrmv.twitter.service.MessageService;

import java.util.Objects;

@Controller
@RequestMapping("/user-messages/{user}")
public class UserMessagesController {

    private final MessageService messageService;

    public UserMessagesController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping
    public String userMessages(@AuthenticationPrincipal User currentUser, @PathVariable User user,
                               @ModelAttribute("message") Message message, Model model) {
        model.addAttribute("userChannel", user);
        model.addAttribute("subscriptionsCount", user.getSubscriptions().size());
        model.addAttribute("subscribersCount", user.getSubscribers().size());
        model.addAttribute("isSubscriber", user.getSubscribers().contains(currentUser));
        model.addAttribute("messages", messageService.messageListForUser(currentUser, user));
        model.addAttribute("isCurrentUser", currentUser.equals(user));
        return "userPage";
    }

    @PostMapping
    public String chooseImage(@PathVariable User user, @RequestParam("file") MultipartFile file) {
        if (file != null && !Objects.requireNonNull(file.getOriginalFilename()).isEmpty() &&
                Objects.requireNonNull(file.getContentType()).contains("image")) {
            File dbFile = DBFileStorageService.storeFile(file);
            user.setUserpic(dbFile);
        }
        return "redirect:/user-messages/{user}";
    }

    @GetMapping("/{message}")
    public String messageEdit(@PathVariable Message message) {
        return "messageEdit";
    }

    @PostMapping("/{message}")
    public String updateMessage(@RequestParam("text") String text, @RequestParam("id") Message message,
                                @RequestParam("file") MultipartFile file) {
        if (text != null && !text.isEmpty() && !(text.length() > 280)) {
            message.setText(text);
            message.setFile(file);
            return "redirect:/main";
        } else {
            return "redirect:/user-messages/{user}/{message}";
        }
    }
}
