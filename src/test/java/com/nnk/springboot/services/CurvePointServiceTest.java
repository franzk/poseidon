package com.nnk.springboot.services;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.exceptions.CurvePointNotFoundException;
import com.nnk.springboot.repositories.CurvePointRepository;
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
 * Unit Tests for  {@link com.nnk.springboot.services.CurvePointService} service class
 */
@ExtendWith(MockitoExtension.class)
class CurvePointServiceTest {

    @InjectMocks
    CurvePointService serviceUnderTest;

    @Mock
    CurvePointRepository curvePointRepository;

    private final TestDataService testDataService = new TestDataService();

    @Test
    void addTest() {
        // Arrange
        CurvePoint testCurvePoint = testDataService.makeTestCurvePoint();
        // Act
        serviceUnderTest.add(testCurvePoint);
        // Assert
        verify(curvePointRepository, times(1)).save(testCurvePoint);
    }

    @Test
    void updateTest() {
        // Arrange
        CurvePoint testCurvePoint = testDataService.makeTestCurvePoint();
        // Act
        serviceUnderTest.update(testCurvePoint);
        // Assert
        verify(curvePointRepository, times(1)).save(testCurvePoint);
    }

    @Test
    void getAllTest() {
        // Arrange
        // Act
        serviceUnderTest.getAll();
        // Assert
        verify(curvePointRepository, times(1)).findAll();
    }

    @Test
    void getByIdTest() throws CurvePointNotFoundException {
        // Arrange
        CurvePoint testCurvePoint = testDataService.makeTestCurvePoint();
        int curveId = testCurvePoint.getCurveId();
        when(curvePointRepository.findById(anyInt())).thenReturn(Optional.of(testCurvePoint));
        // Act
        serviceUnderTest.getById(curveId);
        // Assert
        verify(curvePointRepository, times(1)).findById(curveId);
    }

    @Test
    void getByIdWithExceptionTest()  {
        // Arrange
        int id = new Random().nextInt();
        // Act + Assert
        assertThrows(CurvePointNotFoundException.class, () -> serviceUnderTest.getById(id));
    }

    @Test
    void deleteByIdTest() {
        // Arrange
        int id = new Random().nextInt();
        // Act
        serviceUnderTest.deleteById(id);
        // Assert
        verify(curvePointRepository, times(1)).deleteById(id);
    }

}
