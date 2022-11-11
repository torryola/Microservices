package net.torrydev.microservices.appuserservice.controller;

import com.google.gson.Gson;
import net.torrydev.microservices.appuserservice.dto.UsersListDto;
import net.torrydev.microservices.appuserservice.model.AppUser;
import net.torrydev.microservices.appuserservice.service.AppUserServiceImpl;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Arrays;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AppUserController.class)
class AppUserControllerIntegrationTest {

    @MockBean
    private AppUserServiceImpl appUserService;
    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName(" Returns List of all Users")
    void getAllUsers() throws Exception {

        AppUser user1 = AppUser.builder().name("Tester").email("tester@email.com").username("testerDemo").build();
        AppUser user2 = AppUser.builder().name("Tester1").email("tester1@email.com").username("testerDemo1").build();

        // Mock the Service Response
        when(appUserService.allUsers()).thenReturn(UsersListDto.builder()
                        .appUserList(Arrays.asList(user1, user2)).build());

        mockMvc.perform(get(AppUserController.APP_USER_BASE_URI+"/"))
                .andExpect(status().is(200))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.appUserList.size()", Matchers.is(2)))
                .andExpect(jsonPath("$.appUserList[0].name", Matchers.equalTo("Tester")))
                .andExpect(jsonPath("$.appUserList[0].username", Matchers.equalTo("testerDemo")))
                .andExpect(jsonPath("$.appUserList[1].username", Matchers.equalTo("testerDemo1")));
    }

    @Test
    @DisplayName(" Get App user by UserName")
    void getUserByUsername() throws Exception {
        AppUser user = AppUser.builder().name("Tester").email("tester@email.com").username("testerDemo").build();
        // Mock the Service
        when(appUserService.findByUsername("testerDemo")).thenReturn(user);
        // mock the controller
        mockMvc.perform(get(AppUserController.APP_USER_BASE_URI+"/user/username/{username}", user.getUsername()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", Matchers.equalTo("Tester")));
    }

    @Test
    @DisplayName(" Get App user by Unknown UserName")
    void getUserByUsernameThrowsException() throws Exception {
        AppUser user = AppUser.builder().name("Tester").email("tester@email.com").username("testerDemo").build();
        // Mock the Service
        when(appUserService.findByUsername("")).thenThrow(MethodArgumentTypeMismatchException.class);
        // mock the controller
        mockMvc.perform(get(AppUserController.APP_USER_BASE_URI+"/user/username/{username}", "")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertThat(result.getResolvedException() instanceof MethodArgumentTypeMismatchException).isTrue());
    }

    @Test
    @DisplayName("Get App User by Id")
    void findUserById() throws Exception {
        long id = 1001;
        AppUser user = AppUser.builder().name("Tester").email("tester@email.com").username("testerDemo").Id(id).build();
        // Mock Service
        when(appUserService.findById(id)).thenReturn(user);
        // Mock Controller
        mockMvc.perform(get(AppUserController.APP_USER_BASE_URI+"/user/{id}",user.getId()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").exists())
                .andDo(print())
                .andExpect(jsonPath("$.name", Matchers.equalTo(user.getName())))
                .andExpect(jsonPath("$.id").exists());

    }

    @Test
    void registerNewUser() throws Exception {
        // Given
        AppUser newUser = AppUser.builder().name("Tester").email("tester@email.com").username("testerDemo").build();
        // When
        when(appUserService.registerNewUser(newUser)).thenReturn(newUser);

        mockMvc.perform(post(AppUserController.APP_USER_BASE_URI  + "/user/signup").accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON).content(new Gson().toJson(newUser)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$", Matchers.equalTo("http://localhost/api/v1/users/user/")));


    }
}