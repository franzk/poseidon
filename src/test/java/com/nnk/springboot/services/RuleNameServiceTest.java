package com.nnk.springboot.services;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.exceptions.RuleNameNotFoundException;
import com.nnk.springboot.repositories.RuleNameRepository;
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
 * Unit Tests for  {@link com.nnk.springboot.services.RuleNameService} service class
 */
@ExtendWith(MockitoExtension.class)
class RuleNameServiceTest {

    @InjectMocks
    RuleNameService serviceUnderTest;

    @Mock
    RuleNameRepository ruleNameRepository;

    private final TestDataService testDataService = new TestDataService();

    @Test
    void addTest() {
        // Arrange
        RuleName testRuleName = testDataService.makeTestRuleName();
        // Act
        serviceUnderTest.add(testRuleName);
        // Assert
        verify(ruleNameRepository, times(1)).save(testRuleName);
    }

    @Test
    void updateTest() {
        // Arrange
        RuleName testRuleName = testDataService.makeTestRuleName();
        // Act
        serviceUnderTest.update(testRuleName);
        // Assert
        verify(ruleNameRepository, times(1)).save(testRuleName);
    }

    @Test
    void getAllTest() {
        // Arrange
        // Act
        serviceUnderTest.getAll();
        // Assert
        verify(ruleNameRepository, times(1)).findAll();
    }

    @Test
    void getByIdTest() throws RuleNameNotFoundException {
        // Arrange
        RuleName testRuleName = testDataService.makeTestRuleName();
        int ruleNameId = testRuleName.getId();
        when(ruleNameRepository.findById(anyInt())).thenReturn(Optional.of(testRuleName));
        // Act
        serviceUnderTest.getById(ruleNameId);
        // Assert
        verify(ruleNameRepository, times(1)).findById(ruleNameId);
    }

    @Test
    void getByIdWithExceptionTest()  {
        // Arrange
        int id = new Random().nextInt();
        // Act + Assert
        assertThrows(RuleNameNotFoundException.class, () -> serviceUnderTest.getById(id));
    }

    @Test
    void deleteByIdTest() {
        // Arrange
        int id = new Random().nextInt();
        // Act
        serviceUnderTest.deleteById(id);
        // Assert
        verify(ruleNameRepository, times(1)).deleteById(id);
    }
}
