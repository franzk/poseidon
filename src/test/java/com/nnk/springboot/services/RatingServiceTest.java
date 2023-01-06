package com.nnk.springboot.services;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.exceptions.RatingNotFoundException;
import com.nnk.springboot.repositories.RatingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

/**
 * Unit Tests for  {@link com.nnk.springboot.services.RatingService} service class
 */
@ExtendWith(MockitoExtension.class)
class RatingServiceTest {

    @InjectMocks
    RatingService serviceUnderTest;

    @Mock
    RatingRepository ratingRepository;

    private final TestDataService testDataService = new TestDataService();


    @Test
    void addTest() {
        // Arrange
        Rating testRating = testDataService.makeTestRating();
        // Act
        serviceUnderTest.add(testRating);
        // Assert
        verify(ratingRepository, times(1)).save(testRating);
    }

    @Test
    void updateTest() {
        // Arrange
        Rating testRating = testDataService.makeTestRating();
        // Act
        serviceUnderTest.update(testRating);
        // Assert
        verify(ratingRepository, times(1)).save(testRating);
    }

    @Test
    void getAllTest() {
        // Arrange
        // Act
        serviceUnderTest.getAll();
        // Assert
        verify(ratingRepository, times(1)).findAll();
    }

    @Test
    void getByIdTest() throws RatingNotFoundException {
        // Arrange
        Rating testRating = testDataService.makeTestRating();
        int ratingId = testRating.getId();
        when(ratingRepository.findById(anyInt())).thenReturn(Optional.of(testRating));
        // Act
        serviceUnderTest.getById(ratingId);
        // Assert
        verify(ratingRepository, times(1)).findById(ratingId);
    }

    @Test
    void getByIdWithExceptionTest()  {
        // Arrange
        int id = new Random().nextInt();
        // Act + Assert
        assertThrows(RatingNotFoundException.class, () -> serviceUnderTest.getById(id));
    }

    @Test
    void deleteByIdTest() {
        // Arrange
        int id = new Random().nextInt();
        // Act
        serviceUnderTest.deleteById(id);
        // Assert
        verify(ratingRepository, times(1)).deleteById(id);
    }

}
