package com.nnk.springboot.services;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.exceptions.BidListNotFoundException;
import com.nnk.springboot.repositories.BidListRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * Unit Tests for  {@link com.nnk.springboot.services.BidListService} service class
 */
@ExtendWith(MockitoExtension.class)
class BidListServiceTest {

    @InjectMocks
    BidListService serviceUnderTest;

    @Mock
    BidListRepository bidListRepository;

    private final TestDataService testDataService = new TestDataService();

    @Test
    void addTest() {
        // Arrange
        BidList testBidList = testDataService.makeTestBidList();
        // Act
        serviceUnderTest.add(testBidList);
        // Assert
        verify(bidListRepository, times(1)).save(testBidList);
    }

    @Test
    void updateTest() {
        // Arrange
        BidList testBidList = testDataService.makeTestBidList();
        // Act
        serviceUnderTest.update(testBidList);
        // Assert
        verify(bidListRepository, times(1)).save(testBidList);
    }

    @Test
    void getAllTest() {
        // Arrange
        // Act
        serviceUnderTest.getAll();
        // Assert
        verify(bidListRepository, times(1)).findAll();
    }

    @Test
    void getByIdTest() throws BidListNotFoundException {
        // Arrange
        BidList testBidList = testDataService.makeTestBidList();
        int bidListId = testBidList.getBidListId();
        when(bidListRepository.findById(anyInt())).thenReturn(Optional.of(testBidList));
        // Act
        serviceUnderTest.getById(bidListId);
        // Assert
        verify(bidListRepository, times(1)).findById(bidListId);
    }

    @Test
    void getByIdWithExceptionTest()  {
        // Arrange
        int id = new Random().nextInt();
        // Act + Assert
        assertThrows(BidListNotFoundException.class, () -> serviceUnderTest.getById(id));
    }

    @Test
    void deleteByIdTest() {
        // Arrange
        int id = new Random().nextInt();
        // Act
        serviceUnderTest.deleteById(id);
        // Assert
        verify(bidListRepository, times(1)).deleteById(id);
    }

}
