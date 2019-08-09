package ru.agrmv.twitter.service;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import ru.agrmv.twitter.model.File;
import ru.agrmv.twitter.model.Message;
import ru.agrmv.twitter.model.user.User;
import ru.agrmv.twitter.repository.MessageRepository;

import java.util.Objects;

@Service
public class MessageService {
    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public void addMessage(User user, String text, Model model, MultipartFile file) {
        if (file != null && !Objects.requireNonNull(file.getOriginalFilename()).isEmpty()) {
            File dbFile = DBFileStorageService.storeFile(file);
            messageRepository.save(new Message(text, user, dbFile));
        } else {
            messageRepository.save(new Message(text, user));
        }
        model.addAttribute("messages", messageRepository.findAll());
    }

    public void getMessage(String filter, Model model) {
        if (filter != null && !filter.isEmpty()) {
            model.addAttribute("messages", messageRepository.findByText(filter));
        } else {
            model.addAttribute("messages", messageRepository.findAll());
        }

        model.addAttribute("filter", filter);
    }
}
