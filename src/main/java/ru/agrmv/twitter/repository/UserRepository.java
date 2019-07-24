package ru.agrmv.twitter.repository;

import org.springframework.data.repository.CrudRepository;
import ru.agrmv.twitter.user.User;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByUsername(String userName);
}

