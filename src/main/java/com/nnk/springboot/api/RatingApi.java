package com.nnk.springboot.api;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.exceptions.RatingNotFoundException;
import com.nnk.springboot.services.RatingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * CRUD Api controller for {@link com.nnk.springboot.domain.Rating} entities
 *
 */
@RestController
@RequestMapping("/api/rating")
public class RatingApi {

    private final RatingService ratingService;

    public RatingApi(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @PostMapping("")
    public ResponseEntity<Rating> createRating(@RequestBody @Valid Rating rating) {
        return new ResponseEntity<>(ratingService.add(rating), HttpStatus.CREATED);
    }

    @GetMapping("/list")
    public ResponseEntity<List<Rating>> getRatings() {
        return new ResponseEntity<>(ratingService.getAll(), HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<Rating> getRating(@RequestParam Integer id) throws RatingNotFoundException {
        return new ResponseEntity<>(ratingService.getById(id), HttpStatus.OK);
    }

    @PutMapping("")
    public ResponseEntity<Rating> updateRating(@RequestBody @Valid Rating rating) {
        return new ResponseEntity<>(ratingService.update(rating), HttpStatus.OK);
    }

    @DeleteMapping("")
    public ResponseEntity<String> deleteRating(@RequestParam Integer id) {
        ratingService.deleteById(id);
        return new ResponseEntity<>("Rating deleted !", HttpStatus.OK);
    }

}
