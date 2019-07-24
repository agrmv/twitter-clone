package ru.agrmv.twitter.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import ru.agrmv.twitter.user.User;

import javax.persistence.*;

@Entity // Дает понять спрингу, что класс является сущностью, которую необходимо сохранять в базу данных
@Data
@NoArgsConstructor
public class Message {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;

    private String text;
    private String tag;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @Autowired private User author;

    public Message(String text, String tag, User user) {
        this.author = user;
        this.text = text;
        this.tag = tag;
    }

    public String getAuthorName() {
        return author.getUsername();
    }
}
