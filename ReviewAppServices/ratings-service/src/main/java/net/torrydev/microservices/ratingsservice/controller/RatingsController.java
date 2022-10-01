package net.torrydev.microservices.ratingsservice.controller;

import lombok.extern.slf4j.Slf4j;
import net.torrydev.microservices.ratingsservice.dto.RatingsDto;
import net.torrydev.microservices.ratingsservice.model.Ratings;
import net.torrydev.microservices.ratingsservice.service.RatingServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static net.torrydev.microservices.ratingsservice.constants.ServiceConstants.REST_API;

@Slf4j
@RestController
@RequestMapping(path = RatingsController.RATINGS_BASE_URI)
public class RatingsController {
    public static final String RATINGS_BASE_URI = REST_API + "/ratings";
    @Value("${service.welcome.msg}")
    String msg;

    @Autowired
    public RatingServiceImpl ratingService;

    @GetMapping("/welcome")
    public String welcomeMsg(){
        return msg;
    }

    @GetMapping("/user/{userid}")
    public ResponseEntity<?> getUsersRatings(@PathVariable("userid") Long userId){
        List<Ratings> ratings = ratingService.findByUserId(userId);
        log.trace("getUsersRatings is called {}", userId);
        return ResponseEntity.ok().body(ratings);
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<?> getProductRatings(@PathVariable("id") Long prodId){
        List<Ratings> ratings = ratingService.findByProductId(prodId);
        log.trace("getProductRatings is call with {} and response {}", prodId, ratings);
        return ResponseEntity.ok().body(ratings);
    }

    @GetMapping("/product/{id}/stat")
    public ResponseEntity<?> getProductRatingSum(@PathVariable("id") Long prodId) {
        RatingsDto dto = ratingService.getRatingSumByProductId(prodId);
        log.trace("getProductRatingSum is called {}", prodId);
        return ResponseEntity.ok().body(dto);
    }

    @GetMapping("/rating/{Id}")
    public ResponseEntity<?> getRatingsById(@PathVariable("Id") Long Id){
        Ratings ratings = ratingService.findById(Id);
        log.trace("getRatingsById {}", Id);
        return ResponseEntity.ok().body(ratings);
    }

    @PostMapping("/rating")
    public ResponseEntity<?> createNewRating(@RequestBody Ratings newRating){
        Ratings ratings = ratingService.saveNewRating(newRating);
        if (ratings == null) {
            log.trace("createNewRating {}", ratings);
            return ResponseEntity.badRequest().body(" Error Creating new Ratings");
        }
        else {
            log.info("New Rating created {}", ratings);
            return ResponseEntity.status(HttpStatus.CREATED).body(ratings);
        }
    }

    @GetMapping("total/product/{id}")
    Integer getTotalRatingsByProductId(@PathVariable("id") Long productId) {
        return Optional.ofNullable(ratingService.findByProductId(productId)).map(List::size).orElse(0);
    }

    @GetMapping("/avg/product/{id}")
    Double getAverageRatingsByProductId(@PathVariable("id") Long productId) {
        return ratingService.getAvgRatingsByProductId(productId);
    }

    @GetMapping("user/{id}/rating/product/{productid}")
    Ratings getUserRatingByProductId(@PathVariable("id") Long userId, @PathVariable("productid") Long productId){
        return ratingService.findByUserIdAndProductId(userId, productId);
    }

    @GetMapping("/rating")
    public ResponseEntity<?> getAllRatings(){
            return ResponseEntity.ok().body(ratingService.getAllRatings());
    }

}
