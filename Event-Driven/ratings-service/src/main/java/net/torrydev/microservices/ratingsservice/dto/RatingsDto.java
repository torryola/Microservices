package net.torrydev.microservices.ratingsservice.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import net.torrydev.microservices.ratingsservice.model.Ratings;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RatingsDto {
    Long productid;  // ProductId - id of the product rated
    Integer sum;
    Double average;
    Integer totalratings;
    String message;
    String errormessage;
    List<Ratings> ratings;
}
