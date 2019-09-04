package ru.agrmv.twitter.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.agrmv.twitter.model.File;
import ru.agrmv.twitter.model.Message;
import ru.agrmv.twitter.model.user.User;
import ru.agrmv.twitter.model.util.MessageHelper;

/**
 * DTO (data transfer object) используется для передачи данных между подсистемами приложения
 * */

@Data
@NoArgsConstructor
public class MessageDto {

    private Integer id;
    private String text;
    private User author;
    private File file;
    private Long likes;
    private Boolean meLiked;

    public MessageDto(Message message, Long likes, Boolean meLiked) {
        this.id = message.getId();
        this.text = message.getText();
        this.author = message.getAuthor();
        this.file = message.getFile();
        this.likes = likes;
        this.meLiked = meLiked;
    }

    public String getAuthorName() {
        return MessageHelper.getAuthorName(author);
    }

    public String getFileDownloadUri() {
        return MessageHelper.getFileDownloadUri(file);
    }

    public String getImage() {
        return  MessageHelper.getImage(file);
    }

    @Override
    public String toString() {
        return "MessageDto{" +
                "id=" + id +
                ", author=" + author +
                ", likes=" + likes +
                ", meLiked=" + meLiked +
                '}';
    }

}
