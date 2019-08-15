package ru.agrmv.twitter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.agrmv.twitter.model.Message;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Integer> {

    @Override
    List<Message> findAll();

    List<Message> findByText(String tag);
}
