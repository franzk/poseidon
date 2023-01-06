package com.nnk.springboot.integration;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.exceptions.CurvePointNotFoundException;
import com.nnk.springboot.repositories.CurvePointRepository;
import com.nnk.springboot.services.TestDataService;
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

@SpringBootTest()
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CurveControllerTestIT {
    private static final String loggedUsername = "testUser";
    private static final String populateScriptFilePath = "classpath:populate_test_db.sql";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CurvePointRepository curvePointRepository;

    @Test
    @WithMockUser(username = loggedUsername)
    @Sql(scripts = populateScriptFilePath)
    void homeTestIT() throws Exception {
        // Arrange
        List<CurvePoint> curvePoints = curvePointRepository.findAll();
        CurvePoint testCurvePoint = curvePoints.get(new Random().nextInt(curvePoints.size()));
        // Act + Assert
        mockMvc.perform(get("/curvePoint/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("curvePoint/list"))
                .andExpect(content().string(containsString(testCurvePoint.getCurveId().toString())))
                .andExpect(content().string(containsString(testCurvePoint.getValue().toString())))
                .andExpect(content().string(containsString(testCurvePoint.getTerm().toString())));
    }

    @Test
    @WithMockUser(username = loggedUsername)
    @Sql(scripts = populateScriptFilePath)
    void addCurvePointFormTestIT() throws Exception {
        // Act + Assert
        mockMvc.perform(get("/curvePoint/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("curvePoint/add"));
    }

    @Test
    @WithMockUser(username = loggedUsername)
    @Sql(scripts = populateScriptFilePath)
    void validateTestIT() throws Exception {
        // Arrange
        CurvePoint testCurvePoint = new TestDataService().makeTestCurvePoint();
        // Act
        mockMvc.perform(post("/curvePoint/validate")
                        .param("curveId", testCurvePoint.getCurveId().toString())
                        .param("term", testCurvePoint.getTerm().toString())
                        .param("value", testCurvePoint.getValue().toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("location", "/curvePoint/list"));
        // Assert
        List<CurvePoint> curvePoints = curvePointRepository.findAll();
        CurvePoint lastCurvePoint = curvePoints.get(curvePoints.size()-1);
        assertThat(lastCurvePoint.getCurveId()).isEqualTo(testCurvePoint.getCurveId());
        assertThat(lastCurvePoint.getTerm()).isEqualTo(testCurvePoint.getTerm());
        assertThat(lastCurvePoint.getValue()).isEqualTo(testCurvePoint.getValue());
    }

   @Test
    @WithMockUser(username = loggedUsername)
    @Sql(scripts = populateScriptFilePath)
    void validateValidAnnotationTestIT() throws Exception {
        // Act + Assert
        mockMvc.perform(post("/curvePoint/validate")) // no param : params = null
                .andExpect(view().name("curvePoint/add"));
    }

    @Test
    @WithMockUser(username = loggedUsername)
    @Sql(scripts = populateScriptFilePath)
    void showUpdateFormTestIT() throws Exception {
        // Arrange
        List<CurvePoint> curvePoints = curvePointRepository.findAll();
        CurvePoint testCurvePoint = curvePoints.get(new Random().nextInt(curvePoints.size()));
        Integer testCurvePointId = testCurvePoint.getId();
        // Act + Assert
        mockMvc.perform(get("/curvePoint/update/" + testCurvePointId))
                .andExpect(status().isOk())
                .andExpect(view().name("curvePoint/update"))
                .andExpect(content().string(containsString(testCurvePoint.getCurveId().toString())))
                .andExpect(content().string(containsString(testCurvePoint.getTerm().toString())))
                .andExpect(content().string(containsString(testCurvePoint.getValue().toString())));
    }

    @Test
    @WithMockUser(username = loggedUsername)
    @Sql(scripts = populateScriptFilePath)
    void updateCurvePointTestIT() throws Exception {
        // Arrange
        List<CurvePoint> curvePoints = curvePointRepository.findAll();
        CurvePoint testCurvePoint = curvePoints.get(new Random().nextInt(curvePoints.size()));
        Integer testCurvePointId = testCurvePoint.getId();
        Integer newCurveId = new Random().nextInt();
        Double newTerm = new Random().nextDouble();
        Double newValue = new Random().nextDouble();
        // Act
        mockMvc.perform(post("/curvePoint/update/" + testCurvePointId)
                        .param("curveId", newCurveId.toString())
                        .param("term", newTerm.toString())
                        .param("value", newValue.toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("location", "/curvePoint/list"));
        // Assert
        CurvePoint testCurvePointAfter = curvePointRepository.findById(testCurvePointId).orElseThrow(() -> new CurvePointNotFoundException());
        assertThat(testCurvePointAfter.getCurveId()).isEqualTo(newCurveId);
        assertThat(testCurvePointAfter.getTerm()).isEqualTo(newTerm);
        assertThat(testCurvePointAfter.getValue()).isEqualTo(newValue);
    }

    @Test
    @WithMockUser(username = loggedUsername)
    @Sql(scripts = populateScriptFilePath)
    void deleteCurvePointTestIT() throws Exception {
        // Arrange
        List<CurvePoint> curvePoints = curvePointRepository.findAll();
        CurvePoint testCurvePoint = curvePoints.get(new Random().nextInt(curvePoints.size()));
        Integer testCurvePointId = testCurvePoint.getId();
        // Act
        mockMvc.perform(get("/curvePoint/delete/" + testCurvePointId))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("location", "/curvePoint/list"));
        // Arrange
        assertThat(curvePointRepository.findById(testCurvePointId)).isEmpty();

    }

    @Test
    @WithMockUser(username = loggedUsername)
    void wrongUrlTest() throws Exception {
        mockMvc.perform(get("/curvePoint/wrongUrl"))
                .andExpect(status().isNotFound());
    }


    @Test
    void notLoggedTest() throws Exception {
        mockMvc.perform(get("/curvePoint/list"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = loggedUsername)
    void curvePointNotFoundTest() throws Exception {
        mockMvc.perform(get("/curvePoint/update/100000"))
                .andExpect(status().isNotFound());
    }

}
