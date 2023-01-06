package com.nnk.springboot.integration;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.exceptions.UserNotFoundException;
import com.nnk.springboot.repositories.UserRepository;
import com.nnk.springboot.services.TestDataService;
import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest()
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserControllerTestIT {
    private static final String loggedUsername = "testUser";
    private static final String populateScriptFilePath = "classpath:populate_test_db.sql";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Test
    @WithMockUser(username = loggedUsername, authorities = {"ADMIN"})
    @Sql(scripts = populateScriptFilePath)
    void homeTestIT() throws Exception {
        // Arrange
        List<User> users = userRepository.findAll();
        User testUser = users.get(new Random().nextInt(users.size()));
        // Act + Assert
        mockMvc.perform(get("/user/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/list"))
                .andExpect(content().string(containsString(testUser.getFullname())))
                .andExpect(content().string(containsString(testUser.getUsername())))
                .andExpect(content().string(containsString(testUser.getRole())));
    }

    @Test
    @WithMockUser(username = loggedUsername, authorities = {"ADMIN"})
    @Sql(scripts = populateScriptFilePath)
    void addUserFormTestIT() throws Exception {
        // Act + Assert
        mockMvc.perform(get("/user/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/add"));
    }

    @Test
    @WithMockUser(username = loggedUsername, authorities = {"ADMIN"})
    @Sql(scripts = populateScriptFilePath)
    void validateTestIT() throws Exception {
        // Arrange
        User testUser = new TestDataService().makeTestUser();
        String testPassword = "Aa1!mmmm";
        // Act
        mockMvc.perform(post("/user/validate")
                        .param("fullname", testUser.getFullname())
                        .param("username", testUser.getUsername())
                        .param("password", testPassword)
                        .param("role", testUser.getRole()))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("location", "/user/list"));
        // Assert
        List<User> users = userRepository.findAll();
        User lastUser = users.get(users.size()-1);
        assertThat(lastUser.getFullname()).isEqualTo(testUser.getFullname());
        assertThat(lastUser.getUsername()).isEqualTo(testUser.getUsername());
        assertThat(lastUser.getRole()).isEqualTo(testUser.getRole());
    }

    @Test
    @WithMockUser(username = loggedUsername, authorities = {"ADMIN"})
    @Sql(scripts = populateScriptFilePath)
    void showUpdateFormTestIT() throws Exception {
        // Arrange
        List<User> users = userRepository.findAll();
        User testUser = users.get(new Random().nextInt(users.size()));
        Integer testUserId = testUser.getId();
        // Act + Assert
        mockMvc.perform(get("/user/update/" + testUserId))
                .andExpect(status().isOk())
                .andExpect(view().name("user/update"))
                .andExpect(content().string(containsString(testUser.getFullname())))
                .andExpect(content().string(containsString(testUser.getUsername())));
    }

    @Test
    @WithMockUser(username = loggedUsername, authorities = {"ADMIN"})
    @Sql(scripts = populateScriptFilePath)
    void updateUserTestIT() throws Exception {
        // Arrange
        List<User> users = userRepository.findAll();
        User testUser = users.get(new Random().nextInt(users.size()));
        Integer testUserId = testUser.getId();
        String newFullname = RandomString.make(64);
        String newUsername = RandomString.make(64);
        String newRole = RandomString.make(64);
        String newPassword = "Aa1!mmmm";
        // Act
        mockMvc.perform(post("/user/update/" + testUserId)
                        .param("fullname", newFullname)
                        .param("username", newUsername)
                        .param("password", newPassword)
                        .param("role", newRole))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("location", "/user/list"));
        // Assert
        User testUserAfter = userRepository.findById(testUserId).orElseThrow(UserNotFoundException::new);
        assertThat(testUserAfter.getFullname()).isEqualTo(newFullname);
        assertThat(testUserAfter.getUsername()).isEqualTo(newUsername);
        assertThat(testUserAfter.getRole()).isEqualTo(newRole);
    }

    @Test
    @WithMockUser(username = loggedUsername, authorities = {"ADMIN"})
    @Sql(scripts = populateScriptFilePath)
    void deleteUserTestIT() throws Exception {
        // Arrange
        List<User> users = userRepository.findAll();
        User testUser = users.get(new Random().nextInt(users.size()));
        Integer testUserId = testUser.getId();
        // Act
        mockMvc.perform(get("/user/delete/" + testUserId))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("location", "/user/list"));
        // Arrange
        assertThat(userRepository.findById(testUserId)).isEmpty();

    }

    @Test
    @WithMockUser(username = loggedUsername, authorities = {"ADMIN"})
    void wrongUrlTest() throws Exception {
        mockMvc.perform(get("/user/wrongUrl"))
                .andExpect(status().isNotFound());
    }


    @Test
    void notLoggedTest() throws Exception {
        mockMvc.perform(get("/user/list"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = loggedUsername, authorities = {"ADMIN"})
    void userNotFoundTest() throws Exception {
        mockMvc.perform(get("/user/update/100000"))
                .andExpect(status().isNotFound());
    }
}
