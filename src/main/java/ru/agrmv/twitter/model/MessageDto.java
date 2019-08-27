package ru.agrmv.twitter.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.agrmv.twitter.model.user.User;
import ru.agrmv.twitter.model.util.MessageHelper;

import java.util.Base64;

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
        if (file != null && !file.getFileType().contains("image")) {
            return file.getFileDownloadUri();
        }
        return null;
    }

    public String getImage() {
        if (file != null && file.getFileType().contains("image")) {
            return Base64.getEncoder().encodeToString(file.getData());
        }
        return null;
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
