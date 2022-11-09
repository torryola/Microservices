package net.torrydev.microservices.reviewsservice.controller;

import lombok.extern.slf4j.Slf4j;
import net.torrydev.microservices.reviewsservice.dto.CommentDto;
import net.torrydev.microservices.reviewsservice.dto.UserDto;
import net.torrydev.microservices.reviewsservice.interfaces.CommentServiceProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static net.torrydev.microservices.reviewsservice.constants.ServiceConstants.API_VERSION_WITH_FEIGN;
import static net.torrydev.microservices.reviewsservice.controller.CommentsController.COMMENTS_BASE_ENDPOINT;

/**
 * Controller for accessing External comments-service endpoints instead of wrapping everything
 * in ReviewController class
 */
@Slf4j
@RestController
@RequestMapping(path = COMMENTS_BASE_ENDPOINT)
public class CommentsController {

    static final String COMMENTS_BASE_ENDPOINT = API_VERSION_WITH_FEIGN + "/comments";
    @Autowired
    private CommentServiceProxy commentServiceProxy;

    /*
 Comments-Related EndPoints:
 /comments/comment/{id} ==> Comment
 /comments/product/{productid} ==> List<Comment>
 /comments/users/total ==> int
 /comments/users/product/{productid} ==> List<User>
 */
    // Comments Endpoints
    @GetMapping("/product/{id}/comment")
    public ResponseEntity<List<CommentDto>> getCommentsByProductId(@PathVariable("id") Long id) {
        List<CommentDto> commentDtoList = commentServiceProxy.getListOfCommentsByProductId(id); // reviewService.getCommentsByProductId(id);
        log.trace("ReviewService[getCommentsByProductId]: {}", commentDtoList);
        return ResponseEntity.status(HttpStatus.OK).body(commentDtoList);
    }

    @GetMapping("/product/{id}")
    List<CommentDto> getListOfCommentsByProductId(@PathVariable("id") Long id){
        log.trace("ReviewService[getListOfCommentsByProductId]: {}", id);
        return commentServiceProxy.getListOfCommentsByProductId(id);
    }

    @GetMapping("/product/{id}/total")
    Integer getTotalCommentsByProductId(@PathVariable("id") Long id){
        log.trace("ReviewService[getTotalCommentsByProductId]: {}", id);
        return commentServiceProxy.getListOfCommentsByProductId(id).size();
    }

    //comments/comment/{id} ==> Comment
    @GetMapping("/comment/{id}")
    CommentDto getCommentById(@PathVariable("id") Long id){
        return commentServiceProxy.getCommentById(id);
    }

    @GetMapping("/users/total")
    Integer getTotalCommentsByUsers(){
        return commentServiceProxy.getTotalCommentsByUsers();
    }

    // All users commented on a product
    @GetMapping("/users/product/{productid}")
    List<UserDto> getAllUsersCommentedOnProduct(@PathVariable("productid") Long productid){
        return commentServiceProxy.getAllUsersCommentedOnProduct(productid);
    }
}
