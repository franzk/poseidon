package com.nnk.springboot.integration;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.exceptions.CurvePointNotFoundException;
import com.nnk.springboot.repositories.CurvePointRepository;
import com.nnk.springboot.services.TestDataService;
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
class CurveApiTestIT {

    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), StandardCharsets.UTF_8);

    private static final String loggedUsername = "testUser";
    private static final String populateScriptFilePath = "classpath:populate_test_db.sql";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CurvePointRepository curvePointRepository;

    ObjectMapper mapper = new ObjectMapper();

    @Test
    @WithMockUser(username = loggedUsername)
    @Sql(scripts = populateScriptFilePath)
    void createCurvePointTestIT() throws Exception {
        // Arrange
        CurvePoint testCurvePoint = new TestDataService().makeTestCurvePoint();
        testCurvePoint.setId(null);
        String requestJson = mapper.writeValueAsString(testCurvePoint);
        // Act
        mockMvc.perform(post("/api/curvePoint").contentType(APPLICATION_JSON_UTF8).content(requestJson))
                .andExpect(status().isCreated());
        // Assert
        List<CurvePoint> curvePoints = curvePointRepository.findAll();
        CurvePoint lastCurvePoint = curvePoints.get(curvePoints.size()-1);
        testCurvePoint.setId(lastCurvePoint.getId());
        assertThat(lastCurvePoint).isEqualTo(testCurvePoint);
    }

    @Test
    @WithMockUser(username = loggedUsername)
    @Sql(scripts = populateScriptFilePath)
    void getCurvePointsTestIT() throws Exception {
        // Arrange
        List<CurvePoint> curvePoints = curvePointRepository.findAll();
        // Act
        ResultActions resultActions = mockMvc.perform(get("/api/curvePoint/list")).andExpect(status().isOk());
        // Assert
        MvcResult result = resultActions.andReturn();
        String contentAsString = result.getResponse().getContentAsString();
        List<CurvePoint> resultData = mapper.readValue(contentAsString, new TypeReference<List<CurvePoint>>() {});
        assertThat(resultData).hasSize(curvePoints.size());
        int testId = new Random().nextInt(curvePoints.size());
        assertThat(resultData.get(testId)).isEqualTo(curvePoints.get(testId));
    }

    @Test
    @WithMockUser(username = loggedUsername)
    @Sql(scripts = populateScriptFilePath)
    void getCurvePointTestIT() throws Exception {
        // Arrange
        List<CurvePoint> curvePoints = curvePointRepository.findAll();
        CurvePoint testCurvePoint = curvePoints.get(new Random().nextInt(curvePoints.size()));
        // Act
        ResultActions resultActions = mockMvc.perform(get("/api/curvePoint")
                        .param("id", testCurvePoint.getId().toString()))
                .andExpect(status().isOk());
        // Assert
        MvcResult result = resultActions.andReturn();
        String contentAsString = result.getResponse().getContentAsString();
        CurvePoint resultData = mapper.readValue(contentAsString, CurvePoint.class);
        assertThat(resultData).isEqualTo(testCurvePoint);
    }


    @Test
    @WithMockUser(username = loggedUsername)
    @Sql(scripts = populateScriptFilePath)
    void updateCurvePointTest() throws Exception {
        // Arrange
        List<CurvePoint> curvePoints = curvePointRepository.findAll();
        CurvePoint testCurvePoint = curvePoints.get(new Random().nextInt(curvePoints.size()));
        double valueAfterUpdate = new Random().nextDouble();
        testCurvePoint.setValue(valueAfterUpdate);
        String requestJson = mapper.writeValueAsString(testCurvePoint);
        // Act
        mockMvc.perform(put("/api/curvePoint").contentType(APPLICATION_JSON_UTF8).content(requestJson))
                .andExpect(status().isOk());
        // Assert
        CurvePoint afterUpdate = curvePointRepository.findById(testCurvePoint.getId()).orElseThrow(() -> new CurvePointNotFoundException());
        assertThat(afterUpdate.getValue()).isEqualTo(valueAfterUpdate);

    }

    @Test
    @WithMockUser(username = loggedUsername)
    @Sql(scripts = populateScriptFilePath)
    void deleteCurvePointTest() throws Exception {
        // Arrange
        List<CurvePoint> curvePoints = curvePointRepository.findAll();
        CurvePoint testCurvePoint = curvePoints.get(new Random().nextInt(curvePoints.size()));
        int sizeBefore = curvePoints.size();
        // Act
        mockMvc.perform(delete("/api/curvePoint")
                        .param("id", testCurvePoint.getId().toString()))
                .andExpect(status().isOk());
        // Assert
        assertThat(curvePointRepository.findAll()).hasSize(sizeBefore - 1);
        assertThat(curvePointRepository.findById(testCurvePoint.getId())).isEmpty();
    }


    @Test
    @WithMockUser(username = loggedUsername)
    @Sql(scripts = populateScriptFilePath)
    void getCurvePointNotFoundExceptionTestIT() throws Exception {
        // Arrange
        Integer testId = 1000000;
        // Act + Assert
        mockMvc.perform(get("/api/curvePoint").param("id", testId.toString()))
                .andExpect(status().isNotFound());
    }

    @Test
    void apiWithNoLoggedUserTestIT() throws Exception {
        mockMvc.perform(get("/api/curvePoint/list"))
                .andExpect(status().isUnauthorized());
    }
}
