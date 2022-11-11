package net.torrydev.microservices.reviewsservice.interfaces;

import net.torrydev.microservices.reviewsservice.dto.RatingsDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Facade for Rating-Service related-calls
 * Using Feign Api client will reduce boilerplate that comes with using restTemplate or webClient to
 * make service-to-service call.
 */
@FeignClient(name = "RATINGS-SERVICE", url = "${api.gateway.url}"+ RatingServiceProxy.RATING_API_VERSION)
public interface RatingServiceProxy {

    String RATING_API_VERSION = "api/v1/ratings/";
    /*
     Ratings-Related Endpoints:
     // Get total Ratings by ProductId
     /ratings/total/product/{id} -> int
     // Get Average Ratings by ProductId
     /ratings/avg/product/{id} -> double
      User's Rating on a particular product
    /users/user/{id}/rating/product/{productid} ==> Rating
     */
    @GetMapping("product/{id}/stat")
    RatingsDto getRatingStatByProductId(@PathVariable("id") Long id);

    @GetMapping("total/product/{id}")
    Integer getTotalRatingsByProductId(@PathVariable("id") Long productId);

    @GetMapping("/avg/product/{id}")
    Double getAverageRatingsByProductId(@PathVariable("id") Long productId);

    @GetMapping("user/{id}/rating/product/{productid}")
    RatingsDto getUserRatingByProductId(@PathVariable("id") Long userId, @PathVariable("productid") Long productId);

    @PostMapping("/rating")
    public ResponseEntity<?> createNewRating(@RequestBody RatingsDto.Ratings dto);

}
