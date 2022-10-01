package net.torrydev.microservices.reviewsservice.controller;

import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import net.torrydev.microservices.reviewsservice.dto.RatingsDto;
import net.torrydev.microservices.reviewsservice.interfaces.RatingServiceProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.reactive.ReactorLoadBalancerExchangeFilterFunction;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collections;

import static net.torrydev.microservices.reviewsservice.constants.ServiceConstants.API_VERSION_WITH_FEIGN;
import static net.torrydev.microservices.reviewsservice.controller.AppUsersController.USERS_BASE_ENDPOINT;
import static net.torrydev.microservices.reviewsservice.controller.RatingsController.RATINGS_BASE_ENDPOINT;

/**
 * Controller for accessing External ratings-service endpoints instead of wrapping everything
 * in ReviewController class
 */
@Slf4j
@RestController
@RequestMapping(path = RATINGS_BASE_ENDPOINT)
public class RatingsController {

    static final String RATINGS_BASE_ENDPOINT = API_VERSION_WITH_FEIGN + "/ratings";

    @Autowired
    private RatingServiceProxy ratingServiceProxy;

    private Long productId;
    private Long userId;

    /*
  Ratings-Related Endpoints:
  /ratings/total/product/{id}
  /ratings/avg/product/{id}
   User's Rating on a particular product
    /users/user/{id}/rating/product/{productid} ==> rating
  */
    // Ratings Endpoints
    @GetMapping("/product/{id}/stat")
    @Retry(name = "rating-api-retry", fallbackMethod = "getRatingStat_From_Cache")
    public RatingsDto getRatingStatByProductId(@PathVariable("id") Long productId) {
        this.productId = productId;
        log.trace("ReviewService[getRatingStatByProductId] {}", productId);
        return ratingServiceProxy.getRatingStatByProductId(productId);
    }

    @GetMapping("/total/product/{id}")
    @Retry(name = "rating-api-retry", fallbackMethod = "getTotalRatingsByProductId_FromCache")
    public Integer getTotalRatingsByProductId(@PathVariable("id") Long productId) {
        this.productId = productId;
        log.trace("ReviewService[getTotalRatingsByProductId] {}", productId);
        return ratingServiceProxy.getTotalRatingsByProductId(productId);
    }

    @GetMapping("/avg/product/{id}")
    @Retry(name = "rating-api-retry", fallbackMethod = "getAverageRatingsByProductId_FromCache")
   public Double getAverageRatingsByProductId(@PathVariable("id") Long productId) {
        this.productId = productId;
        log.trace("ReviewService[getAverageRatingsByProductId] {}", productId);
        return ratingServiceProxy.getAverageRatingsByProductId(productId);
    }

    // User's Rating on a particular product
    @GetMapping("/user/{id}/rating/product/{productid}")
    @Retry(name = "rating-api-retry" , fallbackMethod = "getUserRatingByProductId_FromCache")
   public RatingsDto getUserRatingByProductId(@PathVariable("id") Long userId, @PathVariable("productid") Long productId) {
        this.productId = productId; this.userId = userId;
        log.trace("ReviewService[getUserRatingByProductId] userId {} and productId {}", userId, productId);
        return ratingServiceProxy.getUserRatingByProductId(userId, productId);
    }
    // End Ratings Endpoints

    // FallbackMethods
    // For Prod App grade, Fallback methods will get value from cache Db directly or call Cache Controller
    public RatingsDto getRatingStat_From_Cache(Exception e){
        return RatingsDto.builder().ratings(Collections.emptyList()).average(0.0D)
                .errormessage("Service is Down").message("Rating Stat Pulled from Cache").productid(productId).sum(0).totalratings(0)
                .build();
    }

    public Integer getTotalRatingsByProductId_FromCache(Exception e){
        return 0;
    }

    public Double getAverageRatingsByProductId_FromCache(Exception e){
        return 0.0D;
    }

    public RatingsDto getUserRatingByProductId_FromCache(){
        return RatingsDto.builder().ratings(Collections.emptyList()).average(0.0D)
                .errormessage("Service is Down").message("User ratings Pulled from Cache").productid(productId).sum(0).totalratings(0)
                .build();
    }

}
