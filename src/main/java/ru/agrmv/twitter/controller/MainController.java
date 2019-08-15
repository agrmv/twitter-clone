package ru.agrmv.twitter.controller;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.agrmv.twitter.model.File;
import ru.agrmv.twitter.model.Message;
import ru.agrmv.twitter.model.user.User;
import ru.agrmv.twitter.service.DBFileStorageService;
import ru.agrmv.twitter.service.MessageService;

import javax.validation.Valid;


@Controller
public class MainController {

    private final MessageService messageService;
    private final DBFileStorageService DBFileStorageService;


    public MainController(MessageService messageService, DBFileStorageService DBFileStorageService) {
        this.messageService = messageService;
        this.DBFileStorageService = DBFileStorageService;
    }

    @GetMapping("/")
    public String startPage() {
        return "startPage";
    }

    @GetMapping("/main")
    public String main(@RequestParam(required = false, defaultValue = "") String filter, @ModelAttribute("message")
            Message message, Model model) {
        messageService.getMessage(filter, model);
        return "mainPage";
    }

    @PostMapping("/main")
    public String add(@AuthenticationPrincipal User user, @Valid Message message,
                      BindingResult bindingResult, Model model) {
        messageService.addMessage(user, message, bindingResult, model);
        return "mainPage";
    }


    @GetMapping("/downloadFile/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Integer fileId) {
        File dbFile = DBFileStorageService.getFile(fileId);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(dbFile.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + dbFile.getFileName() + "\"")
                .body(new ByteArrayResource(dbFile.getData()));
    }
}