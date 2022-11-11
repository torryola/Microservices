package net.torrydev.microservices.appuserservice;

import com.google.gson.Gson;
import net.torrydev.microservices.appuserservice.controller.AppUserController;
import net.torrydev.microservices.appuserservice.model.AppUser;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
class AppUserAcceptanceTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName(" Returns List of all Users")
    @Order(1)
    @Sql("classpath:test-data.sql")
    void getAllUsers() throws Exception {

        mockMvc.perform(get(AppUserController.APP_USER_BASE_URI+"/"))
                .andExpect(status().is(200))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.appUserList.size()", Matchers.is(3)))
                .andExpect(jsonPath("$.appUserList[0].name", Matchers.equalTo("Tester")))
                .andExpect(jsonPath("$.appUserList[0].username", Matchers.equalTo("demoTester")))
                .andExpect(jsonPath("$.appUserList[1].username", Matchers.equalTo("demoTester2")));
    }

    @Test
    @DisplayName(" Get App user by UserName")
    @Order(2)
    void getUserByUsername() throws Exception {
        mockMvc.perform(get(AppUserController.APP_USER_BASE_URI+"/user/username/{username}", "demoTester").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", Matchers.equalTo("Tester")));
    }

    @Test
    @DisplayName(" Get App user by Unknown UserName")
    @Order(3)
    void getUserByUsernameThrowsException() throws Exception {
        mockMvc.perform(get(AppUserController.APP_USER_BASE_URI+"/user/username/{username}", "")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertThat(result.getResolvedException() instanceof MethodArgumentTypeMismatchException).isTrue());
    }

    @Test
    @DisplayName("Get App User by Id")
    @Order(4)
    void findUserById() throws Exception {
        long id = 1001;
        mockMvc.perform(get(AppUserController.APP_USER_BASE_URI+"/user/{id}",id).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").exists())
                .andDo(print())
                .andExpect(jsonPath("$.name", Matchers.equalTo("Tester")))
                .andExpect(jsonPath("$.id").exists());

    }

    @Test
    @DisplayName("Register New App user")
    @Order(5)
    void registerNewUser() throws Exception {
        // Given
        AppUser newUser = AppUser.builder().name("Tester4").email("tester4@email.com").username("testerDemo4").build();

        mockMvc.perform(post(AppUserController.APP_USER_BASE_URI  + "/user/signup").accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON).content(new Gson().toJson(newUser)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$", Matchers.equalTo("http://localhost/api/v1/users/user/1")));


    }
}