package net.torrydev.microservices.commentsservice;

import com.google.gson.Gson;
import net.torrydev.microservices.commentsservice.controller.CommentsController;
import net.torrydev.microservices.commentsservice.model.Comment;
import net.torrydev.microservices.commentsservice.service.CommentServiceImpl;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AcceptanceTest {

    private static final String BASE_URL = CommentsController.COMMENTS_BASE_URI;

    @Autowired
    MockMvc mockMvc;

    Comment comment;

    @Test
    @Order(1)
    void getUsersComments() throws Exception {
        // Given
        var userId = 2;
        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL+"/user/{userid}", userId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", Matchers.equalTo(1)))
                .andExpect(jsonPath("$.[0].userid", Matchers.equalTo(userId)));

    }

    @Test
    @Order(2)
    void getProductComments() throws Exception {
        // Given
        var prodId = 2;
        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL+"/product/{prodId}", prodId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", Matchers.equalTo(1)))
                .andExpect(jsonPath("$.[0].productid", Matchers.equalTo(prodId)));
    }

    @Test
    @Order(3)
    void getCommentById() throws Exception {
        // Given
        var Id = 4;
        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL+"/comment/{Id}", Id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.notNullValue()))
                .andExpect(jsonPath("$.id", Matchers.equalTo(Id)));
    }

    @Test
    @Order(4)
    void createNewComment() throws Exception {
        // Given
        comment = Comment.builder().userid(2L).productid(2L).Id((long)1001).comment("Nice Product").build();

        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL+"/comment")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON).content(new Gson().toJson(comment)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$", Matchers.notNullValue()))
                .andExpect(jsonPath("$.id", Matchers.equalTo(5)));
    }

    @Test
    @Order(5)
    void createNewCommentWithBadRequest() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL+"/comment")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON).content((byte[]) null))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}