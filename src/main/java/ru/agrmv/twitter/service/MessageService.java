package ru.agrmv.twitter.service;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import ru.agrmv.twitter.model.Message;
import ru.agrmv.twitter.model.user.User;
import ru.agrmv.twitter.repository.MessageRepository;

@Service
public class MessageService {
    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public void addMessage(User user, Message message, BindingResult bindingResult, Model model) {
        if (bindingResult.hasFieldErrors()) {
            model.addAttribute("error", "error");
        } else {
            message.setAuthor(user);
            model.addAttribute("error", null);
            messageRepository.save(message);
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
