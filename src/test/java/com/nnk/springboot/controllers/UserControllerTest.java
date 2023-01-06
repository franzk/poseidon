package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.exceptions.UserNotFoundException;
import com.nnk.springboot.services.AuthService;
import com.nnk.springboot.services.UserService;
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
 * Unit Tests for  {@link com.nnk.springboot.controllers.UserController} Thymeleaf controller class
 */
@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    
    @InjectMocks
    private UserController controllerUnderTest;

    @Mock
    private UserService userService;

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
    void addUserFormTest() {
        // Act
        controllerUnderTest.addUserForm(model);
        // Assert
        verify(model, times(1)).addAttribute(any());
    }

    @Test
    void validateTest() {
        // Arrange
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(false);
        // Act
        controllerUnderTest.validate(new User(), result, model);
        // Assert
        verify(userService, times(1)).add(any());

    }

    @Test
    void validateWithErrorsTest() {
        // Arrange
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(true);
        // Act
        controllerUnderTest.validate(new User(), result, model);
        // Assert
        verify(userService, times(0)).add(any());

    }

    @Test
    void showUpdateFormTest() throws UserNotFoundException {
        // Arrange
        int id = new Random().nextInt();
        when(userService.getById(anyInt())).thenReturn(new User());
        // Act
        controllerUnderTest.showUpdateForm(id, model);
        // Assert
        verify(model, times(1)).addAttribute(any());

    }

    @Test
    void updateUserTest() {
        // Arrange
        int id = new Random().nextInt();
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(false);
        // Act
        controllerUnderTest.updateUser(id, new User(), result, model);
        // Assert
        verify(userService, times(1)).update(any());
    }

    @Test
    void updateUserWithErrorsTest() {
        // Arrange
        int id = new Random().nextInt();
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(true);
        // Act
        controllerUnderTest.updateUser(id, new User(), result, model);
        // Assert
        verify(userService, times(0)).update(any());
        verify(model, times(1)).addAttribute(any());
    }

    @Test
    void deleteUserTest() {
        // Arrange
        int id = new Random().nextInt();
        // Act
        controllerUnderTest.deleteUser(id, model);
        // Assert
        verify(userService, times(1)).deleteById(id);

    }


}
