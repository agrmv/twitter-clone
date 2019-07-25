package ru.agrmv.twitter.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import ru.agrmv.twitter.model.user.User;

import javax.persistence.*;

@Entity // Дает понять спрингу, что класс является сущностью, которую необходимо сохранять в базу данных
@Data
@NoArgsConstructor
public class Message {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;

    private String text;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User author;

    public Message(String text, User user) {
        this.author = user;
        this.text = text;
    }

    public String getAuthorName() {
        return author.getUsername();
    }
}
