package com.nnk.springboot.integration;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.exceptions.RuleNameNotFoundException;
import com.nnk.springboot.repositories.RuleNameRepository;
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
class RuleNameControllerTestIT {
    private static final String loggedUsername = "testUser";
    private static final String populateScriptFilePath = "classpath:populate_test_db.sql";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RuleNameRepository ruleNameRepository;

    @Test
    @WithMockUser(username = loggedUsername)
    @Sql(scripts = populateScriptFilePath)
    void homeTestIT() throws Exception {
        // Arrange
        List<RuleName> ruleNames = ruleNameRepository.findAll();
        RuleName testRuleName = ruleNames.get(new Random().nextInt(ruleNames.size()));
        // Act + Assert
        mockMvc.perform(get("/ruleName/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("ruleName/list"))
                .andExpect(content().string(containsString(testRuleName.getName())))
                .andExpect(content().string(containsString(testRuleName.getDescription())))
                .andExpect(content().string(containsString(testRuleName.getJson())))
                .andExpect(content().string(containsString(testRuleName.getTemplate())))
                .andExpect(content().string(containsString(testRuleName.getSqlStr())))
                .andExpect(content().string(containsString(testRuleName.getSqlPart())));
    }

    @Test
    @WithMockUser(username = loggedUsername)
    @Sql(scripts = populateScriptFilePath)
    void addRuleNameFormTestIT() throws Exception {
        // Act + Assert
        mockMvc.perform(get("/ruleName/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("ruleName/add"));
    }

    @Test
    @WithMockUser(username = loggedUsername)
    @Sql(scripts = populateScriptFilePath)
    void validateTestIT() throws Exception {
        // Arrange
        RuleName testRuleName = new TestDataService().makeTestRuleName();
        // Act
        mockMvc.perform(post("/ruleName/validate")
                        .param("name", testRuleName.getName())
                        .param("description", testRuleName.getDescription())
                        .param("json", testRuleName.getJson())
                        .param("template", testRuleName.getTemplate())
                        .param("sqlStr", testRuleName.getSqlStr())
                        .param("sqlPart", testRuleName.getSqlPart()))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("location", "/ruleName/list"));
        // Assert
        List<RuleName> ruleNames = ruleNameRepository.findAll();
        RuleName lastRuleName = ruleNames.get(ruleNames.size()-1);
        assertThat(lastRuleName.getName()).isEqualTo(testRuleName.getName());
        assertThat(lastRuleName.getDescription()).isEqualTo(testRuleName.getDescription());
        assertThat(lastRuleName.getJson()).isEqualTo(testRuleName.getJson());
        assertThat(lastRuleName.getTemplate()).isEqualTo(testRuleName.getTemplate());
        assertThat(lastRuleName.getSqlStr()).isEqualTo(testRuleName.getSqlStr());
        assertThat(lastRuleName.getSqlPart()).isEqualTo(testRuleName.getSqlPart());
    }

@Test
    @WithMockUser(username = loggedUsername)
    @Sql(scripts = populateScriptFilePath)
    void validateValidAnnotationTestIT() throws Exception {
        // Act + Assert
        mockMvc.perform(post("/ruleName/validate")) // no param : params = null
                .andExpect(view().name("ruleName/add"));
    }

    @Test
    @WithMockUser(username = loggedUsername)
    @Sql(scripts = populateScriptFilePath)
    void showUpdateFormTestIT() throws Exception {
        // Arrange
        List<RuleName> ruleNames = ruleNameRepository.findAll();
        RuleName testRuleName = ruleNames.get(new Random().nextInt(ruleNames.size()));
        Integer testRuleNameId = testRuleName.getId();
        // Act + Assert
        mockMvc.perform(get("/ruleName/update/" + testRuleNameId))
                .andExpect(status().isOk())
                .andExpect(view().name("ruleName/update"))
                .andExpect(content().string(containsString(testRuleName.getName())))
                .andExpect(content().string(containsString(testRuleName.getDescription())))
                .andExpect(content().string(containsString(testRuleName.getJson())))
                .andExpect(content().string(containsString(testRuleName.getTemplate())))
                .andExpect(content().string(containsString(testRuleName.getSqlStr())))
                .andExpect(content().string(containsString(testRuleName.getSqlPart())));
    }

    @Test
    @WithMockUser(username = loggedUsername)
    @Sql(scripts = populateScriptFilePath)
    void updateRuleNameTestIT() throws Exception {
        // Arrange
        List<RuleName> ruleNames = ruleNameRepository.findAll();
        RuleName testRuleName = ruleNames.get(new Random().nextInt(ruleNames.size()));
        Integer testRuleNameId = testRuleName.getId();
        String newName = RandomString.make(64);
        String newDescription = RandomString.make(64);
        String newJson = RandomString.make(64);
        String newTemplate = RandomString.make(64);
        String newSqlStr = RandomString.make(64);
        String newSqlPart = RandomString.make(64);
        // Act
        mockMvc.perform(post("/ruleName/update/" + testRuleNameId)
                        .param("name", newName)
                        .param("description", newDescription)
                        .param("json", newJson)
                        .param("template", newTemplate)
                        .param("sqlStr", newSqlStr)
                        .param("sqlPart", newSqlPart))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("location", "/ruleName/list"));
        // Assert
        RuleName testRuleNameAfter = ruleNameRepository.findById(testRuleNameId).orElseThrow(() -> new RuleNameNotFoundException());
        assertThat(testRuleNameAfter.getName()).isEqualTo(newName);
    }

    @Test
    @WithMockUser(username = loggedUsername)
    @Sql(scripts = populateScriptFilePath)
    void deleteRuleNameTestIT() throws Exception {
        // Arrange
        List<RuleName> ruleNames = ruleNameRepository.findAll();
        RuleName testRuleName = ruleNames.get(new Random().nextInt(ruleNames.size()));
        Integer testRuleNameId = testRuleName.getId();
        // Act
        mockMvc.perform(get("/ruleName/delete/" + testRuleNameId))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("location", "/ruleName/list"));
        // Arrange
        assertThat(ruleNameRepository.findById(testRuleNameId)).isEmpty();

    }

    @Test
    @WithMockUser(username = loggedUsername)
    void wrongUrlTest() throws Exception {
        mockMvc.perform(get("/ruleName/wrongUrl"))
                .andExpect(status().isNotFound());
    }


    @Test
    void notLoggedTest() throws Exception {
        mockMvc.perform(get("/ruleName/list"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = loggedUsername)
    void ruleNameNotFoundTest() throws Exception {
        mockMvc.perform(get("/ruleName/update/100000"))
                .andExpect(status().isNotFound());
    }
}
