package net.torrydev.microservices.commentsservice.controller;

import com.google.gson.Gson;
import net.torrydev.microservices.commentsservice.model.Comment;
import net.torrydev.microservices.commentsservice.service.CommentServiceImpl;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
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

@WebMvcTest
@ActiveProfiles("test")
class CommentsControllerTest {

    private static final String BASE_URL = CommentsController.COMMENTS_BASE_URI;

    @MockBean
    CommentServiceImpl commentService;
    @Autowired
    MockMvc mockMvc;

    Comment comment;
    List<Comment> comments = new ArrayList<>();

    @BeforeEach
    void setUp() {
        comment = Comment.builder().userid(2L).productid(2L).Id((long)1001).comment("Nice Product").build();
        comments.add(comment);
    }

    @Test
    void getUsersComments() throws Exception {
        // Given
        var userId = 2;
        when(commentService.findByUserId(anyLong())).thenReturn(comments);
        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL+"/user/{userid}", userId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", Matchers.equalTo(1)))
                .andExpect(jsonPath("$.[0].userid", Matchers.equalTo(userId)));
        // Verify
        verify(commentService).findByUserId(anyLong());

    }

    @Test
    void getProductComments() throws Exception {
        // Given
        var prodId = 2;
        when(commentService.findByProductId(anyLong())).thenReturn(comments);
        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL+"/product/{prodId}", prodId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", Matchers.equalTo(1)))
                .andExpect(jsonPath("$.[0].productid", Matchers.equalTo(prodId)));
        // Verify
        verify(commentService).findByProductId(anyLong());
    }

    @Test
    void getCommentById() throws Exception {
        // Given
        var Id = 1001;
        when(commentService.findByCommentId(anyLong())).thenReturn(comment);
        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL+"/comment/{Id}", Id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.notNullValue()))
                .andExpect(jsonPath("$.id", Matchers.equalTo(Id)));
        // Verify
        verify(commentService).findByCommentId(anyLong());
    }

    @Test
    void createNewComment() throws Exception {
        when(commentService.saveNewComment(any(Comment.class))).thenReturn(comment);
        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL+"/comment")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON).content(new Gson().toJson(comment)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$", Matchers.notNullValue()))
                .andExpect(jsonPath("$.id", Matchers.equalTo(1001)));
        // Verify
        verify(commentService).saveNewComment(any(Comment.class));
    }

    @Test
    void createNewCommentWithBadRequest() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL+"/comment")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON).content((byte[]) null))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}