package ru.agrmv.twitter.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.agrmv.twitter.model.user.User;

import javax.persistence.*;
import java.util.Base64;

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

    //TODO FIX THIS
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "file_id")
    private File file;

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
        file.setFileDownloadUri(ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/downloadFile/")
                .path(file.getId().toString())
                .toUriString());
    }

    /**
     * Функция, возвращающая имя автора сообщения
     * */
    public String getAuthorName() {
        return author.getUsername();
    }

    /**
     * Функция, возвращающая ссылку для загрузки файла
     * */
    public String getFileDownloadUri() {
        if (file != null && !file.getFileType().contains("image")) {
            return file.getFileDownloadUri();
        }
        return null;
    }

    /**
     * Функция, возвращающая картинку
     * */
    public String getImage() {
        if (file != null && file.getFileType().contains("image")) {
            return Base64.getEncoder().encodeToString(file.getData());
        }
        return null;
    }
}
