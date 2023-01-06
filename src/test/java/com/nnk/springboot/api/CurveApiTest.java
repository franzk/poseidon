package com.nnk.springboot.api;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.exceptions.CurvePointNotFoundException;
import com.nnk.springboot.services.CurvePointService;
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
 * Unit Tests for {@link com.nnk.springboot.api.CurveApi} controller class
 */
@ExtendWith(MockitoExtension.class)
class CurveApiTest {

    @InjectMocks
    private CurveApi controllerUnderTest;

    @Mock
    private CurvePointService curvePointService;

    private TestDataService testDataService = new TestDataService();

    @Test
    void getCurvePointsTest() {
        // Act
        ResponseEntity<List<CurvePoint>> result = controllerUnderTest.getCurvePoints();
        // Assert
        verify(curvePointService, times(1)).getAll();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void createCurvePointTest() {
        // Arrange
        CurvePoint testCurvePoint = testDataService.makeTestCurvePoint();
        // Act
        ResponseEntity<CurvePoint> result = controllerUnderTest.createCurvePoint(testCurvePoint);
        // Assert
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        verify(curvePointService, times(1)).add(testCurvePoint);
    }

    @Test
    void getCurvePointTest() throws CurvePointNotFoundException {
        // Arrange
        int curvePointId = new Random().nextInt();
        // Act
        ResponseEntity<CurvePoint> result = controllerUnderTest.getCurvePoint(curvePointId);
        // Assert
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(curvePointService, times(1)).getById(curvePointId);
    }

    @Test
    void updateCurvePointTest() {
        // Arrange
        CurvePoint testCurvePoint = testDataService.makeTestCurvePoint();
        // Act
        ResponseEntity<CurvePoint> result = controllerUnderTest.updateCurvePoint(testCurvePoint);
        // Assert
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(curvePointService, times(1)).update(testCurvePoint);
    }

    @Test
    void deleteCurvePointTest() {
        // Arrange
        int curvePointId = new Random().nextInt();
        // Act
        ResponseEntity<String> result = controllerUnderTest.deleteCurvePoint(curvePointId);
        // Assert
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(curvePointService, times(1)).deleteById(curvePointId);
    }

}
