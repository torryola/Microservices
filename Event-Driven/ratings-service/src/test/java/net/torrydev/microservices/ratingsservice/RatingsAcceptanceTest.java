package net.torrydev.microservices.ratingsservice;

import com.google.gson.Gson;
import net.torrydev.microservices.ratingsservice.controller.RatingsController;
import net.torrydev.microservices.ratingsservice.dto.RatingsDto;
import net.torrydev.microservices.ratingsservice.model.Ratings;
import net.torrydev.microservices.ratingsservice.service.RatingServiceImpl;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
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

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RatingsAcceptanceTest {

    @Autowired
    private MockMvc mockMvc;

    private static final String BASE_URL = RatingsController.RATINGS_BASE_URI;

    @Test
    @Order(1)
    void getUsersRatings() throws Exception {
        // Given
        var userId = 2L;
        // mock controller and assert
        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL+"/user/{userid}", userId))
                .andExpect(status().isOk())
                .andExpectAll(jsonPath("$.[0].id", Matchers.is(2)),
                        jsonPath("$.size()", greaterThanOrEqualTo(2)));
    }

    @Test
    @Order(2)
    void getProductRatings() throws Exception {
        // Given
        var prodId = 1L;
        // mock controller and assert
        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL+"/product/{id}", prodId))
                .andExpect(status().isOk())
                .andExpectAll(jsonPath("$.[0].id", Matchers.is(1)),
                        jsonPath("$.size()", greaterThanOrEqualTo(2)));
    }

    @Test
    @Order(3)
    void getProductRatingSum() throws Exception {
        // Given
        var prodId = 3L;

        // mock controller and assert
        mockMvc.perform(get(BASE_URL+"/product/{id}/stat", prodId))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpectAll(jsonPath("$.sum", Matchers.is(8)),
                        jsonPath("$.average", greaterThanOrEqualTo(4.0)));
    }

    @Test
    @Order(4)
    void getRatingsById() throws Exception {
        // Given
        var id = 1;
        // mock controller and assert
        mockMvc.perform(get(BASE_URL+"/rating/{Id}", id).accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpectAll(jsonPath("$.id", equalTo(id)),
                        jsonPath("$.rating", Matchers.not(5)),
                        jsonPath("$.rating", equalTo(4)),
                        jsonPath("$.productid", equalTo(1)));
    }

    @Test @Order(5)
    void createNewRating() throws Exception {
        // Given
        Ratings newRating = Ratings.builder().rating(5).productid(3L).userid(4L).build();
        // mock and assert
        mockMvc.perform(post(BASE_URL+"/rating")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON).content(new Gson().toJson(newRating)))
                .andExpectAll(status().isCreated(),
                        jsonPath("$.id", equalTo(7)),
                        jsonPath("$.rating", equalTo(5)));

    }

    @Test
    @Order(6)
    void getAllRatings() throws Exception {

        mockMvc.perform(get(BASE_URL+"/rating").accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpectAll(status().isOk(), jsonPath("$.size()", equalTo(7)),
                        jsonPath("$.[0].id", equalTo(1)),
                        jsonPath("$.[1].id", equalTo(2)));
    }
}