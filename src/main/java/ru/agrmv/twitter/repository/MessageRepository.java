package ru.agrmv.twitter.repository;

import org.springframework.data.repository.CrudRepository;
import ru.agrmv.twitter.model.Message;

import java.util.List;

public interface MessageRepository extends CrudRepository<Message, Long> {

    @Override
    List<Message> findAll();

    List<Message> findByTag(String tag);
}
