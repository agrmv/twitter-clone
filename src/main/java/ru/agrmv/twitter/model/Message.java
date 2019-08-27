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

/**
 * Класс ChatMessageModel описывает модель сообщения
 *
 * @author agrmv
 * */

@Entity // Дает понять спрингу, что класс является сущностью, которую необходимо сохранять в базу данных
@Data   // Удобная аннотация, которая объединяет @ToString, @EqualsAndHashCode, @Getter / @Setter, @RequiredArgsConstructor
@NoArgsConstructor //Создает конструктор без аргументов
public class Message {
    /** Идентификатор сообщения */
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;

    /** Текст сообщения */
    @NotBlank(message = "Please fill the message")
    @Length(max = 280, message = "Message too long(>280 symbols)")
    private String text;

    /**
     * Атор сообщения
     *
     * Через @JoinColumn описана колонка в базе данных, которая связывает author с user_id из родительской таблици.
     *
     * В @ManyToOne каждый раз получая сообщение мы получаем и информацию о авторе
     * EAGER = fetch immediately(получить немедленно)
     * */
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

    /**
     * Конструктор - создание нового объекта(сообщения) с определенными значениями
     * @param text - текст сообщения
     * @param user - автор сообщения
     * */
    public Message(String text, User user) {
        this.author = user;
        this.text = text;
    }

    /**
     * Конструктор - создание нового объекта(сообщения с файлом) с определенными значениями
     * @param text - текст сообщения
     * @param user - автор сообщения
     * @param file - отправляемый файл
     * */
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

    /**
     * Функция, возвращающая имя автора сообщения
     * */
    public String getAuthorName() {
        return MessageHelper.getAuthorName(author);
    }

    /**
     * Функция, возвращающая ссылку для загрузки файла
     * */
    public String getFileDownloadUri() {
       return MessageHelper.getFileDownloadUri(file);
    }

    /**
     * Функция, возвращающая картинку
     * */
    public String getImage() {
       return MessageHelper.getImage(file);
    }
}
