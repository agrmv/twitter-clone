package ru.agrmv.twitter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.agrmv.twitter.model.File;

@Repository
public interface DBFileRepository extends JpaRepository<File, Integer> {

}
