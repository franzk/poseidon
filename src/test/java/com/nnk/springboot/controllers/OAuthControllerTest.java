package com.nnk.springboot.controllers;

import com.nnk.springboot.exceptions.UserNotFoundException;
import com.nnk.springboot.services.OAuth2LoginService;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.security.Principal;

import static org.mockito.Mockito.*;

/**
 * Unit Tests for  {@link com.nnk.springboot.controllers.OAuthController} controller class
 */
@ExtendWith(MockitoExtension.class)
class OAuthControllerTest {

    @InjectMocks
    private OAuthController controllerUnderTest;

    @Mock
    private OAuth2LoginService oAuth2LoginService;

    @Test
    void oauth2LoginTest() throws IOException {
        // Arrange
        Principal user = mock(Principal.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        // Act
        controllerUnderTest.oauth2Login(user, response);
        // Assert
        verify(oAuth2LoginService, times(1)).oauth2Login(any());
        verify(response, times(1)).sendRedirect(anyString());
    }

}
