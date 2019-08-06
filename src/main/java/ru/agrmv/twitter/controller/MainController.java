package ru.agrmv.twitter.controller;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    public String main(@RequestParam(required = false, defaultValue = "") String filter, Model model) {

        if (filter != null && !filter.isEmpty()) {
            model.addAttribute("messages", messageRepository.findByText(filter));
        } else {
            model.addAttribute("messages", messageRepository.findAll());
        }

        model.addAttribute("filter", filter);
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

    @GetMapping("/downloadFile/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Integer fileId) {
        File dbFile = DBFileStorageService.getFile(fileId);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(dbFile.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + dbFile.getFileName() + "\"")
                .body(new ByteArrayResource(dbFile.getData()));
    }

    @PostMapping("/filter")
    public String filter(@RequestParam String text, Model model) {
        if (text != null && !text.isEmpty()) {
            model.addAttribute("messages", messageRepository.findByText(text));
        } else {
            model.addAttribute("messages", messageRepository.findAll());
        }
        return "mainPage";
    }
}
