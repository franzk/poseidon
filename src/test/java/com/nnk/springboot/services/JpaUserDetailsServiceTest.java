package com.nnk.springboot.services;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.repositories.UserRepository;
import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


/**
 * Unit Tests for  {@link com.nnk.springboot.services.JpaUserDetailsService} service class
 */
@ExtendWith(MockitoExtension.class)
class JpaUserDetailsServiceTest {

    @InjectMocks
    private JpaUserDetailsService serviceUnderTest;

    @Mock
    private UserRepository userRepository;

    @Test
    void loadUserByUsernameTest() {
        // Arrange
        String testUsername = RandomString.make(64);
        User user = new User();
        user.setUsername(testUsername);
        user.setRole(RandomString.make(64));
        when(userRepository.findByUsername(any())).thenReturn(Optional.of(user));

        // Act
        UserDetails aa = serviceUnderTest.loadUserByUsername(RandomString.make(64));

        // Assert
        assertThat(aa.getUsername()).isEqualTo(testUsername);
    }


}
