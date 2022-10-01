package net.torrydev.microservices.commentsservice.service;

import net.torrydev.microservices.commentsservice.model.Comment;
import net.torrydev.microservices.commentsservice.repository.CommentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {

    // Dependencies
    @Mock
    CommentRepository repository;
    @InjectMocks
    CommentServiceImpl commentService;

    Comment comment;
    List<Comment> comments = new ArrayList<>();

    @BeforeEach
    void setUp() {
        comment = Comment.builder().userid(2L).productid(2L).comment("Nice Product").build();
        comments.add(comment);
    }

    @Test
    void findByUserId() {
        // Given
        var userId = 2L;
        // When
        when(repository.findByUserid(userId)).thenReturn(comments);
        // Then
        List<Comment> expectedComments = commentService.findByUserId(userId);
        int size = expectedComments.size();
        assertThat(size).isGreaterThan(0);
    }

    @Test
    void findByUserIdThrowsException() {
        // Given
        var userId = -2L;
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> commentService.findByUserId(userId))
                .withMessageContaining("Invalid userId");
    }

    @Test
    void findByProductId() {
        // Given
        var proId = 2L;
        // When
        when(repository.findByProductid(proId)).thenReturn(comments);
        // Then
        List<Comment> expectedComments = commentService.findByProductId(proId);
        int size = expectedComments.size();
        assertThat(size).isGreaterThan(0);
    }

    @Test
    void findByProductIdThrowException() {
        // Given
        var proId = -2L;

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> commentService.findByProductId(proId))
                .withMessageContaining("Invalid productId");
    }

    @Test
    void findByCommentId() {
        // Given
        var Id = 2L;
        // When
        when(repository.findById(Id)).thenReturn(Optional.of(comment));
        // Then
        Comment expectedComment = commentService.findByCommentId(Id);
        // Assert
        assertThat(expectedComment).isNotNull();
        assertThat(expectedComment.getProductid()).isEqualTo(comment.getProductid());
    }

    @Test
    void findByCommentIdThrowException() {
        // Given
        var Id = -2L;
       assertThatExceptionOfType(IllegalArgumentException.class)
               .isThrownBy(() -> commentService.findByCommentId(Id))
               .withMessageContaining("Invalid commentId");
    }

    @Test
    void saveNewComment() {
        when(repository.save(ArgumentMatchers.any(Comment.class))).thenReturn(comment);
        Comment expectedComment = commentService.saveNewComment(comment);
        assertThat(expectedComment).isNotNull();
        verify(repository).save(any(Comment.class));
    }
}