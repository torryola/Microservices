package net.torrydev.microservices.ratingsservice.repository;

import net.torrydev.microservices.ratingsservice.model.Ratings;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RatingRepositoryTest {

    @Autowired
    private RatingRepository repository;

    @BeforeEach
    void setUp() {
    }

    @Test
    @Order(1)
    void findByProductid() {
        List<Ratings> ratings = repository.findByProductid(1L);
        assertEquals(2, ratings.size());
        assertThat(ratings.get(0).getRating()).isEqualTo(4);
    }

    @Test
    @Order(2)
    void findByUserid() {
        List<Ratings> ratings = repository.findByUserid(2L);
        assertThat(ratings.size()).isEqualTo(2);
        assertThat(ratings.get(0).getUserid()).isEqualTo(2);
    }

    @Test
    @Order(3)
    void existsByProductid() {
        assertTrue(repository.existsByProductid(1L));
        assertTrue(repository.existsByProductid(2L));
        assertFalse(repository.existsByProductid(300L));
    }

    @Test
    @Order(4)
    void findByUseridAndProductid() {
        Ratings ratings = repository.findByUseridAndProductid(1L, 1L);
        assertThat(ratings).isNotNull();
        assertThat(ratings.getRating()).isEqualTo(4);

        assertThat(repository.findByUseridAndProductid(100L, 100L)).isNull();
    }

    @Test
    @Order(5)
    void createNewRatings(){
        Ratings myRating = Ratings.builder().rating(3).productid(2L).userid(4L).build();
        Ratings newRating = repository.save(myRating);
        assertThat(newRating.getId()).isGreaterThan(0L);
    }
}