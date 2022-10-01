package net.torrydev.microservices.appuserservice.controller;

import net.torrydev.microservices.appuserservice.dto.UsersListDto;
import net.torrydev.microservices.appuserservice.model.AppUser;
import net.torrydev.microservices.appuserservice.service.AppUserServiceImpl;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.annotation.security.RunAs;

import java.util.Arrays;
import java.util.regex.Matcher;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(AppUserController.class)
class AppUserControllerTest {

    @MockBean
    private AppUserServiceImpl appUserService;
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName(" Returns List of all Users")
    void getAllUsers() throws Exception {

        AppUser user1 = AppUser.builder().name("Tester").email("tester@email.com").username("testerDemo").build();
        AppUser user2 = AppUser.builder().name("Tester1").email("tester1@email.com").username("testerDemo1").build();

        // Mock the Service Response
        when(appUserService.allUsers()).thenReturn(UsersListDto.builder()
                        .appUserList(Arrays.asList(user1, user2)).build());

        mockMvc.perform(MockMvcRequestBuilders.get(AppUserController.APP_USER_BASE_URI+"/"))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.appUserList.size()", Matchers.is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.appUserList[0].name", Matchers.equalTo("Tester")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.appUserList[0].username", Matchers.equalTo("testerDemo")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.appUserList[1].username", Matchers.equalTo("testerDemo1")));
    }

    @Test
    @DisplayName(" Get App user by UserName")
    void getUserByUsername() throws Exception {
        AppUser user = AppUser.builder().name("Tester").email("tester@email.com").username("testerDemo").build();
        // Mock the Service
        when(appUserService.findByUsername("testerDemo")).thenReturn(user);
        // mock the controller
        mockMvc.perform(MockMvcRequestBuilders.get(AppUserController.APP_USER_BASE_URI+"/user/username/{username}", user.getUsername()).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.equalTo("Tester")));
    }

    @Test
    @DisplayName(" Get App user by Unknown UserName")
    void getUserByUsernameThrowsException() throws Exception {
        AppUser user = AppUser.builder().name("Tester").email("tester@email.com").username("testerDemo").build();
        // Mock the Service
        when(appUserService.findByUsername("testerDemo")).thenReturn(user);
        // mock the controller
        mockMvc.perform(MockMvcRequestBuilders.get(AppUserController.APP_USER_BASE_URI+"/user/username/{username}", "")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(result -> assertThat(result.getResolvedException() instanceof IllegalArgumentException));
    }

    @Test
    @DisplayName("Get App User by Id")
    void findUserById() throws Exception {
        long id = 1001;
        AppUser user = AppUser.builder().name("Tester").email("tester@email.com").username("testerDemo").Id(id).build();
        // Mock Service
        when(appUserService.findById(id)).thenReturn(user);
        // Mock Controller
        mockMvc.perform(MockMvcRequestBuilders.get(AppUserController.APP_USER_BASE_URI+"/user/{id}",user.getId()).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$").exists())
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.equalTo(user.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());

    }

    @Test
    void getUserById() {
    }

    @Test
    void registerNewUser() {
    }
}