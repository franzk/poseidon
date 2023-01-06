package com.nnk.springboot.services;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.exceptions.UserNotFoundException;
import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Unit Tests for  {@link com.nnk.springboot.services.OAuth2LoginService} service class
 */
@ExtendWith(MockitoExtension.class)
class OAuth2LoginServiceTest {

    @InjectMocks
    OAuth2LoginService serviceUnderTest;

    @Mock
    UserService userService;

    @Test
    void oauth2LoginTest() {
        // Arrange
        User user = new User();
        user.setUsername(RandomString.make(64));
        when(userService.getByUsername(anyString())).thenReturn(Optional.of(user));
        // Act
        serviceUnderTest.oauth2Login(this.makeOAuth2User(RandomString.make(64)));
        // Assert
        verify(userService, times(1)).update(any());
    }

    @Test
    void oauth2LoginWithNewUserTest() {
        // Arrange
        User user = new User();
        user.setUsername("ll");
        when(userService.getByUsername(anyString())).thenReturn(Optional.empty());
        // Act
        serviceUnderTest.oauth2Login(this.makeOAuth2User(RandomString.make(64)));
        // Assert
        verify(userService, times(1)).add(any());
    }

    @Test
    void oauth2LoginWithExistingUserAndNoNameChangeTest() {
        // Arrange
        String fullname = RandomString.make(64);
        OAuth2AuthenticationToken token = this.makeOAuth2User(fullname);
        User user = new User();
        user.setFullname(fullname);
        when(userService.getByUsername(anyString())).thenReturn(Optional.of(user));
        // Act
        User result = serviceUnderTest.oauth2Login(token);
        // Assert
        assertThat(result.getFullname()).isEqualTo(fullname);
    }

    /**
     * Utils private Method : create an OAuth2 token to call {@link com.nnk.springboot.services.OAuth2LoginService#oauth2Login(Principal)}
     * @param fullname
     * @return {@link org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken}
     */
    private OAuth2AuthenticationToken makeOAuth2User(String fullname)  {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("login", RandomString.make(64));
        attributes.put("name", fullname);

        DefaultOAuth2User oAuth2User = new DefaultOAuth2User(null, attributes , "login");
        OAuth2AuthenticationToken token = new OAuth2AuthenticationToken(oAuth2User, new ArrayList<>(), "ab");
        return token;
    }

}
