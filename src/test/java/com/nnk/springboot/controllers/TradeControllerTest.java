package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.exceptions.TradeNotFoundException;
import com.nnk.springboot.services.AuthService;
import com.nnk.springboot.services.TradeService;
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
 * Unit Tests for  {@link com.nnk.springboot.controllers.TradeController} Thymeleaf controller class
 */
@ExtendWith(MockitoExtension.class)
class TradeControllerTest {
    
    @InjectMocks
    private TradeController controllerUnderTest;

    @Mock
    private TradeService tradeService;

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
    void addTradeFormTest() {
        // Act
        controllerUnderTest.addTradeForm(model);
        // Assert
        verify(model, times(1)).addAttribute(any());
    }

    @Test
    void validateTest() {
        // Arrange
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(false);
        // Act
        controllerUnderTest.validate(new Trade(), result, model);
        // Assert
        verify(tradeService, times(1)).add(any());

    }

    @Test
    void validateWithErrorsTest() {
        // Arrange
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(true);
        // Act
        controllerUnderTest.validate(new Trade(), result, model);
        // Assert
        verify(tradeService, times(0)).add(any());

    }

    @Test
    void showUpdateFormTest() throws TradeNotFoundException {
        // Arrange
        int id = new Random().nextInt();
        // Act
        controllerUnderTest.showUpdateForm(id, model);
        // Assert
        verify(model, times(1)).addAttribute(any());

    }

    @Test
    void updateTradeTest() {
        // Arrange
        int id = new Random().nextInt();
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(false);
        // Act
        controllerUnderTest.updateTrade(id, new Trade(), result, model);
        // Assert
        verify(tradeService, times(1)).update(any());
    }

    @Test
    void updateTradeWithErrorsTest() {
        // Arrange
        int id = new Random().nextInt();
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(true);
        // Act
        controllerUnderTest.updateTrade(id, new Trade(), result, model);
        // Assert
        verify(tradeService, times(0)).update(any());
        verify(model, times(1)).addAttribute(any());
    }

    @Test
    void deleteTradeTest() {
        // Arrange
        int id = new Random().nextInt();
        // Act
        controllerUnderTest.deleteTrade(id, model);
        // Assert
        verify(tradeService, times(1)).deleteById(id);
    }
}
