package com.nnk.springboot.api;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.exceptions.BidListNotFoundException;
import com.nnk.springboot.services.BidListService;
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
 * Unit Tests for  {@link com.nnk.springboot.api.BidListApi} controller class
 */
@ExtendWith(MockitoExtension.class)
class BidListApiTest {
    @InjectMocks
    private BidListApi controllerUnderTest;

    @Mock
    private BidListService bidListService;

    TestDataService testDataService = new TestDataService();

    @Test
    void createBidList() {
        // Arrange
        BidList testBidList = testDataService.makeTestBidList();
        // Act
        ResponseEntity<BidList> result = controllerUnderTest.createBidList(testBidList);
        // Assert
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        verify(bidListService, times(1)).add(testBidList);
    }

    @Test
    void getBidLists() {
        // Act
        ResponseEntity<List<BidList>> result = controllerUnderTest.getBidLists();
        // Assert
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(bidListService, times(1)).getAll();
    }

    @Test
    void getBidListTest() throws BidListNotFoundException {
        // Arrange
        int bidListId = new Random().nextInt();
        // Act
        ResponseEntity<BidList> result = controllerUnderTest.getBidList(bidListId);
        // Assert
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(bidListService, times(1)).getById(bidListId);
    }

    @Test
    void updateBidListTest() {
        // Arrange
        BidList testBidList = testDataService.makeTestBidList();
        // Act
        ResponseEntity<BidList> result = controllerUnderTest.updateBidList(testBidList);
        // Assert
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(bidListService, times(1)).update(testBidList);
    }

    @Test
    void deleteBidListTest() throws BidListNotFoundException {
        // Arrange
        int bidListId = new Random().nextInt();
        // Act
        ResponseEntity<String> result = controllerUnderTest.deleteBidList(bidListId);
        // Assert
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(bidListService, times(1)).deleteById(bidListId);
    }
}
