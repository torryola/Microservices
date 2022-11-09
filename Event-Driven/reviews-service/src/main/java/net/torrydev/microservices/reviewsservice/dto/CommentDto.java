package net.torrydev.microservices.reviewsservice.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CommentDto {
    Long id;
    Long userid;
    String comment;
    Long productid;
}
