package com.nnk.springboot.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.exceptions.TradeNotFoundException;
import com.nnk.springboot.repositories.TradeRepository;
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
class TradeApiTestIT {
    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), StandardCharsets.UTF_8);
    private static final String loggedUsername = "testUser";
    private static final String populateScriptFilePath = "classpath:populate_test_db.sql";

    @Autowired
    private MockMvc mockMvc;

    ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private TradeRepository tradeRepository;

    @Test
    @WithMockUser(username = loggedUsername)
    @Sql(scripts = populateScriptFilePath)
    void createTradeTestIT() throws Exception {
        // Arrange
        Trade testTrade = new TestDataService().makeTestTrade();
        testTrade.setTradeId(null);
        String requestJson = mapper.writeValueAsString(testTrade);
        // Act
        mockMvc.perform(post("/api/trade").contentType(APPLICATION_JSON_UTF8).content(requestJson))
                .andExpect(status().isCreated());
        // Assert
        List<Trade> trades = tradeRepository.findAll();
        Trade lastTrade = trades.get(trades.size()-1);
        testTrade.setTradeId(lastTrade.getTradeId());
        assertThat(lastTrade).isEqualTo(testTrade);
    }

    @Test
    @WithMockUser(username = loggedUsername)
    @Sql(scripts = populateScriptFilePath)
    void getTradesTestIT() throws Exception {
        // Arrange
        List<Trade> trades = tradeRepository.findAll();
        // Act
        ResultActions resultActions = mockMvc.perform(get("/api/trade/list")).andExpect(status().isOk());
        // Assert
        MvcResult result = resultActions.andReturn();
        String contentAsString = result.getResponse().getContentAsString();
        List<Trade> resultData = mapper.readValue(contentAsString, new TypeReference<List<Trade>>() {});
        assertThat(resultData).hasSize(trades.size());
        int testId = new Random().nextInt(trades.size());
        assertThat(resultData.get(testId)).isEqualTo(trades.get(testId));
    }

    @Test
    @WithMockUser(username = loggedUsername)
    @Sql(scripts = populateScriptFilePath)
    void getTradeTestIT() throws Exception {
        // Arrange
        List<Trade> trades = tradeRepository.findAll();
        Trade testTrade = trades.get(new Random().nextInt(trades.size()));
        // Act
        ResultActions resultActions = mockMvc.perform(get("/api/trade")
                        .param("id", testTrade.getTradeId().toString()))
                .andExpect(status().isOk());
        // Assert
        MvcResult result = resultActions.andReturn();
        String contentAsString = result.getResponse().getContentAsString();
        Trade resultData = mapper.readValue(contentAsString, Trade.class);
        assertThat(resultData).isEqualTo(testTrade);
    }


    @Test
    @WithMockUser(username = loggedUsername)
    @Sql(scripts = populateScriptFilePath)
    void updateTradeTest() throws Exception {
        // Arrange
        List<Trade> trades = tradeRepository.findAll();
        Trade testTrade = trades.get(new Random().nextInt(trades.size()));
        String accountAfterUpdate = RandomString.make(64);
        testTrade.setAccount(accountAfterUpdate);
        String requestJson = mapper.writeValueAsString(testTrade);
        // Act
        mockMvc.perform(put("/api/trade").contentType(APPLICATION_JSON_UTF8).content(requestJson))
                .andExpect(status().isOk());
        // Assert
        Trade afterUpdate = tradeRepository.findById(testTrade.getTradeId()).orElseThrow(() -> new TradeNotFoundException());
        assertThat(afterUpdate.getAccount()).isEqualTo(accountAfterUpdate);

    }

    @Test
    @WithMockUser(username = loggedUsername)
    @Sql(scripts = populateScriptFilePath)
    void deleteTradeTest() throws Exception {
        // Arrange
        List<Trade> trades = tradeRepository.findAll();
        Trade testTrade = trades.get(new Random().nextInt(trades.size()));
        int sizeBefore = trades.size();
        // Act
        mockMvc.perform(delete("/api/trade")
                        .param("id", testTrade.getTradeId().toString()))
                .andExpect(status().isOk());
        // Assert
        assertThat(tradeRepository.findAll()).hasSize(sizeBefore - 1);
        assertThat(tradeRepository.findById(testTrade.getTradeId())).isEmpty();
    }

    @Test
    @WithMockUser(username = loggedUsername)
    @Sql(scripts = populateScriptFilePath)
    void getTradeNotFoundExceptionTestIT() throws Exception {
        // Arrange
        Integer testId = 1000000;
        // Act + Assert
        mockMvc.perform(get("/api/trade").param("id", testId.toString()))
                .andExpect(status().isNotFound());
    }

    @Test
    void apiWithNoLoggedUserTestIT() throws Exception {
        mockMvc.perform(get("/api/trade/list"))
                .andExpect(status().isUnauthorized());
    }
}
