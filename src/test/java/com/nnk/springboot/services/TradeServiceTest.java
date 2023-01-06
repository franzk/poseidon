package com.nnk.springboot.services;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.exceptions.TradeNotFoundException;
import com.nnk.springboot.repositories.TradeRepository;
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
 * Unit Tests for  {@link com.nnk.springboot.services.TradeService} service class
 */
@ExtendWith(MockitoExtension.class)
class TradeServiceTest {

    @InjectMocks
    TradeService serviceUnderTest;

    @Mock
    TradeRepository tradeRepository;

    private final TestDataService testDataService = new TestDataService();

    @Test
    void addTest() {
        // Arrange
        Trade testTrade = testDataService.makeTestTrade();
        // Act
        serviceUnderTest.add(testTrade);
        // Assert
        verify(tradeRepository, times(1)).save(testTrade);
    }

    @Test
    void updateTest() {
        // Arrange
        Trade testTrade = testDataService.makeTestTrade();
        // Act
        serviceUnderTest.update(testTrade);
        // Assert
        verify(tradeRepository, times(1)).save(testTrade);
    }

    @Test
    void getAllTest() {
        // Arrange
        // Act
        serviceUnderTest.getAll();
        // Assert
        verify(tradeRepository, times(1)).findAll();
    }

    @Test
    void getByIdTest() throws TradeNotFoundException {
        // Arrange
        Trade testTrade = testDataService.makeTestTrade();
        int tradeId = testTrade.getTradeId();
        when(tradeRepository.findById(anyInt())).thenReturn(Optional.of(testTrade));
        // Act
        serviceUnderTest.getById(tradeId);
        // Assert
        verify(tradeRepository, times(1)).findById(tradeId);
    }

    @Test
    void getByIdWithExceptionTest()  {
        // Arrange
        int id = new Random().nextInt();
        // Act + Assert
        assertThrows(TradeNotFoundException.class, () -> serviceUnderTest.getById(id));
    }

    @Test
    void deleteByIdTest() {
        // Arrange
        int id = new Random().nextInt();
        // Act
        serviceUnderTest.deleteById(id);
        // Assert
        verify(tradeRepository, times(1)).deleteById(id);
    }

}
