package net.torrydev.microservices.commentsservice.service;

import lombok.extern.slf4j.Slf4j;
import net.torrydev.microservices.commentsservice.interfaces.ICommentService;
import net.torrydev.microservices.commentsservice.model.Comment;
import net.torrydev.microservices.commentsservice.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CommentServiceImpl implements ICommentService {

    @Autowired
    CommentRepository commentRepository;

    @Override
    public List<Comment> findByUserId(Long userId) {
        if (null == userId || userId <= 0)
            throw new IllegalArgumentException("Invalid userId : "+userId);
        return commentRepository.findByUserid(userId);
    }

    @Override
    public List<Comment> findByProductId(Long productId) {
        if (null == productId || productId <= 0)
            throw new IllegalArgumentException("Invalid productId : "+productId);
        return commentRepository.findByProductid(productId);
    }

    @Override
    public Comment findByCommentId(Long Id) {
        if (null == Id || Id <= 0)
            throw new IllegalArgumentException("Invalid commentId : "+Id);
        return commentRepository.findById(Id).orElse(null);
    }

    @Override
    public Comment saveNewComment(Comment newComment) {
        return commentRepository.save(newComment);
    }
}
