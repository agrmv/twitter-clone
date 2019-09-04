package ru.agrmv.twitter.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.agrmv.twitter.model.user.User;
import ru.agrmv.twitter.model.util.MessageHelper;
import ru.agrmv.twitter.service.DBFileStorageService;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
public class Message {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;

    @NotBlank(message = "Please fill the message")
    @Length(max = 280, message = "Message too long(>280 symbols)")
    private String text;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User author;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "file_id")
    private File file;

    @ManyToMany
    @JoinTable(
            name = "message_likes",
            joinColumns = {@JoinColumn(name = "message_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")}
    )
    private Set<User> likes = new HashSet<>();

    public Message(String text, User user) {
        this.author = user;
        this.text = text;
    }

    public Message(String text, User user, File file) {
        this.author = user;
        this.text = text;
        this.file = file;
        this.file.setFileDownloadUri(ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/downloadFile/")
                .path(file.getId().toString())
                .toUriString());
    }

    public void setFile(MultipartFile file) {
        if (file != null && !Objects.requireNonNull(file.getOriginalFilename()).isEmpty()) {
            this.file = DBFileStorageService.storeFile(file);
            this.file.setFileDownloadUri(ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/downloadFile/")
                    .path(this.file.getId().toString())
                    .toUriString());
        }
    }

    public String getAuthorName() {
        return MessageHelper.getAuthorName(author);
    }

    public String getFileDownloadUri() {
       return MessageHelper.getFileDownloadUri(file);
    }

    public String getImage() {
       return MessageHelper.getImage(file);
    }
}
