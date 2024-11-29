package dev.hiruna.rescuenet.repository;

import dev.hiruna.rescuenet.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {

    // Optional: You can define custom queries here if needed

    // Example of finding posts by userId
    List<Post> findByUserId(Integer user_Id);

    // Example of finding post by its ID
    Optional<Post> findById(Integer id);

    // Example of finding posts by title
    List<Post> findByTitleContaining(String keyword);
}
