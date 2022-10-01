package net.torrydev.microservices.commentsservice.controller;

import lombok.extern.slf4j.Slf4j;
import net.torrydev.microservices.commentsservice.model.Comment;
import net.torrydev.microservices.commentsservice.service.CommentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static net.torrydev.microservices.commentsservice.constants.ServiceConstants.REST_API;

@Slf4j
@RestController
@RequestMapping(path = CommentsController.COMMENTS_BASE_URI)
public class CommentsController {
    public static final String COMMENTS_BASE_URI = REST_API + "/comments";
    @Value("${service.welcome.msg}")
    String msg;

    @Autowired
    public CommentServiceImpl commentService;

    @GetMapping("/welcome")
    public String welcomeMsg(){
        return msg;
    }

    @GetMapping("/user/{userid}")
    public ResponseEntity<?> getUsersComments(@PathVariable("userid") Long userId){
        List<Comment> Comment = commentService.findByUserId(userId);
        log.trace("getUsersComments is called with {}",userId);
        return ResponseEntity.ok().body(Comment);
    }

    @GetMapping("/product/{prodId}")
    public ResponseEntity<?> getProductComments(@PathVariable("prodId") Long prodId){
        List<Comment> comments = commentService.findByProductId(prodId);
        log.trace("getProductComments is called with {} and response {}", prodId, comments);

        return ResponseEntity.ok().body(comments);
    }

    @GetMapping("/comment/{Id}")
    public ResponseEntity<?> getCommentById(@PathVariable("Id") Long Id){
        Comment comment = commentService.findByCommentId(Id);
        log.trace("getCommentById is called with {}", Id);
        return ResponseEntity.ok().body(comment);
    }

    @PostMapping("/comment")
    public ResponseEntity<?> createNewComment(@RequestBody Comment newComment){
        if (null == newComment)
           throw new IllegalArgumentException("Please enter valid comment details");
        Comment Comment = commentService.saveNewComment(newComment);
        log.trace("createNewComment {} ", newComment);
        if (Comment == null) {
            log.trace("New comment now created");
            return ResponseEntity.badRequest().body(" Error Creating new Comments");
        }
        else {
            log.info("new Comment created");
            return ResponseEntity.status(HttpStatus.CREATED).body(Comment);
        }
    }
}
