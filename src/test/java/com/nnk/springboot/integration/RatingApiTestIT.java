package com.nnk.springboot.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.exceptions.RatingNotFoundException;
import com.nnk.springboot.repositories.RatingRepository;
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
class RatingApiTestIT {


    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), StandardCharsets.UTF_8);

    private static final String loggedUsername = "testUser";
    private static final String populateScriptFilePath = "classpath:populate_test_db.sql";

    @Autowired
    private MockMvc mockMvc;

    ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private RatingRepository ratingRepository;

    @Test
    @WithMockUser(username = loggedUsername)
    @Sql(scripts = populateScriptFilePath)
    void createRatingTestIT() throws Exception {
        // Arrange
        Rating testRating = new TestDataService().makeTestRating();
        testRating.setId(null);
        String requestJson = mapper.writeValueAsString(testRating);
        // Act
        mockMvc.perform(post("/api/rating").contentType(APPLICATION_JSON_UTF8).content(requestJson))
                .andExpect(status().isCreated());
        // Assert
        List<Rating> ratings = ratingRepository.findAll();
        Rating lastRating = ratings.get(ratings.size()-1);
        testRating.setId(lastRating.getId());
        assertThat(lastRating).isEqualTo(testRating);
    }

    @Test
    @WithMockUser(username = loggedUsername)
    @Sql(scripts = populateScriptFilePath)
    void getRatingsTestIT() throws Exception {
        // Arrange
        List<Rating> ratings = ratingRepository.findAll();
        // Act
        ResultActions resultActions = mockMvc.perform(get("/api/rating/list")).andExpect(status().isOk());
        // Assert
        MvcResult result = resultActions.andReturn();
        String contentAsString = result.getResponse().getContentAsString();
        List<Rating> resultData = mapper.readValue(contentAsString, new TypeReference<List<Rating>>() {});
        assertThat(resultData).hasSize(ratings.size());
        int testId = new Random().nextInt(ratings.size());
        assertThat(resultData.get(testId)).isEqualTo(ratings.get(testId));
    }

    @Test
    @WithMockUser(username = loggedUsername)
    @Sql(scripts = populateScriptFilePath)
    void getRatingTestIT() throws Exception {
        // Arrange
        List<Rating> ratings = ratingRepository.findAll();
        Rating testRating = ratings.get(new Random().nextInt(ratings.size()));
        // Act
        ResultActions resultActions = mockMvc.perform(get("/api/rating")
                        .param("id", testRating.getId().toString()))
                .andExpect(status().isOk());
        // Assert
        MvcResult result = resultActions.andReturn();
        String contentAsString = result.getResponse().getContentAsString();
        Rating resultData = mapper.readValue(contentAsString, Rating.class);
        assertThat(resultData).isEqualTo(testRating);
    }


    @Test
    @WithMockUser(username = loggedUsername)
    @Sql(scripts = populateScriptFilePath)
    void updateRatingTest() throws Exception {
        // Arrange
        List<Rating> ratings = ratingRepository.findAll();
        Rating testRating = ratings.get(new Random().nextInt(ratings.size()));
        String moodysRatingAfterUpdate = RandomString.make(64);
        testRating.setMoodysRating(moodysRatingAfterUpdate);
        String requestJson = mapper.writeValueAsString(testRating);
        // Act
        mockMvc.perform(put("/api/rating").contentType(APPLICATION_JSON_UTF8).content(requestJson))
                .andExpect(status().isOk());
        // Assert
        Rating afterUpdate = ratingRepository.findById(testRating.getId()).orElseThrow(() -> new RatingNotFoundException());
        assertThat(afterUpdate.getMoodysRating()).isEqualTo(moodysRatingAfterUpdate);

    }

    @Test
    @WithMockUser(username = loggedUsername)
    @Sql(scripts = populateScriptFilePath)
    void deleteRatingTest() throws Exception {
        // Arrange
        List<Rating> ratings = ratingRepository.findAll();
        Rating testRating = ratings.get(new Random().nextInt(ratings.size()));
        int sizeBefore = ratings.size();
        // Act
        mockMvc.perform(delete("/api/rating")
                        .param("id", testRating.getId().toString()))
                .andExpect(status().isOk());
        // Assert
        assertThat(ratingRepository.findAll()).hasSize(sizeBefore - 1);
        assertThat(ratingRepository.findById(testRating.getId())).isEmpty();
    }

    @Test
    @WithMockUser(username = loggedUsername)
    @Sql(scripts = populateScriptFilePath)
    void getRatingNotFoundExceptionTestIT() throws Exception {
        // Arrange
        Integer testId = 1000000;
        // Act + Assert
        mockMvc.perform(get("/api/rating").param("id", testId.toString()))
                .andExpect(status().isNotFound());
    }

    @Test
    void apiWithNoLoggedUserTestIT() throws Exception {
        mockMvc.perform(get("/api/rating/list"))
                .andExpect(status().isUnauthorized());
    }
}
