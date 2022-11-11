package net.torrydev.microservices.appuserservice.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserRequestDto {
    Long Id;
    String name;
    String email;
    String username;
}
