package net.torrydev.microservices.reviewsservice.dto;


import lombok.*;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class RatingDto {
    Long Id;
    Long userid;
    Integer rating;
}
