package com.nnk.springboot.services;

import com.nnk.springboot.domain.LoggedUser;
import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit Tests for  {@link com.nnk.springboot.services.AuthService} service class
 */
class AuthServiceTest {

    private static final String testUserName = RandomString.make(64);
    private AuthService serviceUnderTest = new AuthService();

    @Test
    void getLoggedUserTest() {
        // Arrange
        SecurityContextHolder.setContext(mockContext());
        // Act
        LoggedUser user = serviceUnderTest.getLoggedUser();
        // Assert
        assertThat(user.getName()).isEqualTo(testUserName);
    }

    SecurityContext mockContext() {
        UsernamePasswordAuthenticationToken authentication = mock(UsernamePasswordAuthenticationToken.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(securityContext.getAuthentication().getName()).thenReturn(testUserName);
        return securityContext;
    }

}
