package com.nnk.springboot.api;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.exceptions.RatingNotFoundException;
import com.nnk.springboot.services.RatingService;
import com.nnk.springboot.services.TestDataService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Unit Tests for {@link com.nnk.springboot.api.RatingApiTest} controller class
 */
@ExtendWith(MockitoExtension.class)
class RatingApiTest {

    @InjectMocks
    private RatingApi controllerUnderTest;
    @Mock
    private RatingService ratingService;

    private TestDataService testDataService = new TestDataService();

    @Test
    void createRatingTest() {
        // Arrange
        Rating testRating = testDataService.makeTestRating();
        // Act
        ResponseEntity<Rating> result = controllerUnderTest.createRating(testRating);
        // Assert
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        verify(ratingService, times(1)).add(testRating);
    }

    @Test
    void getRatingsTest() {
        // Act
        ResponseEntity<List<Rating>> result = controllerUnderTest.getRatings();
        // Assert
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(ratingService, times(1)).getAll();
    }

    @Test
    void getRatingTest() throws RatingNotFoundException {
        // Arrange
        int ratingId = new Random().nextInt();
        // Act
        ResponseEntity<Rating> result = controllerUnderTest.getRating(ratingId);
        // Assert
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(ratingService, times(1)).getById(ratingId);
    }

    @Test
    void updateRatingTest() {
        // Arrange
        Rating testRating = testDataService.makeTestRating();
        // Act
        ResponseEntity<Rating> result = controllerUnderTest.updateRating(testRating);
        // Assert
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(ratingService, times(1)).update(testRating);
    }

    @Test
    void deleteRatingTest() {
        // Arrange
        int ratingId = new Random().nextInt();
        // Act
        ResponseEntity<String> result = controllerUnderTest.deleteRating(ratingId);
        // Assert
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(ratingService, times(1)).deleteById(ratingId);
    }

}
