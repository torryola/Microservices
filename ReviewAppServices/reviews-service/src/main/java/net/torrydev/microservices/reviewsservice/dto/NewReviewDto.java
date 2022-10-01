package net.torrydev.microservices.reviewsservice.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor @NoArgsConstructor @Data @Builder
public class NewReviewDto {
    Long userId; // Reviewer Id
    String comment; // The actual review
    Long productId; // ID of Product to review
    Integer actualRating; // Actual Rating for the product
}
