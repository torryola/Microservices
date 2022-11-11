package net.torrydev.microservices.commentsservice.repository;

import net.torrydev.microservices.commentsservice.model.Comment;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CommentRepositoryTest {

    @Autowired
    CommentRepository repository;

    Comment comment;
    List<Comment> comments = new ArrayList<>();
    @BeforeEach
    void setUp() {
        comment = Comment.builder().userid(2L).productid(2L).comment("Nice Product").build();
        comments.add(comment);
    }

    @Test
    @Order(1)
    void findByUserId() {
        // Given
        var userId = 2L;
        // Then
        List<Comment> expectedComments = repository.findByUserid(userId);
        int size = expectedComments.size();
        Comment aComment = expectedComments.get(0);
        // Assert
        assertThat(size).isEqualTo(1);
        assertThat(aComment.getUserid()).isEqualTo(userId);
        assertThat(aComment.getComment()).isEqualTo("The product is very good");
    }

    @Test
    @Order(2)
    void findByProductId() {
        // Given
        var prodId = 3L;
        // Then
        List<Comment> expectedComments = repository.findByProductid(prodId);
        int size = expectedComments.size();
        Comment aComment = expectedComments.get(0);
        // Assert
        assertThat(size).isEqualTo(1);
        assertThat(aComment.getProductid()).isEqualTo(prodId);
        assertThat(aComment.getComment()).isEqualTo("Nice product and value for money");
    }

    @Test
    @Order(3)
    void findById() {
        // Given
        var Id = 1L;
        // Then
        Comment aComment = repository.findById(Id).orElse(null);
        // Assert
        assertThat(aComment).isNotNull();
        assertThat(aComment.getProductid()).isEqualTo(Id);
        assertThat(aComment.getComment()).isNotEqualTo("Nice product and value for money");
        assertThat(aComment.getComment()).isEqualTo("Excellent Item");
    }

    @Test
    @Order(4)
    void saveNewComment() {
        Comment aComment = repository.save(comment);
        // Assert
        assertThat(aComment).isNotNull();
        assertThat(aComment.getId()).isEqualTo(5);
        assertThat(aComment.getProductid()).isEqualTo(2L);
        assertThat(aComment.getComment()).isNotEqualTo("Nice product and value for money");
        assertThat(aComment.getComment()).isEqualTo("Nice Product");

    }
}