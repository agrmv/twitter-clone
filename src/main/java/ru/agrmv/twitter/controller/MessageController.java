package ru.agrmv.twitter.controller;

import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import ru.agrmv.twitter.model.Message;
import ru.agrmv.twitter.model.user.User;
import ru.agrmv.twitter.service.MessageService;

import javax.validation.Valid;
import java.util.Set;


@Controller
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("/")
    public String startPage() {
        return "startPage";
    }

    @GetMapping("/main")
    public String main(@RequestParam(required = false, defaultValue = "") String filter, @ModelAttribute("message")
            Message message, Model model, @PageableDefault(sort = { "id" }, direction = Sort.Direction.DESC) Pageable pageable,
                       @AuthenticationPrincipal User user) {
        model.addAttribute("messages", messageService.messageList(pageable, filter, user));
        model.addAttribute("filter", filter);
        return "mainPage";
    }

    @PostMapping("/main")
    public String add(@AuthenticationPrincipal User user, @Valid Message message, BindingResult bindingResult,
                      @PageableDefault(sort = { "id" }, direction = Sort.Direction.DESC) Pageable pageable, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("messageError", "messageError");
        } else {
            messageService.save(user, message);
            model.addAttribute("messageError", null);
        }
        model.addAttribute("messages", messageService.messageList(pageable, user));
        return "mainPage";
    }


    @GetMapping("/downloadFile/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Integer fileId) {
        return messageService.getFileUrl(fileId);
    }

    @GetMapping("/messages/{message}/like")
    public String like(@AuthenticationPrincipal User currentUser, @PathVariable Message message,
                       RedirectAttributes redirectAttributes, @RequestHeader(required = false) String referer) {
        Set<User> likes = message.getLikes();

        if (likes.contains(currentUser)) {
            likes.remove(currentUser);
        } else {
            likes.add(currentUser);
        }

        UriComponents components = UriComponentsBuilder.fromHttpUrl(referer).build();
        components.getQueryParams().forEach(redirectAttributes::addAttribute);

        return "redirect:" + components.getPath();
    }

    @GetMapping("likes/{message}/list")
    public String likes(@PathVariable Message message, Model model) {
        model.addAttribute("likes", message.getLikes());
        return "likes";
    }
}