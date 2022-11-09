package net.torrydev.microservices.reviewsservice.controller;

import lombok.extern.slf4j.Slf4j;
import net.torrydev.microservices.reviewsservice.dto.*;
import net.torrydev.microservices.reviewsservice.model.Review;
import net.torrydev.microservices.reviewsservice.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.reactive.ReactorLoadBalancerExchangeFilterFunction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static net.torrydev.microservices.reviewsservice.constants.ServiceConstants.APP_USER_SERVICE_ALL_USERS;
import static net.torrydev.microservices.reviewsservice.constants.ServiceConstants.REST_API;

@Slf4j
@RestController
@RequestMapping(path = ReviewController.RATINGS_BASE_URI)
public class ReviewController {
    public static final String RATINGS_BASE_URI = REST_API + "/reviews";
    @Value("${service.welcome.msg}")
    String msg;

    @Autowired
    private WebClient.Builder webclient;
    @Autowired
    private ReactorLoadBalancerExchangeFilterFunction lbFunction;

    @Autowired
    private ReviewService reviewService;

    @GetMapping("/welcome")
    public String welcomeMsg(){
        return msg;
    }

    /*
     Review-Related EndPoints:
     New Review:
     /review ==> Response + Http Status Created
      Reviews by product id
     /product/{id} ==> List<Review>
    Reviews by product brand AND category
    /product/brand/{brand}/category/{category} ==> List<Review>
    Reviews by product category OR brand
    /product/category/{category}/brand/{brand} ==> List<Review>
    Reviews by User id
     /users/{id} ==> List<Review>
    Reviews by User username
    /user/{username} ==> List<Review>
     */
   // @GetMapping("/product/{id}")
    public ResponseEntity<?> getReviewsByProductId(@PathVariable("id") Long id){
        /*
         1st - Get productDto by product-id
         Run in parallel
         Comments - Get List of comments with this product-id
         Ratings -  Get List of Ratings with this product-id
         - Create List<Review>
          - then for each Review check user-id from each comment/rating to fetch userDto to complete the Review Obj
         */
        ProductDto productDto = reviewService.getProductById(id);
        if (productDto == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Product not found");
        log.info(" ProductDto : {}", productDto);
        List<CommentDto> commentDtoList = reviewService.getCommentsByProductId(id);
        List<RatingsDto> ratingDtoList = reviewService.getRatingsByProductId(id);
        RatingsDto ratingStat = reviewService.getRatingStatByProductId(id);
        //RatingDto ratingDto = ratingDtoList.stream().filter(rating -> rating.getUserid()).findAny();
     //   List<Review> reviews =  commentDtoList.stream().map(commentDto -> Review.builder().commentDto(commentDto).productDto(productDto).build()).collect(Collectors.toList());
        // Create Map of review using user-id as Key
        Map<Long, Review> reviewMap =  commentDtoList.stream()
                .collect(Collectors.toMap(CommentDto::getUserid, commentDto -> Review.builder().commentDto(commentDto).productDto(productDto).build()));
        log.trace(" Review[Comments] : {}", reviewMap);
        //  commentDtoList.forEach( commentDto -> reviews.add(Review.builder().commentDto(commentDto).build()));
        // Add User who commented on the product using user-id from comment
//        reviewMap.forEach((k,v)-> reviewService.addUserToReview(v, k));

//        reviews.forEach(review -> {
//            // Make a call to app-user-service
//            CommentDto commentDto = review.getCommentDto();
//            reviewService.addUserToReview(review, commentDto.getUserid());
//        });

        // Add user who rated product
//        ratingDtoList.forEach(ratingDto -> {
//            Long userId = ratingDto.getUserid();
//            Review review = reviewMap.get(userId);
//            if (review != null)
//                review.setRatingDto(ratingDto);
//            else
//            {
//                // Create New review
//                Review review1 = Review.builder()
//                        .ratingDto(ratingDto)
//                        .productDto(productDto)
//                        .commentDto(null)
//                        .userDto(reviewService.getUserByUserId(userId))
//                        .build();
//                reviewMap.put(userId, review1);
//            }
//        });
        // if the user already exists
        //else create new Review Object

        return ResponseEntity.ok().body(new ArrayList<>(reviewMap.values()));
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<?> getProductReview(@PathVariable("id") Long id){
        // Get Product Details by id
        ProductDto productDto = reviewService.getProductById(id);
        if (productDto == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Product not found");
        log.info(" ProductDto : {}", productDto);
        // Get List of Comments for the Product by Product id
        List<CommentDto> commentDtoList = reviewService.getCommentsByProductId(id);
        log.info("CommentDto : {}",commentDtoList);
        // Find Ratings Stat for a Product by Product id
        RatingsDto ratingStat = reviewService.getRatingStatByProductId(id);
        ProductReview productReview = ProductReview.builder()
                .commentDto(commentDtoList).productDto(productDto).totalrating(ratingStat.getSum()).averagerating(ratingStat.getAverage())
                .build();

        return ResponseEntity.ok().body(productReview);
    }

    @GetMapping("/product/{id}/comment")
    public ResponseEntity<List<CommentDto>> getCommentsByProductId(@PathVariable("id") Long id){
        List<CommentDto> commentDtoList = reviewService.getCommentsByProductId(id);
        log.info("CommentDto @ Controller : {}",commentDtoList);
        return ResponseEntity.status(HttpStatus.OK).body(commentDtoList);
    }

    /*
    User-related EndPoints:
    User's Comment on a particular product
    /users/user/{id}/comment/product/{productid} ==> List<comment>
    User's Rating on a particular product
    /users/user/{id}/rating/product/{productid} ==> rating
   User's Review on a particular product
   /users/user/{id}/product/{productid} ==> Review
    */
    // All Users
    @GetMapping(path = "/users")
    public ResponseEntity<?> getAllUser(){
     UsersListDto usersListDto = webclient.build().get()
                .uri(APP_USER_SERVICE_ALL_USERS)
             .retrieve()
              .bodyToMono(UsersListDto.class).blockOptional().orElse(null);
        if (usersListDto == null)
           return ResponseEntity.badRequest().body("User not Found");
        else
            return ResponseEntity.ok().body(usersListDto);
    }

    /*
     Product-related EndPoints:
    Product by id
     /products/product/{id} ==> Product
     Products by product category
    /products/product/{category} ==> List<Product>
    Products by product brand
    /products/product/{brand} ==> List<Product>
     Products by product brand AND category
    /products/product/brand/{brand}/category/{category} ==> List<Review>
    Products by product category OR brand
    /products/product/category/{category}/brand/{brand} ==> List<Review>
     */

    /*
     List of users who rated a product
     /ratings/product/{prodId}/users -> Pagination List<AppUsers>
     */
    /*
     Ratings-Related Endpoints:
     /ratings/total/product/{id}
     /ratings/avg/product/{id}
     */

    /*
     Comments-Related EndPoints:
     /comments/comment/{id} ==> Comment
     /comments/product/{productid} ==> List<Comment>
     /comments/users/total ==> int
     /comments/users/product/{productid} ==> List<User>
     */

}
