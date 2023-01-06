package com.nnk.springboot.services;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.exceptions.UserNotFoundException;
import com.nnk.springboot.repositories.UserRepository;
import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Random;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;

/**
 * Unit Tests for  {@link com.nnk.springboot.services.UserService} service class
 */
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService serviceUnderTest;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    private final TestDataService testDataService = new TestDataService();

    @Test
    void createUserTest() {
        // Arrange
        User testUser = testDataService.makeTestUser();
        // Act
        serviceUnderTest.createUser(testUser);
        // Assert
        verify(userRepository, times(1)).save(any());
    }

    @Test
    void getAllTest() {
        // Act
        serviceUnderTest.getAll();
        // Assert
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void getByUsernameTest() {
        // Arrange
        String username = RandomString.make(64);
        // Act
        serviceUnderTest.getByUsername(username);
        // Assert
        verify(userRepository,times(1)).findByUsername(username);
    }

    @Test
    void saveTest() {
        // Arrange
        User user = testDataService.makeTestUser();
        // Act
        serviceUnderTest.add(user);
        // Assert
        verify(userRepository, times(1)).save(user);

    }

    @Test
    void getByIdTest() throws UserNotFoundException {
        // Arrange
        User user = testDataService.makeTestUser();
        int id = new Random().nextInt();
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));
        // Act
        serviceUnderTest.getById(id);
        // Assert
        verify(userRepository, times(1)).findById(id);
    }

    @Test
    void getByIdWithUserNotFoundExceptionTest()  {
        // Arrange
        int id = new Random().nextInt();
        when(userRepository.findById(anyInt())).thenReturn(Optional.empty());
        // Act + Assert
        assertThrows(UserNotFoundException.class, () -> serviceUnderTest.getById(id));
    }

    @Test
    void updateTest() {
        // Arrange
        User user = testDataService.makeTestUser();
        // Act
        serviceUnderTest.update(user);
        // Assert
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void deleteByIdTest() {
        int id = new Random().nextInt();
        // Act
        serviceUnderTest.deleteById(id);
        // Assert
        verify(userRepository, times(1)).deleteById(id);
    }

}
