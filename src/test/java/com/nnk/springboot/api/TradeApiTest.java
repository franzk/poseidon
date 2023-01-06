package com.nnk.springboot.api;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.exceptions.TradeNotFoundException;
import com.nnk.springboot.services.TestDataService;
import com.nnk.springboot.services.TradeService;
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
 * Unit Tests for {@link com.nnk.springboot.api.TradeApi} controller class
 */
@ExtendWith(MockitoExtension.class)
class TradeApiTest {

    @InjectMocks
    private TradeApi controllerUnderTest;
    @Mock
    private TradeService tradeService;

    TestDataService testDataService = new TestDataService();

    @Test
    void createTradeTest() {
        // Arrange
        Trade testTrade= testDataService.makeTestTrade();
        // Act
        ResponseEntity<Trade> result = controllerUnderTest.createTrade(testTrade);
        // Assert
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        verify(tradeService, times(1)).add(testTrade);
    }

    @Test
    void getTradesTest() {
        // Act
        ResponseEntity<List<Trade>> result = controllerUnderTest.getTrades();
        // Assert
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(tradeService, times(1)).getAll();
    }

    @Test
    void getTradeTest() throws TradeNotFoundException {
        // Arrange
        int tradeId = new Random().nextInt();
        // Act
        ResponseEntity<Trade> result = controllerUnderTest.getTrade(tradeId);
        // Assert
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(tradeService, times(1)).getById(tradeId);
    }

    @Test
    void updateTradeTest() {
        // Arrange
        Trade testTrade = testDataService.makeTestTrade();
        // Act
        ResponseEntity<Trade> result = controllerUnderTest.updateTrade(testTrade);
        // Assert
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(tradeService, times(1)).update(testTrade);
    }

    @Test
    void deleteTradeTest() {
        // Arrange
        int tradeId = new Random().nextInt();
        // Act
        ResponseEntity<String> result = controllerUnderTest.deleteTrade(tradeId);
        // Assert
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(tradeService, times(1)).deleteById(tradeId);
    }

}
