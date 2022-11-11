package net.torrydev.microservices.ratingsservice.service;

import lombok.extern.slf4j.Slf4j;
import net.torrydev.microservices.ratingsservice.dto.RatingsDto;
import net.torrydev.microservices.ratingsservice.interfaces.IRatingService;
import net.torrydev.microservices.ratingsservice.model.Ratings;
import net.torrydev.microservices.ratingsservice.repository.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class RatingServiceImpl implements IRatingService {

    @Autowired
    RatingRepository ratingRepository;

    @Override
    public List<Ratings> findByUserId(Long userId) {
        if (null == userId || userId <= 0)
            throw new IllegalArgumentException("Invalid UserId");
        return ratingRepository.findByUserid(userId);
    }

    private List<Ratings> findListOfRatingsByProductId(Long productId) {
      return ratingRepository.findByProductid(productId);
    }

    public List<Ratings> getAllRatings() {
        return ratingRepository.findAll();
    }


    @Override
    public List<Ratings> findByProductId(Long productId) {
        if (null == productId || productId <= 0)
            throw new IllegalArgumentException("Invalid Product Id");
        return ratingRepository.findByProductid(productId);
    }

    @Override
    public Ratings findById(Long Id) {
        return ratingRepository.findById(Id).orElse(null);
    }

    @Override
    public Ratings saveNewRating(Ratings newRatings) {
        if (newRatings == null)
            return null;
        // Check if user already rated the product
        Ratings existingRating = findByUserIdAndProductId(newRatings.getUserid(), newRatings.getProductid());
        // Update existing Rating
        if (existingRating != null){
            existingRating.setRating(newRatings.getRating());
            return ratingRepository.save(existingRating);
        }
        return ratingRepository.save(newRatings);
    }
    @Override
    public Boolean isExistByProductId(Long productId) {
        return ratingRepository.existsByProductid(productId);
    }

    public RatingsDto getRatingSumByProductId(Long prodId){
        if (null == prodId || prodId <= 0)
           return noRating(prodId);

        List<Ratings> ratings = findByProductId(prodId);
        if (!ratings.isEmpty()) {
            int sumRatings = ratings.stream().filter(Objects::nonNull).map(Ratings::getRating).reduce(Integer::sum).orElse(0);

           return RatingsDto.builder().productid(prodId).sum(sumRatings).average(format_2_Decimal((double) sumRatings / ratings.size())).totalratings(ratings.size())
                    .message("Successful").build();
        }
        else return noRating(prodId);
    }

    // For Testing Only - No Bulk Rating is Allowed
    public void saveAll(List<Ratings> ratings){
        for (Ratings rating : ratings) {
            Ratings existingRating = findByUserIdAndProductId(rating.getUserid(), rating.getProductid());
            if ( existingRating != null) {
                existingRating.setRating(rating.getRating());
                ratingRepository.save(existingRating);
            }
            else
                ratingRepository.save(rating);
        }
    }

    // Average Ratings by Product Id - using custom Query at repository Level can be neater or efficient
    public double getAvgRatingsByProductId(Long id){
        List<Ratings> ratings = findByProductId(id);
        return Optional.ofNullable(ratings).map(list -> ratings.stream().map(Ratings::getRating).reduce(Integer::sum).map(total -> (double)total/ratings.size()).orElse(0d)).orElse(0d);
    }

    public Ratings findByUserIdAndProductId(Long userid, Long productid) {
       return ratingRepository.findByUseridAndProductid(userid, productid);
    }

    private static double format_2_Decimal(double val){
        return new BigDecimal(val).setScale(2, RoundingMode.FLOOR).doubleValue();
    }

    private static RatingsDto noRating(Long prodId){
        return RatingsDto.builder().sum(0).average(0D).totalratings(0).errormessage(String.format("Ratings for Product %d not found", prodId))
                .message("Unsuccessful").build();
    }

}
