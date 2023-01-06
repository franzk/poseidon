package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.exceptions.CurvePointNotFoundException;
import com.nnk.springboot.services.AuthService;
import com.nnk.springboot.services.CurvePointService;
import jakarta.servlet.http.HttpServletResponse;
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

/**
 * Unit Tests for  {@link com.nnk.springboot.controllers.CurveController} Thymeleaf controller class
 */
@ExtendWith(MockitoExtension.class)
class CurveControllerTest {

    @InjectMocks
    private CurveController controllerUnderTest;

    @Mock
    private CurvePointService curvePointService;

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
    void addCurvePointFormTest() {
        // Act
        controllerUnderTest.addCurvePointForm(model);
        // Assert
        verify(model, times(1)).addAttribute(any());
    }

    @Test
    void validateTest() {
        // Arrange
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(false);
        // Act
        controllerUnderTest.validate(new CurvePoint(), result, model);
        // Assert
        verify(curvePointService, times(1)).add(any());

    }

    @Test
    void validateWithErrorsTest() {
        // Arrange
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(true);
        // Act
        controllerUnderTest.validate(new CurvePoint(), result, model);
        // Assert
        verify(curvePointService, times(0)).add(any());

    }

    @Test
    void showUpdateFormTest() throws CurvePointNotFoundException {
        // Arrange
        int id = new Random().nextInt();
        // Act
        controllerUnderTest.showUpdateForm(id, model);
        // Assert
        verify(model, times(1)).addAttribute(any());

    }

    @Test
    void updateCurvePointTest() {
        // Arrange
        int id = new Random().nextInt();
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(false);
        // Act
        controllerUnderTest.updateCurvePoint(id, new CurvePoint(), result, model);
        // Assert
        verify(curvePointService, times(1)).update(any());
    }

    @Test
    void updateCurvePointWithErrorsTest() {
        // Arrange
        int id = new Random().nextInt();
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(true);
        // Act
        controllerUnderTest.updateCurvePoint(id, new CurvePoint(), result, model);
        // Assert
        verify(curvePointService, times(0)).update(any());
        verify(model, times(1)).addAttribute(any());
    }

    @Test
    void deleteCurvePointTest() {
        // Arrange
        int id = new Random().nextInt();
        // Act
        controllerUnderTest.deleteCurvePoint(id, model);
        // Assert
        verify(curvePointService, times(1)).deleteById(id);

    }
}
