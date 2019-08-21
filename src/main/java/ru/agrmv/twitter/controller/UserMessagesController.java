package ru.agrmv.twitter.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.agrmv.twitter.model.Message;
import ru.agrmv.twitter.model.user.User;

import java.util.Set;

@Controller
@RequestMapping("/user-messages/{user}")
public class UserMessagesController {

    @GetMapping
    public String userMessages(@AuthenticationPrincipal User currentUser,
                               @PathVariable User user,
                               @ModelAttribute("message") Message message,
                               Model model) {
        Set<Message> messages = user.getMessages();
        model.addAttribute("userChannel", user);
        model.addAttribute("subscriptionsCount", user.getSubscriptions().size());
        model.addAttribute("subscribersCount", user.getSubscribers().size());
        model.addAttribute("isSubscriber", user.getSubscribers().contains(currentUser));
        model.addAttribute("messages", messages);
        model.addAttribute("isCurrentUser", currentUser.equals(user));
        return "userPage";
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
