package net.torrydev.microservices.reviewsservice.dto;


import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class RatingsDto {
    Long productid;  // ProductId - id of the product rated
    Integer sum;
    Double average;
    Integer totalratings;
    String message;
    String errormessage;
    List<Ratings> ratings;

    @FieldDefaults(level = AccessLevel.PRIVATE)
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class Ratings{
        Long Id;  // Rate_Id
        Long userid; // i.e. the rater
        Integer rating;
        Long productid;  // ProductId - id of the product rated
    }
}
