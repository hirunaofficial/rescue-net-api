package dev.hiruna.rescuenet.repository;

import dev.hiruna.rescuenet.entity.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsRepository extends JpaRepository<News, Integer> {

}