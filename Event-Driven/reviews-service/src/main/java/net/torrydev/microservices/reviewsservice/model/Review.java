package net.torrydev.microservices.reviewsservice.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import net.torrydev.microservices.reviewsservice.dto.CommentDto;
import net.torrydev.microservices.reviewsservice.dto.ProductDto;
import net.torrydev.microservices.reviewsservice.dto.RatingsDto;
import net.torrydev.microservices.reviewsservice.dto.UserDto;

import java.math.BigDecimal;
import java.util.Date;

@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor @NoArgsConstructor @Data @Builder
public class Review {
    UserDto userDto;
    CommentDto commentDto;
    ProductDto productDto;
    RatingsDto ratingDto;
    Integer totalrating;
    Double averagerating;
   // Date dateposted;
   // Date dateedited;

    /*
     Get comments and ratings by Product-id 1st
     if comments or ratings isn't null
        Then get userid from comments/ratings to retrieve userdto
     then put together to make review obj
     */
}
