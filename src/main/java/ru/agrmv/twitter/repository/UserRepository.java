package ru.agrmv.twitter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.agrmv.twitter.model.user.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String userName);
}

