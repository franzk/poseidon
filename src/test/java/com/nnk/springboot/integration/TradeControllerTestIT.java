package com.nnk.springboot.integration;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.exceptions.TradeNotFoundException;
import com.nnk.springboot.repositories.TradeRepository;
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
class TradeControllerTestIT {
    private static final String loggedUsername = "testUser";
    private static final String populateScriptFilePath = "classpath:populate_test_db.sql";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TradeRepository tradeRepository;

    @Test
    @WithMockUser(username = loggedUsername)
    @Sql(scripts = populateScriptFilePath)
    void homeTestIT() throws Exception {
        // Arrange
        List<Trade> trades = tradeRepository.findAll();
        Trade testTrade = trades.get(new Random().nextInt(trades.size()));
        // Act + Assert
        mockMvc.perform(get("/trade/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("trade/list"))
                .andExpect(content().string(containsString(testTrade.getAccount())))
                .andExpect(content().string(containsString(testTrade.getType())))
                .andExpect(content().string(containsString(testTrade.getBuyQuantity().toString())));
    }

    @Test
    @WithMockUser(username = loggedUsername)
    @Sql(scripts = populateScriptFilePath)
    void addTradeFormTestIT() throws Exception {
        // Act + Assert
        mockMvc.perform(get("/trade/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("trade/add"));
    }

    @Test
    @WithMockUser(username = loggedUsername)
    @Sql(scripts = populateScriptFilePath)
    void validateTestIT() throws Exception {
        // Arrange
        Trade testTrade = new TestDataService().makeTestTrade();
        // Act
        mockMvc.perform(post("/trade/validate")
                        .param("account", testTrade.getAccount())
                        .param("type", testTrade.getType())
                        .param("buyQuantity", testTrade.getBuyQuantity().toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("location", "/trade/list"));
        // Assert
        List<Trade> trades = tradeRepository.findAll();
        Trade lastTrade = trades.get(trades.size()-1);
        assertThat(lastTrade.getAccount()).isEqualTo(testTrade.getAccount());
        assertThat(lastTrade.getType()).isEqualTo(testTrade.getType());
        assertThat(lastTrade.getBuyQuantity()).isEqualTo(testTrade.getBuyQuantity());
    }

    @Test
    @WithMockUser(username = loggedUsername)
    @Sql(scripts = populateScriptFilePath)
    void validateValidAnnotationTestIT() throws Exception {
        // Act + Assert
        mockMvc.perform(post("/trade/validate")) // no params : params = null
                .andExpect(view().name("trade/add"));
    }

    @Test
    @WithMockUser(username = loggedUsername)
    @Sql(scripts = populateScriptFilePath)
    void showUpdateFormTestIT() throws Exception {
        // Arrange
        List<Trade> trades = tradeRepository.findAll();
        Trade testTrade = trades.get(new Random().nextInt(trades.size()));
        Integer testTradeId = testTrade.getTradeId();
        // Act + Assert
        mockMvc.perform(get("/trade/update/" + testTradeId))
                .andExpect(status().isOk())
                .andExpect(view().name("trade/update"))
                .andExpect(content().string(containsString(testTrade.getAccount())))
                .andExpect(content().string(containsString(testTrade.getType())))
                .andExpect(content().string(containsString(testTrade.getBuyQuantity().toString())));
    }

    @Test
    @WithMockUser(username = loggedUsername)
    @Sql(scripts = populateScriptFilePath)
    void updateTradeTestIT() throws Exception {
        // Arrange
        List<Trade> trades = tradeRepository.findAll();
        Trade testTrade = trades.get(new Random().nextInt(trades.size()));
        Integer testTradeId = testTrade.getTradeId();
        String newAccount = RandomString.make(64);
        String newType = RandomString.make(64);
        Double newBuyQuantity = new Random().nextDouble();
        // Act
        mockMvc.perform(post("/trade/update/" + testTradeId)
                        .param("account", newAccount)
                        .param("type", newType)
                        .param("buyQuantity", newBuyQuantity.toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("location", "/trade/list"));
        // Assert
        Trade testTradeAfter = tradeRepository.findById(testTradeId).orElseThrow(() -> new TradeNotFoundException());
        assertThat(testTradeAfter.getAccount()).isEqualTo(newAccount);
        assertThat(testTradeAfter.getType()).isEqualTo(newType);
        assertThat(testTradeAfter.getBuyQuantity()).isEqualTo(newBuyQuantity);
    }

    @Test
    @WithMockUser(username = loggedUsername)
    @Sql(scripts = populateScriptFilePath)
    void deleteTradeTestIT() throws Exception {
        // Arrange
        List<Trade> trades = tradeRepository.findAll();
        Trade testTrade = trades.get(new Random().nextInt(trades.size()));
        Integer testTradeId = testTrade.getTradeId();
        // Act
        mockMvc.perform(get("/trade/delete/" + testTradeId))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("location", "/trade/list"));
        // Arrange
        assertThat(tradeRepository.findById(testTradeId)).isEmpty();

    }

    @Test
    @WithMockUser(username = loggedUsername)
    void wrongUrlTest() throws Exception {
        mockMvc.perform(get("/trade/wrongUrl"))
                .andExpect(status().isNotFound());
    }


    @Test
    void notLoggedTest() throws Exception {
        mockMvc.perform(get("/trade/list"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = loggedUsername)
    void tradeNotFoundTest() throws Exception {
        mockMvc.perform(get("/trade/update/100000"))
                .andExpect(status().isNotFound());
    }
}
