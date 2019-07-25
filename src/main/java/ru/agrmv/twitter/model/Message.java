package ru.agrmv.twitter.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import ru.agrmv.twitter.model.user.User;

import javax.persistence.*;

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
    @Autowired private User author;

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
     * Функция, возвращающая имя автора сообщения
     * */
    public String getAuthorName() {
        return author.getUsername();
    }
}
