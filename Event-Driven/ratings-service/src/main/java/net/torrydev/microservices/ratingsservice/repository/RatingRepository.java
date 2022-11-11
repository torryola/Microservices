package net.torrydev.microservices.ratingsservice.repository;

import net.torrydev.microservices.ratingsservice.model.Ratings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingRepository extends JpaRepository<Ratings, Long> {

    List<Ratings> findByProductid(Long productId);
    List<Ratings> findByUserid(Long userId);
    Boolean existsByProductid(Long productId);
    Ratings findByUseridAndProductid(Long userId, Long productId);
}
