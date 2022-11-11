package net.torrydev.microservices.commentsservice.interfaces;


import net.torrydev.microservices.commentsservice.model.Comment;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface ICommentService {

    List<Comment> findByUserId(Long Id);
    List<Comment> findByProductId(Long productId);
    Comment findByCommentId(Long Id);
    Comment saveNewComment(Comment newRatings);

}
