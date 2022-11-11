package net.torrydev.microservices.reviewsservice.interfaces;

import net.torrydev.microservices.reviewsservice.dto.CommentDto;
import net.torrydev.microservices.reviewsservice.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

import static net.torrydev.microservices.reviewsservice.constants.ServiceConstants.API_GATEWAY;

/**
 *  Facade for Comment-Service related-calls
 * Using Feign Api client will reduce boilerplate that comes with using restTemplate or webClient to
 * make service-to-service call.
 */
//@FeignClient(name = "COMMENTS-SERVICE", url = "localhost:8281/api/v1/comments/")
@FeignClient(name = "COMMENTS-SERVICE", url = "${api.gateway.url}"+ CommentServiceProxy.COMMENT_API_VERSION)
public interface CommentServiceProxy {

    String COMMENT_API_VERSION = "api/v1/comments/";
    @GetMapping("product/{id}")
    List<CommentDto> getListOfCommentsByProductId(@PathVariable("id") Long id);

     //comments/comment/{id} ==> Comment
    @GetMapping("comment/{id}")
    CommentDto getCommentById(@PathVariable("id") Long id);

    @GetMapping("users/total")
    Integer getTotalCommentsByUsers();

    // All users commented on a product
    @GetMapping("users/product/{productid}")
    List<UserDto> getAllUsersCommentedOnProduct(@PathVariable("productid") Long productid);

    // Create New Comment
    @PostMapping("/comment")
    ResponseEntity<?> createNewComment(@RequestBody CommentDto dto);

}
