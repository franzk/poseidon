package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.exceptions.RatingNotFoundException;
import com.nnk.springboot.services.AuthService;
import com.nnk.springboot.services.RatingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.Random;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

/**
 * Unit Tests for  {@link com.nnk.springboot.controllers.RatingController} Thymeleaf controller class
 */
@ExtendWith(MockitoExtension.class)
class RatingControllerTest {

    @InjectMocks
    private RatingController controllerUnderTest;

    @Mock
    private RatingService ratingService;

    @Mock
    private AuthService authService;

    @Mock
    private Model model;

    @Test
    void homeTest() {
        // Act
        controllerUnderTest.home(model);
        // Assert
        verify(model, times(2)).addAttribute(anyString(), any());
        verify(authService, times(1)).getLoggedUser();
    }

    @Test
    void addRatingFormTest() {
        // Act
        controllerUnderTest.addRatingForm(model);
        // Assert
        verify(model, times(1)).addAttribute(any());
    }

    @Test
    void validateTest() {
        // Arrange
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(false);
        // Act
        controllerUnderTest.validate(new Rating(), result, model);
        // Assert
        verify(ratingService, times(1)).add(any());

    }

    @Test
    void validateWithErrorsTest() {
        // Arrange
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(true);
        // Act
        controllerUnderTest.validate(new Rating(), result, model);
        // Assert
        verify(ratingService, times(0)).add(any());

    }

    @Test
    void showUpdateFormTest() throws RatingNotFoundException {
        // Arrange
        int id = new Random().nextInt();
        // Act
        controllerUnderTest.showUpdateForm(id, model);
        // Assert
        verify(model, times(1)).addAttribute(any());

    }

    @Test
    void updateRatingTest() {
        // Arrange
        int id = new Random().nextInt();
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(false);
        // Act
        controllerUnderTest.updateRating(id, new Rating(), result, model);
        // Assert
        verify(ratingService, times(1)).update(any());
    }

    @Test
    void updateRatingWithErrorsTest() {
        // Arrange
        int id = new Random().nextInt();
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(true);
        // Act
        controllerUnderTest.updateRating(id, new Rating(), result, model);
        // Assert
        verify(ratingService, times(0)).update(any());
        verify(model, times(1)).addAttribute(any());
    }

    @Test
    void deleteRatingTest() {
        // Arrange
        int id = new Random().nextInt();
        // Act
        controllerUnderTest.deleteRating(id, model);
        // Assert
        verify(ratingService, times(1)).deleteById(id);
    }
}
