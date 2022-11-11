package net.torrydev.microservices.reviewsservice.controller;

import lombok.extern.slf4j.Slf4j;
import net.torrydev.microservices.reviewsservice.dto.*;
import net.torrydev.microservices.reviewsservice.interfaces.CommentServiceProxy;
import net.torrydev.microservices.reviewsservice.interfaces.ProductServiceProxy;
import net.torrydev.microservices.reviewsservice.interfaces.RatingServiceProxy;
import net.torrydev.microservices.reviewsservice.interfaces.UserServiceProxy;
import net.torrydev.microservices.reviewsservice.model.Review;
import net.torrydev.microservices.reviewsservice.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.reactive.ReactorLoadBalancerExchangeFilterFunction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static net.torrydev.microservices.reviewsservice.constants.ServiceConstants.*;
import static net.torrydev.microservices.reviewsservice.controller.ReviewControllerWithFeign.REVIEWS_BASE_URI;

@Slf4j
@RestController
@RequestMapping(path = REVIEWS_BASE_URI)
public class ReviewControllerWithFeign {
    public static final String REVIEWS_BASE_URI = API_VERSION_WITH_FEIGN + "/reviews";
    @Value("${service.welcome.msg}")
    String msg;
    @Autowired
    private ProductServiceProxy productServiceProxy;
    @Autowired
    private CommentServiceProxy commentServiceProxy;
    @Autowired
    private RatingServiceProxy ratingServiceProxy;

    @GetMapping("/welcome")
    public String welcomeMsg() {
        return msg;
    }

    // Reviews Endpoints
    /**
     * @param id - id of the product to get reviews for
     * @return - Reviews for the product
     */
    @GetMapping("/product/{id}")
    public ResponseEntity<ProductReview> getProductReview(@PathVariable("id") Long id) {
        // Get Product Details by id
        ProductDto productDto = productServiceProxy.getProductById(id);
        if (productDto == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ProductReview.builder().error("Product not found").build());
        log.info(" ProductDto : {}", productDto);
        // Get List of Comments for the Product by Product id
        List<CommentDto> commentDtoList = commentServiceProxy.getListOfCommentsByProductId(productDto.getId());
        log.info("CommentDto : {}", commentDtoList);
        // Find Ratings Stat for a Product by Product id
        RatingsDto ratingStat = ratingServiceProxy.getRatingStatByProductId(productDto.getId());
        ProductReview productReview = ProductReview.builder().commentDto(commentDtoList)
                .productDto(productDto).totalrating(ratingStat.getSum())
                .averagerating(ratingStat.getAverage()).build();

        return ResponseEntity.ok().body(productReview);
    }

    // Create new Review for a product
    @PostMapping("/new-review")
    public ResponseEntity<?> newReview(@RequestBody NewReviewDto reviewDto) {
        if (reviewDto.getUserId() <= 0 || reviewDto.getProductId() <= 0)
            return ResponseEntity.ok().body("Please provide valid Product-Id and User-Id");
        // Get Product Details by id
        ProductDto productDto = productServiceProxy.getProductById(reviewDto.getProductId());
        if (productDto == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ProductReview.builder().error("Product not found").build());
        log.info(" ProductDto : {}", productDto);
        // Create New Comment
        Optional.of(reviewDto.getComment()).map(comm -> {
            CommentDto commentDto = CommentDto.builder().comment(comm).userid(reviewDto.getUserId()).productid(reviewDto.getProductId()).build();
            return commentServiceProxy.createNewComment(commentDto).getBody();
        }).orElse(null);
        // Create New Ratings
        Optional.of(reviewDto.getActualRating()).filter(i -> i >=1 & i<=5).map(rating -> {
            RatingsDto.Ratings ratings = RatingsDto.Ratings.builder().rating(rating)
                    .userid(reviewDto.getUserId()).productid(reviewDto.getProductId()).build();
            return ratingServiceProxy.createNewRating(ratings);
        }).orElse(null);

        return ResponseEntity.ok().body("Thanks for Reviewing the product");
    }
}
