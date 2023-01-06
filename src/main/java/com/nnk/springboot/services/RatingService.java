package com.nnk.springboot.services;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.exceptions.RatingNotFoundException;
import com.nnk.springboot.repositories.RatingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * CRUD methods for {@link com.nnk.springboot.domain.Rating} entities
 */
@Service
public class RatingService {

    private final RatingRepository ratingRepository;

    public RatingService(RatingRepository ratingRepository) {
        this.ratingRepository = ratingRepository;
    }

    public Rating add(Rating rating) {
        return ratingRepository.save(rating);
    }

    public Rating update(Rating rating) {
        return ratingRepository.save(rating);
    }

    public Rating getById(int id) throws RatingNotFoundException {
        return ratingRepository.findById(id).orElseThrow(RatingNotFoundException::new);
    }
    public List<Rating> getAll() {
        return ratingRepository.findAll();
    }

    public void deleteById(int id) {
        ratingRepository.deleteById(id);
    }

}
