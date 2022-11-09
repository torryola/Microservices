package net.torrydev.microservices.reviewsservice.controller;

import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import net.torrydev.microservices.reviewsservice.dto.CommentDto;
import net.torrydev.microservices.reviewsservice.dto.UserDto;
import net.torrydev.microservices.reviewsservice.dto.UsersListDto;
import net.torrydev.microservices.reviewsservice.interfaces.CommentServiceProxy;
import net.torrydev.microservices.reviewsservice.interfaces.UserServiceProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.reactive.ReactorLoadBalancerExchangeFilterFunction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static net.torrydev.microservices.reviewsservice.constants.ServiceConstants.API_VERSION_WITH_FEIGN;
import static net.torrydev.microservices.reviewsservice.constants.ServiceConstants.APP_USER_SERVICE_ALL_USERS;
import static net.torrydev.microservices.reviewsservice.controller.AppUsersController.USERS_BASE_ENDPOINT;

/**
 * Controller for accessing External app-user-service endpoints instead of wrapping everything
 * in ReviewController class
 */
@Slf4j
@RestController
@RequestMapping(path = USERS_BASE_ENDPOINT)
public class AppUsersController {
    static final String USERS_BASE_ENDPOINT = API_VERSION_WITH_FEIGN + "/users";
    @Autowired
    private WebClient.Builder webclient;
    @Autowired
    private ReactorLoadBalancerExchangeFilterFunction lbFunction;
    @Autowired
    private CommentServiceProxy commentServiceProxy;
    @Autowired
    private UserServiceProxy userServiceProxy;

    private Long prodId;

    /*
User-related EndPoints:
User's Comment on a particular product
/users/user/{id}/comment/product/{productid} ==> List<comment>
User's Rating on a particular product
/users/user/{id}/rating/product/{productid} ==> rating
User's Review on a particular product
/users/user/{id}/product/{productid} ==> Review
*/
    @GetMapping(path = "/")
    @Retry(name = "getAllUser", fallbackMethod = "getAllUserFallBackResponse")
    public ResponseEntity<?> getAllUser() {
        log.trace("ReviewService[getAllUser] is called");
        return webclient.build().get().uri(APP_USER_SERVICE_ALL_USERS).retrieve()
                .bodyToMono(UsersListDto.class).blockOptional()
                .map(usersListDto -> ResponseEntity.ok().body(usersListDto))
                .orElseGet(() -> ResponseEntity.ok().body(UsersListDto.builder().error("User not found").appUserList(null).build()));
    }

    @PostMapping("/user/signup")
    @Retry(name = "app-user-retry", fallbackMethod = "registrationFallBack")
    public ResponseEntity<?> registerNewUser(@RequestBody UserDto userDto) {
        log.trace("ReviewService[registerNewUser] : {}", userDto);
        return userServiceProxy.registerNewUser(userDto);
    }

    /**
     * @param productId Id of the product to get all commentators for
     * @return All users commented on a product
     */
    @GetMapping("/product/{productid}")
    @Retry(name = "app-user-retry", fallbackMethod = "getAllCommentatorsByProductId_Fallback")
    public ResponseEntity<List> getAllCommentatorsByProductId(@PathVariable("productid") Long productId) {
        prodId = productId;
        // Get all comments by product id and iterate each comment to get user
        List users = commentServiceProxy.getListOfCommentsByProductId(productId).stream()
                .map(CommentDto::getUserid).map(userId -> userServiceProxy.getUserByUserId(userId).getBody())
                .collect(Collectors.toList());
        log.trace("ReviewService[getAllCommentatorsByProductId] : {}", users);
        return ResponseEntity.ok().body(users);
    }

    // Fallback Methods
    /**
     * @param ex - Exception thrown by the original method
     * @return - Fallback response for the actual method
     */
    public ResponseEntity<?> getAllUserFallBackResponse(Exception ex){
        log.error(" Exception occurred calling getAllUser {}", ex.getMessage());
        //Here you can get Response from the Cache Instead E.g. Make another Rest Call to Cache-Controller
        List<UserDto> cacheList = List.of(UserDto.builder().Id(1L).name("Cache FirstName")
                .username("Cache UserName").build());
        return ResponseEntity.ok().body(UsersListDto.builder()
                .appUserList(cacheList).error(" Response from Cache").build());
    }

    public ResponseEntity<List> getAllCommentatorsByProductId_Fallback(Exception ex) {
        log.error(" Exception occurred calling getAllCommentatorsByProductId {}", ex.getMessage());
        //Here you can get Response from the Cache Instead E.g. Make another Rest Call to Cache-Controller
        // Get all comments by product id and iterate each comment to get user
        List users = commentServiceProxy.getListOfCommentsByProductId(prodId).stream()
                .map(CommentDto::getUserid).map(userId -> userServiceProxy.getUserByUserId(userId).getBody())
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(users);
    }

    public ResponseEntity<?> registrationFallBack(Exception ex){
        log.error(" Exception occurred calling registerNewUser {}", ex.getMessage());
        return ResponseEntity.ok().body("Error Registering, please try again later");
    }
}
