package net.torrydev.microservices.appuserservice.controller;

import lombok.extern.slf4j.Slf4j;
import net.torrydev.microservices.appuserservice.dto.UserRequestDto;
import net.torrydev.microservices.appuserservice.dto.UsersListDto;
import net.torrydev.microservices.appuserservice.model.AppUser;
import net.torrydev.microservices.appuserservice.service.AppUserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

import static net.torrydev.microservices.appuserservice.constants.ServiceConstants.REST_API;

@Slf4j
@RestController
@RequestMapping(path = AppUserController.APP_USER_BASE_URI)
public class AppUserController {
    public static final String APP_USER_BASE_URI = REST_API + "/users";
    @Value("${service.welcome.msg}")
    String msg;

    @Autowired
    AppUserServiceImpl appUserService;

    @GetMapping(path = "/welcome")
    public String welcomeMsg(){
        return msg;
    }

    @GetMapping("/")
    public UsersListDto getAllUsers(){
        log.info("getAllUsers is called");
       return appUserService.allUsers();
    }

    @GetMapping("/user/username/{username}")
    public ResponseEntity<?> getUserByUsername(@PathVariable("username") String username){
        if (username == null || username.isBlank())
            throw new IllegalArgumentException("Please provide valid username");
        log.trace("getUserByUsername {}", username);
        AppUser user = appUserService.findByUsername(username);
        return ResponseEntity.ok().body(user);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<?> findUserById(@PathVariable("id") Long Id){
        AppUser user = appUserService.findById(Id);
        log.trace("findUserById {}",Id);
        return ResponseEntity.ok().body(user);
    }

    @PostMapping("/user/id/")
    public ResponseEntity<?> getUserById(@RequestBody UserRequestDto dto){
        AppUser user = appUserService.findById(dto.getId());
        log.trace("findUserById {}",dto);
        return ResponseEntity.ok().body(user);
    }

    @PostMapping("/user/signup")
    public ResponseEntity<?> registerNewUser(@RequestBody AppUser newUser){
        AppUser user = appUserService.registerNewUser(newUser);
        if (user == null) {
            log.trace("registerNewUser failed {}", newUser);
            return ResponseEntity.badRequest().body("User Not Created");
        }
        else {
            URI uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path(APP_USER_BASE_URI+"/user/{id}").buildAndExpand(user.getId()).toUri();
            log.trace("New User registration successful {}", uri);
            return ResponseEntity.status(HttpStatus.CREATED).body(uri);
        }
    }

}
