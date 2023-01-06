package com.nnk.springboot.api;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.exceptions.RuleNameNotFoundException;
import com.nnk.springboot.services.RuleNameService;
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
 * Unit Tests for {@link com.nnk.springboot.api.RatingApi} controller class
 */
@ExtendWith(MockitoExtension.class)
class RuleNameApiTest {

    @InjectMocks
    private RuleNameApi controllerUnderTest;
    @Mock
    private RuleNameService ruleNameService;

    TestDataService testDataService = new TestDataService();

    @Test
    void createRuleNameTest() {
        // Arrange
        RuleName testRuleName= testDataService.makeTestRuleName();
        // Act
        ResponseEntity<RuleName> result = controllerUnderTest.createRuleName(testRuleName);
        // Assert
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        verify(ruleNameService, times(1)).add(testRuleName);
    }

    @Test
    void getRuleNamesTest() {
        // Act
        ResponseEntity<List<RuleName>> result = controllerUnderTest.getRuleNames();
        // Assert
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(ruleNameService, times(1)).getAll();
    }

    @Test
    void getRuleNameTest() throws RuleNameNotFoundException {
        // Arrange
        int ruleNameId = new Random().nextInt();
        // Act
        ResponseEntity<RuleName> result = controllerUnderTest.getRuleName(ruleNameId);
        // Assert
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(ruleNameService, times(1)).getById(ruleNameId);
    }

    @Test
    void updateRatingTest() {
        // Arrange
        RuleName testRuleName = testDataService.makeTestRuleName();
        // Act
        ResponseEntity<RuleName> result = controllerUnderTest.updateRuleName(testRuleName);
        // Assert
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(ruleNameService, times(1)).update(testRuleName);
    }

    @Test
    void deleteRuleNameTest() {
        // Arrange
        int ruleNameId = new Random().nextInt();
        // Act
        ResponseEntity<String> result = controllerUnderTest.deleteRuleName(ruleNameId);
        // Assert
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(ruleNameService, times(1)).deleteById(ruleNameId);
    }

}
