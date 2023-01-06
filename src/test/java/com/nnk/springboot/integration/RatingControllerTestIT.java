package com.nnk.springboot.integration;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.exceptions.RatingNotFoundException;
import com.nnk.springboot.repositories.RatingRepository;
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
class RatingControllerTestIT {

    private static final String loggedUsername = "testUser";
    private static final String populateScriptFilePath = "classpath:populate_test_db.sql";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RatingRepository ratingRepository;

    @Test
    @WithMockUser(username = loggedUsername)
    @Sql(scripts = populateScriptFilePath)
    void homeTestIT() throws Exception {
        // Arrange
        List<Rating> ratings = ratingRepository.findAll();
        Rating testRating = ratings.get(new Random().nextInt(ratings.size()));
        // Act + Assert
        mockMvc.perform(get("/rating/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("rating/list"))
                .andExpect(content().string(containsString(testRating.getMoodysRating())))
                .andExpect(content().string(containsString(testRating.getSandPRating())))
                .andExpect(content().string(containsString(testRating.getFitchRating())))
                .andExpect(content().string(containsString(testRating.getOrderNumber().toString())));
    }

    @Test
    @WithMockUser(username = loggedUsername)
    @Sql(scripts = populateScriptFilePath)
    void addRatingFormTestIT() throws Exception {
        // Act + Assert
        mockMvc.perform(get("/rating/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("rating/add"));
    }

    @Test
    @WithMockUser(username = loggedUsername)
    @Sql(scripts = populateScriptFilePath)
    void validateTestIT() throws Exception {
        // Arrange
        Rating testRating = new TestDataService().makeTestRating();
        // Act
        mockMvc.perform(post("/rating/validate")
                        .param("moodysRating", testRating.getMoodysRating())
                        .param("sandPRating", testRating.getSandPRating())
                        .param("fitchRating", testRating.getFitchRating())
                        .param("orderNumber", testRating.getOrderNumber().toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("location", "/rating/list"));
        // Assert
        List<Rating> ratings = ratingRepository.findAll();
        Rating lastRating = ratings.get(ratings.size()-1);
        assertThat(lastRating.getMoodysRating()).isEqualTo(testRating.getMoodysRating());
        assertThat(lastRating.getSandPRating()).isEqualTo(testRating.getSandPRating());
        assertThat(lastRating.getFitchRating()).isEqualTo(testRating.getFitchRating());
        assertThat(lastRating.getOrderNumber()).isEqualTo(testRating.getOrderNumber());
    }


    @Test
    @WithMockUser(username = loggedUsername)
    @Sql(scripts = populateScriptFilePath)
    void validateValidAnnotationTestIT() throws Exception {
        // Act + Assert
        mockMvc.perform(post("/rating/validate")) // no param : params = null
                .andExpect(view().name("rating/add"));
    }

    @Test
    @WithMockUser(username = loggedUsername)
    @Sql(scripts = populateScriptFilePath)
    void showUpdateFormTestIT() throws Exception {
        // Arrange
        List<Rating> ratings = ratingRepository.findAll();
        Integer testId = ratings.get(0).getId();
        // Act + Assert
        mockMvc.perform(get("/rating/update/" + testId))
                .andExpect(status().isOk())
                .andExpect(view().name("rating/update"));
    }

    @Test
    @WithMockUser(username = loggedUsername)
    @Sql(scripts = populateScriptFilePath)
    void updateRatingTestIT() throws Exception {
        // Arrange
        List<Rating> ratings = ratingRepository.findAll();
        Rating testRating = ratings.get(new Random().nextInt(ratings.size()));
        Integer testRatingId = testRating.getId();
        String newMoodysRating = RandomString.make(64);
        String newSandPRating = RandomString.make(64);
        String newFitchRating = RandomString.make(64);
        Integer newOrderNumber = new Random().nextInt();
        // Act
        mockMvc.perform(post("/rating/update/" + testRatingId)
                        .param("moodysRating", newMoodysRating)
                        .param("sandPRating", newSandPRating)
                        .param("fitchRating", newFitchRating)
                        .param("orderNumber", newOrderNumber.toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("location", "/rating/list"));
        // Assert
        Rating testRatingAfter = ratingRepository.findById(testRatingId).orElseThrow(() -> new RatingNotFoundException());
        assertThat(testRatingAfter.getMoodysRating()).isEqualTo(newMoodysRating);
        assertThat(testRatingAfter.getSandPRating()).isEqualTo(newSandPRating);
        assertThat(testRatingAfter.getFitchRating()).isEqualTo(newFitchRating);
        assertThat(testRatingAfter.getOrderNumber()).isEqualTo(newOrderNumber);
    }

    @Test
    @WithMockUser(username = loggedUsername)
    @Sql(scripts = populateScriptFilePath)
    void deleteRatingTestIT() throws Exception {
        // Arrange
        List<Rating> ratings = ratingRepository.findAll();
        Rating testRating = ratings.get(new Random().nextInt(ratings.size()));
        Integer testRatingId = testRating.getId();
        // Act
        mockMvc.perform(get("/rating/delete/" + testRatingId))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("location", "/rating/list"));
        // Arrange
        assertThat(ratingRepository.findById(testRatingId)).isEmpty();

    }

    @Test
    @WithMockUser(username = loggedUsername)
    void wrongUrlTest() throws Exception {
        mockMvc.perform(get("/rating/wrongUrl"))
                .andExpect(status().isNotFound());
    }


    @Test
    void notLoggedTest() throws Exception {
        mockMvc.perform(get("/rating/list"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = loggedUsername)
    void ratingNotFoundTest() throws Exception {
        mockMvc.perform(get("/rating/update/100000"))
                .andExpect(status().isNotFound());
    }
}
