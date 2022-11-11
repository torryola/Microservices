package net.torrydev.microservices.reviewsservice.interfaces;

import net.torrydev.microservices.reviewsservice.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Facade for User-Service related-calls
 * Using Feign Api client will reduce boilerplate that comes with using restTemplate or webClient to
 * make service-to-service call.
 */
//@FeignClient(name = API_GATEWAY)
@FeignClient(name = "APP-USER-SERVICE", url = "${api.gateway.url}"+ UserServiceProxy.USER_SERVICE_API_VERSION)
//All users related requests go through api-gateway
public interface UserServiceProxy {
    String USER_SERVICE_API_VERSION = "/api/v1/users/";
    @PostMapping("user/signup")
    ResponseEntity<?> registerNewUser(@RequestBody UserDto userDto);
    @GetMapping("user/{id}")
    ResponseEntity<?> getUserByUserId(@PathVariable("id") Long userid);
    /*
    User-related EndPoints:
    User's Rating on a particular product
    /users/user/{id}/rating/product/{productid} ==> rating
   User's Review on a particular product
   /users/user/{id}/product/{productid} ==> Review
    */

}
