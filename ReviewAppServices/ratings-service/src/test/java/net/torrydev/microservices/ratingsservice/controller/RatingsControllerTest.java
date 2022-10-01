package net.torrydev.microservices.ratingsservice.controller;

import com.google.gson.Gson;
import net.torrydev.microservices.ratingsservice.dto.RatingsDto;
import net.torrydev.microservices.ratingsservice.model.Ratings;
import net.torrydev.microservices.ratingsservice.service.RatingServiceImpl;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest
class RatingsControllerTest {

    // Dependencies
    @MockBean
    private RatingServiceImpl ratingService;
    @Autowired
    private MockMvc mockMvc;

    private static final String BASE_URL = RatingsController.RATINGS_BASE_URI;
    private Ratings ratings;
    private List<Ratings> ratingsList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        ratings = Ratings.builder().Id(101L).rating(3).productid(2L).userid(4L).build();
        ratingsList.add(ratings);
    }


    @Test
    void getUsersRatings() throws Exception {
        // Given
        var userId = 4L;
        when(ratingService.findByUserId(userId)).thenReturn(ratingsList);
        // mock controller and assert
        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL+"/user/{userid}", userId))
                .andExpect(status().isOk())
                .andExpectAll(jsonPath("$.[0].id", Matchers.is(101)),
                        jsonPath("$.size()", greaterThanOrEqualTo(1)));
        verify(ratingService).findByUserId(ArgumentMatchers.anyLong());
    }

    @Test
    void getProductRatings() throws Exception {
        // Given
        var prodId = 2L;
        when(ratingService.findByProductId(prodId)).thenReturn(ratingsList);
        // mock controller and assert
        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL+"/product/{id}", prodId))
                .andExpect(status().isOk())
                .andExpectAll(jsonPath("$.[0].id", Matchers.is(101)),
                        jsonPath("$.size()", greaterThanOrEqualTo(1)));
        verify(ratingService).findByProductId(ArgumentMatchers.anyLong());
    }

    @Test
    void getProductRatingSum() throws Exception {
        // Given
        var prodId = 2L;
        var totalRatings = ratingsList.size();
        var sum = ratingsList.get(0).getRating();
        var avg = (double)(sum / totalRatings);

        RatingsDto dto = RatingsDto.builder().productid(prodId).sum(sum).average(avg).totalratings(totalRatings)
                .ratings(ratingsList)
                .message("Successful").build();

        when(ratingService.getRatingSumByProductId(prodId)).thenReturn(dto);

        // mock controller and assert
        mockMvc.perform(get(BASE_URL+"/product/{id}/stat", prodId))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpectAll(jsonPath("$.sum", Matchers.is(sum)),
                        jsonPath("$.average", greaterThanOrEqualTo(avg)));
        // Verify
        verify(ratingService).getRatingSumByProductId(ArgumentMatchers.anyLong());
    }

    @Test
    void getRatingsById() throws Exception {
        // Given
        var id = 101;
        when(ratingService.findById((long) id)).thenReturn(ratings);

        // mock controller and assert
        mockMvc.perform(get(BASE_URL+"/rating/{Id}", id).accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpectAll(jsonPath("$.id", equalTo(id)),
                        jsonPath("$.rating", Matchers.not(4)),
                        jsonPath("$.productid", equalTo(2)));
        // verify
        verify(ratingService).findById(anyLong());
    }

    @Test
    void createNewRating() throws Exception {
        // Given
        Ratings newRating = Ratings.builder().Id(102L).rating(5).productid(3L).userid(4L).build();
        when(ratingService.saveNewRating(ArgumentMatchers.any(Ratings.class))).thenReturn(newRating);
        // mock and assert
        mockMvc.perform(post(BASE_URL+"/rating")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON).content(new Gson().toJson(newRating)))
                .andExpect(status().isCreated())
                .andExpectAll(jsonPath("$.id", equalTo(102)),
                        jsonPath("$.rating", equalTo(5)));
        // Verify
        verify(ratingService).saveNewRating(any(Ratings.class));

    }

    @Test
    void getAllRatings() throws Exception {
        Ratings newRating = Ratings.builder().Id(102L).rating(5).productid(3L).userid(4L).build();
        ratingsList.add(newRating);

        when(ratingService.getAllRatings()).thenReturn(ratingsList);

        mockMvc.perform(get(BASE_URL+"/rating").accept(MediaType.APPLICATION_JSON))
                .andExpectAll(status().isOk(), jsonPath("$.size()", equalTo(2)),
                        jsonPath("$.[0].id", equalTo(101)),
                        jsonPath("$.[1].id", equalTo(102)));
    }
}