package com.nnk.springboot.api;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.exceptions.UserNotFoundException;
import com.nnk.springboot.services.TestDataService;
import com.nnk.springboot.services.UserService;
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
 * Unit Tests for {@link com.nnk.springboot.api.UserApi} controller class
 */
@ExtendWith(MockitoExtension.class)
class UserApiTest {

    @InjectMocks
    UserApi controllerUnderTest;

    @Mock
    UserService userService;

    TestDataService testDataService = new TestDataService();

    @Test
    void createUserTest() {
        // Arrange
        User testUser= testDataService.makeTestUser();
        // Act
        ResponseEntity<User> result = controllerUnderTest.createUser(testUser);
        // Assert
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        verify(userService, times(1)).createUser(testUser);
    }

    @Test
    void getUsersTest() {
        // Act
        ResponseEntity<List<User>> result = controllerUnderTest.getUsers();
        // Assert
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(userService, times(1)).getAll();
    }

    @Test
    void getUserTest() throws UserNotFoundException {
        // Arrange
        int userId = new Random().nextInt();
        // Act
        ResponseEntity<User> result = controllerUnderTest.getUser(userId);
        // Assert
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(userService, times(1)).getById(userId);
    }

    @Test
    void updateUserTest() {
        // Arrange
        User testUser = testDataService.makeTestUser();
        // Act
        ResponseEntity<User> result = controllerUnderTest.updateUser(testUser);
        // Assert
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(userService, times(1)).update(testUser);
    }

    @Test
    void deleteUserTest() {
        // Arrange
        int userId = new Random().nextInt();
        // Act
        ResponseEntity<String> result = controllerUnderTest.deleteUser(userId);
        // Assert
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(userService, times(1)).deleteById(userId);
    }


}
