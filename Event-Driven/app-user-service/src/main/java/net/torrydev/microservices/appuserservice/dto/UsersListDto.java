package net.torrydev.microservices.appuserservice.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import net.torrydev.microservices.appuserservice.model.AppUser;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor @NoArgsConstructor
@Builder @Data
public class UsersListDto {
    List<AppUser> appUserList;
}
