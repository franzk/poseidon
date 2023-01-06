package com.nnk.springboot.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.exceptions.RuleNameNotFoundException;
import com.nnk.springboot.repositories.RuleNameRepository;
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
class RuleNameApiTestIT {
    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), StandardCharsets.UTF_8);
    private static final String loggedUsername = "testUser";
    private static final String populateScriptFilePath = "classpath:populate_test_db.sql";

    @Autowired
    private MockMvc mockMvc;

    ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private RuleNameRepository ruleNameRepository;

    @Test
    @WithMockUser(username = loggedUsername)
    @Sql(scripts = populateScriptFilePath)
    void createRuleNameTestIT() throws Exception {
        // Arrange
        RuleName testRuleName = new TestDataService().makeTestRuleName();
        testRuleName.setId(null);
        String requestJson = mapper.writeValueAsString(testRuleName);
        // Act
        mockMvc.perform(post("/api/ruleName").contentType(APPLICATION_JSON_UTF8).content(requestJson))
                .andExpect(status().isCreated());
        // Assert
        List<RuleName> ruleNames = ruleNameRepository.findAll();
        RuleName lastRuleName = ruleNames.get(ruleNames.size()-1);
        testRuleName.setId(lastRuleName.getId());
        assertThat(lastRuleName).isEqualTo(testRuleName);
    }

    @Test
    @WithMockUser(username = loggedUsername)
    @Sql(scripts = populateScriptFilePath)
    void getRuleNamesTestIT() throws Exception {
        // Arrange
        List<RuleName> ruleNames = ruleNameRepository.findAll();
        // Act
        ResultActions resultActions = mockMvc.perform(get("/api/ruleName/list")).andExpect(status().isOk());
        // Assert
        MvcResult result = resultActions.andReturn();
        String contentAsString = result.getResponse().getContentAsString();
        List<RuleName> resultData = mapper.readValue(contentAsString, new TypeReference<List<RuleName>>() {});
        assertThat(resultData).hasSize(ruleNames.size());
        int testId = new Random().nextInt(ruleNames.size());
        assertThat(resultData.get(testId)).isEqualTo(ruleNames.get(testId));
    }

    @Test
    @WithMockUser(username = loggedUsername)
    @Sql(scripts = populateScriptFilePath)
    void getRuleNameTestIT() throws Exception {
        // Arrange
        List<RuleName> ruleNames = ruleNameRepository.findAll();
        RuleName testRuleName = ruleNames.get(new Random().nextInt(ruleNames.size()));
        // Act
        ResultActions resultActions = mockMvc.perform(get("/api/ruleName")
                        .param("id", testRuleName.getId().toString()))
                .andExpect(status().isOk());
        // Assert
        MvcResult result = resultActions.andReturn();
        String contentAsString = result.getResponse().getContentAsString();
        RuleName resultData = mapper.readValue(contentAsString, RuleName.class);
        assertThat(resultData).isEqualTo(testRuleName);
    }


    @Test
    @WithMockUser(username = loggedUsername)
    @Sql(scripts = populateScriptFilePath)
    void updateRuleNameTest() throws Exception {
        // Arrange
        List<RuleName> ruleNames = ruleNameRepository.findAll();
        RuleName testRuleName = ruleNames.get(new Random().nextInt(ruleNames.size()));
        String nameAfterUpdate = RandomString.make(64);
        testRuleName.setName(nameAfterUpdate);
        String requestJson = mapper.writeValueAsString(testRuleName);
        // Act
        mockMvc.perform(put("/api/ruleName").contentType(APPLICATION_JSON_UTF8).content(requestJson))
                .andExpect(status().isOk());
        // Assert
        RuleName afterUpdate = ruleNameRepository.findById(testRuleName.getId()).orElseThrow(() -> new RuleNameNotFoundException());
        assertThat(afterUpdate.getName()).isEqualTo(nameAfterUpdate);
    }

    @Test
    @WithMockUser(username = loggedUsername)
    @Sql(scripts = populateScriptFilePath)
    void deleteRuleNameTest() throws Exception {
        // Arrange
        List<RuleName> ruleNames = ruleNameRepository.findAll();
        RuleName testRuleName = ruleNames.get(new Random().nextInt(ruleNames.size()));
        int sizeBefore = ruleNames.size();
        // Act
        mockMvc.perform(delete("/api/ruleName")
                        .param("id", testRuleName.getId().toString()))
                .andExpect(status().isOk());
        // Assert
        assertThat(ruleNameRepository.findAll()).hasSize(sizeBefore - 1);
        assertThat(ruleNameRepository.findById(testRuleName.getId())).isEmpty();
    }

    @Test
    @WithMockUser(username = loggedUsername)
    @Sql(scripts = populateScriptFilePath)
    void getRuleNameNotFoundExceptionTestIT() throws Exception {
        // Arrange
        Integer testId = 1000000;
        // Act + Assert
        mockMvc.perform(get("/api/ruleName").param("id", testId.toString()))
                .andExpect(status().isNotFound());
    }

    @Test
    void apiWithNoLoggedUserTestIT() throws Exception {
        mockMvc.perform(get("/api/ruleName/list"))
                .andExpect(status().isUnauthorized());
    }

}
