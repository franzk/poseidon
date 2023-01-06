package com.nnk.springboot.integration;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.exceptions.UserNotFoundException;
import com.nnk.springboot.repositories.UserRepository;
import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class OAuthControllerTestIT {

    private static final String populateScriptFilePath = "classpath:populate_test_db.sql";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @Test
    @Sql(scripts = populateScriptFilePath)
    void oauth2LoginWithUserCreationTestIT() throws Exception {
        // Arrange
        String testUserName = RandomString.make(64);
        String testFullname = RandomString.make(64);

        OAuth2AuthenticationToken token = this.createOauth2Token(testUserName, testFullname);

        int userCountBefore = userRepository.findAll().size();
        // Act
        mockMvc.perform(get("/app/oauth2login").with(authentication(token)))
                    .andExpect(redirectedUrl("/bidList/list"));
        // Assert
        List<User> users = userRepository.findAll();
        assertThat(userCountBefore).isEqualTo(users.size() - 1); // check that a user was created
        User newUser = users.get(users.size() - 1);
        assertThat(newUser.getUsername()).isEqualTo(testUserName);
        assertThat(newUser.getFullname()).isEqualTo(testFullname);
    }


    @Test
    @Sql(scripts = populateScriptFilePath)
    void oauth2LoginWithUserAlreadyCreatedTestIT() throws Exception {
        // Arrange
        List<User> users = userRepository.findAll();
        User testUser = users.get(new Random().nextInt(users.size()));
        String newFullname = RandomString.make(64);

        OAuth2AuthenticationToken token = this.createOauth2Token(testUser.getUsername(), newFullname);

        // Act
        mockMvc.perform(get("/app/oauth2login").with(authentication(token)))
                .andExpect(redirectedUrl("/bidList/list"));
        // Assert
        User testUserAfter = userRepository.findById(testUser.getId()).orElseThrow(UserNotFoundException::new);
        assertThat(testUserAfter.getUsername()).isEqualTo(testUser.getUsername());
        assertThat(testUserAfter.getFullname()).isEqualTo(newFullname);
    }

    private OAuth2AuthenticationToken createOauth2Token(String login, String name) {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("login", login);
        attributes.put("name", name);

        return new OAuth2AuthenticationToken(
                new DefaultOAuth2User(null, attributes, "login"),
                null,
                "xxx");

    }


}
