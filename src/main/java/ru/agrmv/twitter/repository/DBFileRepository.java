package ru.agrmv.twitter.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.agrmv.twitter.model.File;

@Repository
public interface DBFileRepository extends CrudRepository<File, Integer> {

}
