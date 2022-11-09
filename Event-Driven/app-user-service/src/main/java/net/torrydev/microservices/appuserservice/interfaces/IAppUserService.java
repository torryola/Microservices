package net.torrydev.microservices.appuserservice.interfaces;

import net.torrydev.microservices.appuserservice.dto.UsersListDto;
import net.torrydev.microservices.appuserservice.model.AppUser;

public interface IAppUserService {

    AppUser findByUsername(String username);
    AppUser registerNewUser(AppUser appUser);
    Long findIdByUsername(String username);
    AppUser findById(Long Id);
    Boolean isExistsByUsername(String username);
    Boolean isExistsById(Long Id);
    UsersListDto allUsers();
}
