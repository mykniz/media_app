package ru.mykniz.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mykniz.entity.MediaStreamDB;


public interface SpringRepo extends JpaRepository<MediaStreamDB,Long> {

}
