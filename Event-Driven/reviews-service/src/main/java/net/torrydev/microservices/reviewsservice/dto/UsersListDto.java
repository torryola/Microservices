package net.torrydev.microservices.reviewsservice.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UsersListDto {
    /*
     Note this reference name must MATCH what's coming in.
     E.g. if the reference name to the list from the response is userList
     and this variable is appUserList then it won't match
     */
    String error;
    List<UserDto> appUserList;
}
