package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.exceptions.RuleNameNotFoundException;
import com.nnk.springboot.services.AuthService;
import com.nnk.springboot.services.RuleNameService;
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
 * Unit Tests for  {@link com.nnk.springboot.controllers.RuleNameController} Thymeleaf controller class
 */
@ExtendWith(MockitoExtension.class)
class RuleNameControllerTest {

    @InjectMocks
    private RuleNameController controllerUnderTest;

    @Mock
    private RuleNameService ruleNameService;

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
    void addRuleFormTest() {
        // Act
        controllerUnderTest.addRuleForm(model);
        // Assert
        verify(model, times(1)).addAttribute(any());
    }

    @Test
    void validateTest() {
        // Arrange
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(false);
        // Act
        controllerUnderTest.validate(new RuleName(), result, model);
        // Assert
        verify(ruleNameService, times(1)).add(any());

    }

    @Test
    void validateWithErrorsTest() {
        // Arrange
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(true);
        // Act
        controllerUnderTest.validate(new RuleName(), result, model);
        // Assert
        verify(ruleNameService, times(0)).add(any());

    }

    @Test
    void showUpdateFormTest() throws RuleNameNotFoundException {
        // Arrange
        int id = new Random().nextInt();
        // Act
        controllerUnderTest.showUpdateForm(id, model);
        // Assert
        verify(model, times(1)).addAttribute(any());

    }

    @Test
    void updateRuleNameTest() {
        // Arrange
        int id = new Random().nextInt();
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(false);
        // Act
        controllerUnderTest.updateRuleName(id, new RuleName(), result, model);
        // Assert
        verify(ruleNameService, times(1)).update(any());
    }

    @Test
    void updateRuleNameWithErrorsTest() {
        // Arrange
        int id = new Random().nextInt();
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(true);
        // Act
        controllerUnderTest.updateRuleName(id, new RuleName(), result, model);
        // Assert
        verify(ruleNameService, times(0)).update(any());
        verify(model, times(1)).addAttribute(any());
    }

    @Test
    void deleteRuleNameTest() {
        // Arrange
        int id = new Random().nextInt();
        // Act
        controllerUnderTest.deleteRuleName(id, model);
        // Assert
        verify(ruleNameService, times(1)).deleteById(id);

    }


}
