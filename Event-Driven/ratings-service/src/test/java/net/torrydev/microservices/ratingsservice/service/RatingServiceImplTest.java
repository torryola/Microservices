package net.torrydev.microservices.ratingsservice.service;

import net.torrydev.microservices.ratingsservice.model.Ratings;
import net.torrydev.microservices.ratingsservice.repository.RatingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RatingServiceImplTest {

    // Dependencies
    @Mock
    private RatingRepository repository;

    // Under Test
    @InjectMocks
    private RatingServiceImpl ratingService;

    private Ratings ratings;
    private List<Ratings> ratingsList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        ratings = Ratings.builder().Id(101L).rating(3).productid(2L).userid(4L).build();
        ratingsList.add(ratings);
    }

    @Test
    void findByUserId() {
        // Given
        var userId = 4L;
        // When
        when(repository.findByUserid(userId)).thenReturn(ratingsList);
        // Then
        List<Ratings> list = ratingService.findByUserId(userId);
        // Verify
        verify(repository).findByUserid(ArgumentMatchers.anyLong());
        // Assert
        assertThat(list).isNotNull();
        assertThat(list.size()).isGreaterThan(0);
    }

    @Test
    void findByInvalidUserId() {
        // Assert With Exception
        List.of(-100L, 0L).forEach(val -> assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ratingService.findByUserId(val))
                .withMessage("Invalid UserId")
        );

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ratingService.findByUserId(null))
                .withMessage("Invalid UserId");
    }

    @Test
    void getAllRatings() {
        // When
        when(repository.findAll()).thenReturn(ratingsList);
        // Then
        List<Ratings> list = ratingService.getAllRatings();
        // Verify
        verify(repository).findAll();
        // Assert
        assertThat(list).isNotNull();
        assertThat(list.size()).isGreaterThan(0);
    }

    @Test
    void findByProductId() {
        // When
        when(repository.findAll()).thenReturn(ratingsList);
        // Then
        List<Ratings> list = ratingService.getAllRatings();
        // Verify
        verify(repository).findAll();
        // Assert
        assertThat(list).isNotNull();
        assertThat(list.size()).isGreaterThan(0);
    }

    @Test
    void findByInvalidProductId() {
        // Assert With Exception
        List.of(-100L, 0L).forEach(val -> assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ratingService.findByProductId(val))
                .withMessage("Invalid Product Id")
        );

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ratingService.findByProductId(null))
                .withMessage("Invalid Product Id");
    }

    @Test
    void findById() {
        // Given
        var Id = 101L;
        // When
        when(repository.findById(Id)).thenReturn(Optional.of(ratings));
        // Then
        Ratings rating = ratingService.findById(Id);
        // Verify
        verify(repository).findById(ArgumentMatchers.anyLong());
        // Assert
        assertThat(rating).isNotNull();
        assertThat(rating.getRating()).isGreaterThan(0);
    }

    @Test
    void saveNewRating() {

        when(repository.save(ArgumentMatchers.any(Ratings.class))).thenReturn(ratings);
        // Then
        Ratings newRating = ratingService.saveNewRating(ratings);
        // Verify
        verify(repository).save(ArgumentMatchers.any(Ratings.class));
        // assert
        assertThat(newRating).isNotNull();
        assertThat(newRating.getUserid()).isEqualTo(4L);
        // Check with null ratings
        assertThat(ratingService.saveNewRating(null)).isNull();
    }

    @Test
    void isExistByProductId() {
        when(repository.existsByProductid(ratings.getProductid())).thenReturn(true);
        var isExisted = ratingService.isExistByProductId(2L);
        assertTrue(isExisted);
        verify(repository).existsByProductid(ArgumentMatchers.anyLong());
    }
}