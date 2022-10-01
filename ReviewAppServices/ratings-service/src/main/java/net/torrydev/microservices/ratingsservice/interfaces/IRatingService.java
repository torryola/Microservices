package net.torrydev.microservices.ratingsservice.interfaces;

import net.torrydev.microservices.ratingsservice.model.Ratings;

import java.util.List;

public interface IRatingService {

    List<Ratings> findByUserId(Long Id);
    List<Ratings> findByProductId(Long productId);
    Ratings findById(Long Id);
    Ratings saveNewRating(Ratings newRatings);
    Boolean isExistByProductId(Long productId);

}
