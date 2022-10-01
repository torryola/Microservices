package net.torrydev.microservices.reviewsservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.torrydev.microservices.reviewsservice.dto.CommentDto;
import net.torrydev.microservices.reviewsservice.dto.ProductDto;
import net.torrydev.microservices.reviewsservice.dto.RatingsDto;
import net.torrydev.microservices.reviewsservice.dto.UserDto;
import net.torrydev.microservices.reviewsservice.model.Review;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static net.torrydev.microservices.reviewsservice.constants.ServiceConstants.*;

@Slf4j
@Service
public class ReviewServiceUsingApiGateway {

    @Autowired
    WebClient.Builder webclient;
    @Autowired
    ObjectMapper objectMapper;

    // Get List of Comments Asynchronously by product-id from comments-service
    public List<CommentDto> getCommentsByProductId(Long productId){
        CommentDto[] res = webclient.build().get().uri(COMMENTS_BY_PRODUCT_ID_URI+productId)
                .accept(MediaType.APPLICATION_JSON).retrieve().bodyToMono(CommentDto[].class).block();

        return Optional.ofNullable(res).map(Arrays::asList).orElse(null);
    }
    // Get List of Ratings by product-id from ratings-service
    public List<RatingsDto> getRatingsByProductId(Long productId){
        RatingsDto[] ratingDtos = webclient.build().get().uri(RATINGS_BY_PRODUCT_ID_URL+productId)
                .accept(MediaType.APPLICATION_JSON).retrieve().bodyToMono(RatingsDto[].class).block();

        return Optional.ofNullable(ratingDtos).map(Arrays::asList).orElse(null);
    }

    public RatingsDto getRatingStatByProductId(Long productId){
        return webclient.build().get().uri(RATINGS_BY_PRODUCT_ID_URL+productId+"/stat")
                .accept(MediaType.APPLICATION_JSON).retrieve().bodyToMono(RatingsDto.class).block();
    }
    // Get Product by product-id from products-service
    public ProductDto getProductById(Long id){
       return webclient.build().get().uri(PRODUCT_API_GATEWAY+"product/"+id).retrieve().bodyToMono(ProductDto.class).blockOptional().orElse(null);
    }

    // Get List of Reviews by product-id
    public List<Review> getReviewsByProductId(Long product_id){
        return new ArrayList<>();
    }

    public UserDto getUserByUserId(Long userid) {
        try {
            return webclient.build().post().uri(APP_USER_BY_USER_ID)
                    .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(objectMapper.writeValueAsString(UserDto.builder().Id(userid).build())))
                    .retrieve().bodyToMono(UserDto.class).block();
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    // Get User buy user-id from app-user-service
    // Get User buy username from app-user-service
    public void addUserToReview(Review review, Long userId){
        UserDto userDto = getUserByUserId(userId);
        review.setUserDto(userDto);
    }
}
