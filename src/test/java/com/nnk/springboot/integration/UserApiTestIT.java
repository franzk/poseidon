package com.nnk.springboot.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nnk.springboot.domain.User;
import com.nnk.springboot.exceptions.UserNotFoundException;
import com.nnk.springboot.repositories.UserRepository;
import com.nnk.springboot.services.TestDataService;
import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserApiTestIT {

    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), StandardCharsets.UTF_8);
    private static final String loggedUsername = "testUser";
    private static final String populateScriptFilePath = "classpath:populate_test_db.sql";

    @Autowired
    private MockMvc mockMvc;

    ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private UserRepository userRepository;

    @Test
    @WithMockUser(username = loggedUsername, authorities = {"ADMIN"})
    @Sql(scripts = populateScriptFilePath)
    void createUserTestIT() throws Exception {
        // Arrange
        User testUser = new TestDataService().makeTestUser();
        testUser.setId(null);
        testUser.setPassword("Aa1!mmmm"); // respect the constraints
        String requestJson = mapper.writeValueAsString(testUser);
        // Act
        mockMvc.perform(post("/api/user").contentType(APPLICATION_JSON_UTF8).content(requestJson))
                .andExpect(status().isCreated());
        // Assert
        List<User> users = userRepository.findAll();
        User lastUser = users.get(users.size()-1);
        assertThat(lastUser.getUsername()).isEqualTo(testUser.getUsername());
        assertThat(lastUser.getFullname()).isEqualTo(testUser.getFullname());
    }

    @Test
    @WithMockUser(username = loggedUsername, authorities = {"ADMIN"})
    @Sql(scripts = populateScriptFilePath)
    void createUserWithInvalidPasswordTestIT() throws Exception {
        // Arrange
        User testUser = new TestDataService().makeTestUser();
        testUser.setId(null);
        testUser.setPassword("aa"); // it doesn't respect the constraints
        String requestJson = mapper.writeValueAsString(testUser);
        int userCount = userRepository.findAll().size();
        // Act
        mockMvc.perform(post("/api/user").contentType(APPLICATION_JSON_UTF8).content(requestJson))
                .andExpect(status().isBadRequest());
        // Assert
        assertThat(userCount).isEqualTo(userRepository.findAll().size()); // verify that no user were created
    }

    @Test
    @WithMockUser(username = loggedUsername, authorities = {"ADMIN"})
    @Sql(scripts = populateScriptFilePath)
    void getUsersTestIT() throws Exception {
        // Arrange
        List<User> users = userRepository.findAll();
        // Act
        ResultActions resultActions = mockMvc.perform(get("/api/user/list")).andExpect(status().isOk());
        // Assert
        MvcResult result = resultActions.andReturn();
        String contentAsString = result.getResponse().getContentAsString();
        List<User> resultData = mapper.readValue(contentAsString, new TypeReference<List<User>>() {});
        assertThat(resultData).hasSize(users.size());
        int testId = new Random().nextInt(users.size());
        assertThat(resultData.get(testId)).isEqualTo(users.get(testId));
    }

    @Test
    @WithMockUser(username = loggedUsername, authorities = {"ADMIN"})
    @Sql(scripts = populateScriptFilePath)
    void getUserTestIT() throws Exception {
        // Arrange
        List<User> users = userRepository.findAll();
        User testUser = users.get(new Random().nextInt(users.size()));
        // Act
        ResultActions resultActions = mockMvc.perform(get("/api/user")
                        .param("id", testUser.getId().toString()))
                .andExpect(status().isOk());
        // Assert
        MvcResult result = resultActions.andReturn();
        String contentAsString = result.getResponse().getContentAsString();
        User resultData = mapper.readValue(contentAsString, User.class);
        assertThat(resultData).isEqualTo(testUser);
    }


    @Test
    @WithMockUser(username = loggedUsername, authorities = {"ADMIN"})
    @Sql(scripts = populateScriptFilePath)
    void updateUserTest() throws Exception {
        // Arrange
        List<User> users = userRepository.findAll();
        User testUser = users.get(new Random().nextInt(users.size()));
        String usernameAfterUpdate = RandomString.make(64);
        testUser.setUsername(usernameAfterUpdate);
        testUser.setPassword("Aa1!mmmm"); // must respect the constraints
        String requestJson = mapper.writeValueAsString(testUser);
        // Act
        mockMvc.perform(put("/api/user").contentType(APPLICATION_JSON_UTF8).content(requestJson))
                .andExpect(status().isOk());
        // Assert
        User afterUpdate = userRepository.findById(testUser.getId()).orElseThrow(UserNotFoundException::new);
        assertThat(afterUpdate.getUsername()).isEqualTo(usernameAfterUpdate);
    }

    @Test
    @WithMockUser(username = loggedUsername, authorities = {"ADMIN"})
    @Sql(scripts = populateScriptFilePath)
    void updateUserWithInvalidPasswordTest() throws Exception {
        // Arrange
        List<User> users = userRepository.findAll();
        User testUser = users.get(new Random().nextInt(users.size()));
        String usernameAfterUpdate = RandomString.make(64);
        testUser.setUsername(usernameAfterUpdate);
        testUser.setPassword("abcg"); // this don't respect the constraints
        String requestJson = mapper.writeValueAsString(testUser);
        // Act
        mockMvc.perform(put("/api/user").contentType(APPLICATION_JSON_UTF8).content(requestJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = loggedUsername, authorities = {"ADMIN"})
    @Sql(scripts = populateScriptFilePath)
    void deleteUserTest() throws Exception {
        // Arrange
        List<User> users = userRepository.findAll();
        User testUser = users.get(new Random().nextInt(users.size()));
        int sizeBefore = users.size();
        // Act
        mockMvc.perform(delete("/api/user")
                        .param("id", testUser.getId().toString()))
                .andExpect(status().isOk());
        // Assert
        assertThat(userRepository.findAll()).hasSize(sizeBefore - 1);
        assertThat(userRepository.findById(testUser.getId())).isEmpty();
    }

    @Test
    @WithMockUser(username = loggedUsername, authorities = {"ADMIN"})
    @Sql(scripts = populateScriptFilePath)
    void getUserNotFoundExceptionTestIT() throws Exception {
        // Arrange
        Integer testId = 1000000;
        // Act + Assert
        mockMvc.perform(get("/api/user").param("id", testId.toString()))
                .andExpect(status().isNotFound());
    }

    @Test
    void apiWithNoLoggedUserTestIT() throws Exception {
        mockMvc.perform(get("/api/user/list"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser("simpleUser")
    void userApiWithNoAdminAuthority() throws Exception {
        mockMvc.perform(get("/api/user/list"))
                .andExpect(status().isForbidden());
    }
}
