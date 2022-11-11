package net.torrydev.microservices.reviewsservice.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProductReview {
    List<CommentDto> commentDto; // Comments about this product
    ProductDto productDto; // This product details
    Integer totalrating; // total rating for this product
    Double averagerating; // Average Ratings for this product
    String error;
}
