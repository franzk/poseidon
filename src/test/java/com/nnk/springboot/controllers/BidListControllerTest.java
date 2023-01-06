package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.exceptions.BidListNotFoundException;
import com.nnk.springboot.services.AuthService;
import com.nnk.springboot.services.BidListService;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.io.IOException;
import java.util.Random;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Unit Tests for  {@link com.nnk.springboot.controllers.BidListController} Thymeleaf controller class
 */
@ExtendWith(MockitoExtension.class)
class BidListControllerTest {

    @InjectMocks
    private BidListController controllerUnderTest;

    @Mock
    private BidListService bidListService;

    @Mock
    private AuthService authService;
    @Mock
    private Model model;

    @Mock
    HttpServletResponse response;

    @Test
    void baseUrlTest() throws IOException {
        controllerUnderTest.baseUrl(response);
        verify(response, times(1)).sendRedirect(any());
    }

    @Test
    void homeTest() {
        // Act
        controllerUnderTest.home(model);
        // Assert
        verify(model, times(2)).addAttribute(anyString(), any());
        verify(authService, times(1)).getLoggedUser();
    }

    @Test
    void addBidFormTest() {
        // Act
        controllerUnderTest.addBidForm(model);
        // Assert
        verify(model, times(1)).addAttribute(any());
    }

    @Test
    void validateTest() {
        // Arrange
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(false);
        // Act
        controllerUnderTest.validate(new BidList(), result, model);
        // Assert
        verify(bidListService, times(1)).add(any());

    }

    @Test
    void validateWithErrorsTest() {
        // Arrange
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(true);
        // Act
        controllerUnderTest.validate(new BidList(), result, model);
        // Assert
        verify(bidListService, times(0)).add(any());

    }

    @Test
    void showUpdateFormTest() throws BidListNotFoundException {
        // Arrange
        int id = new Random().nextInt();
        // Act
        controllerUnderTest.showUpdateForm(id, model);
        // Assert
        verify(model, times(1)).addAttribute(any());

    }

    @Test
    void updateBidTest() {
        // Arrange
        int id = new Random().nextInt();
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(false);
        // Act
        controllerUnderTest.updateBid(id, new BidList(), result, model);
        // Assert
        verify(bidListService, times(1)).update(any());
    }

    @Test
    void updateBidWithErrorsTest() {
        // Arrange
        int id = new Random().nextInt();
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(true);
        // Act
        controllerUnderTest.updateBid(id, new BidList(), result, model);
        // Assert
        verify(bidListService, times(0)).update(any());
        verify(model, times(1)).addAttribute(anyString(), any());
    }

    @Test
    void deleteBidTest() {
        // Arrange
        int id = new Random().nextInt();
        // Act
        controllerUnderTest.deleteBid(id, model);
        // Assert
        verify(bidListService, times(1)).deleteById(id);
    }
}
