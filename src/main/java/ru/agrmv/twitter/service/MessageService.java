package ru.agrmv.twitter.service;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.agrmv.twitter.model.File;
import ru.agrmv.twitter.model.Message;
import ru.agrmv.twitter.model.dto.MessageDto;
import ru.agrmv.twitter.model.user.User;
import ru.agrmv.twitter.repository.MessageRepository;

import java.util.List;

@Service
public class MessageService {
    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public void save(User user, Message message) {
        message.setAuthor(user);
        messageRepository.save(message);
    }

    public List<MessageDto> messageList(User user) {
        return  messageRepository.findAll(user);
    }


    public List<MessageDto> messageList(String filter, User user) {
        if (filter != null && !filter.isEmpty()) {
             return messageRepository.findByText(filter, user);
        } else {
            return  messageRepository.findAll(user);
        }
    }

    public List<MessageDto> messageListForUser(User currentUser, User author) {
        return messageRepository.findByUser(author, currentUser);
    }

    public ResponseEntity<Resource> getFileUrl(Integer fileId) {
        File dbFile = DBFileStorageService.getFile(fileId);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(dbFile.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + dbFile.getFileName() + "\"")
                .body(new ByteArrayResource(dbFile.getData()));
    }
}
