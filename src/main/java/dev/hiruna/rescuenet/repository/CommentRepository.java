package dev.hiruna.rescuenet.repository;

import dev.hiruna.rescuenet.entity.Comment;
import dev.hiruna.rescuenet.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

    // Correct method: Find comments by Post entity
    List<Comment> findByPost(Post post);
}