package net.torrydev.microservices.reviewsservice.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProductDto {
    Long id;
    String name;
    String decs;
    String picurl;
    String brand;
    String category;
    BigDecimal price;
}
