package net.torrydev.microservices.commentsservice.repository;

import net.torrydev.microservices.commentsservice.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByUserid(Long userId);
    List<Comment> findByProductid(Long productId);
}
