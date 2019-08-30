package ru.agrmv.twitter.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.agrmv.twitter.model.Message;
import ru.agrmv.twitter.model.dto.MessageDto;
import ru.agrmv.twitter.model.user.User;

import java.util.List;

public interface MessageRepository extends CrudRepository<Message, Integer> {

    @Query("select new ru.agrmv.twitter.model.dto.MessageDto(" +
            "   m, " +
            "   count(ml), " +
            "   sum(case when ml = :user then 1 else 0 end) > 0" +
            ") " +
            "from Message m left join m.likes ml " +
            "group by m")
    List<MessageDto> findAll(@Param("user") User user);

    @Query("select new ru.agrmv.twitter.model.dto.MessageDto(" +
            "   m, " +
            "   count(ml), " +
            "   sum(case when ml = :user then 1 else 0 end) > 0" +
            ") " +
            "from Message m left join m.likes ml " +
            "where m.text = :text " +
            "group by m")
    List<MessageDto> findByText(@Param("text") String text, @Param("user") User user);

    @Query("select new ru.agrmv.twitter.model.dto.MessageDto(" +
            "   m, " +
            "   count(ml), " +
            "   sum(case when ml = :user then 1 else 0 end) > 0" +
            ") " +
            "from Message m left join m.likes ml " +
            "where m.author = :author " +
            "group by m")
    List<MessageDto> findByUser(@Param("author") User author, @Param("user") User user);

}
