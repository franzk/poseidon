package com.nnk.springboot.integration;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.exceptions.BidListNotFoundException;
import com.nnk.springboot.repositories.BidListRepository;
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

@SpringBootTest()
@AutoConfigureMockMvc
@ActiveProfiles("test")
class BidListControllerTestIT {
    private static final String loggedUsername = "testUser";
    private static final String populateScriptFilePath = "classpath:populate_test_db.sql";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BidListRepository bidListRepository;

    @Test
    @WithMockUser(username = loggedUsername)
    void baseUrlTest() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("location", "/bidList/list"));
    }

    @Test
    @WithMockUser(username = loggedUsername)
    @Sql(scripts = populateScriptFilePath)
    void homeTestIT() throws Exception {
        // Arrange
        List<BidList> bidLists = bidListRepository.findAll();
        BidList testBidList = bidLists.get(new Random().nextInt(bidLists.size()));
        // Act + Assert
        mockMvc.perform(get("/bidList/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("bidList/list"))
                .andExpect(content().string(containsString(testBidList.getAccount())))
                .andExpect(content().string(containsString(testBidList.getType())))
                .andExpect(content().string(containsString(testBidList.getBidQuantity().toString())));
    }

    @Test
    @WithMockUser(username = loggedUsername)
    @Sql(scripts = populateScriptFilePath)
    void addBidFormTestIT() throws Exception {
        // Act + Assert
        mockMvc.perform(get("/bidList/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("bidList/add"));
    }

    @Test
    @WithMockUser(username = loggedUsername)
    @Sql(scripts = populateScriptFilePath)
    void validateTestIT() throws Exception {
        // Arrange
        BidList testBidList = new TestDataService().makeTestBidList();
        // Act
        mockMvc.perform(post("/bidList/validate")
                        .param("account", testBidList.getAccount())
                        .param("type", testBidList.getType())
                        .param("bidQuantity", testBidList.getBidQuantity().toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("location", "/bidList/list"));
        // Assert
        List<BidList> bidLists = bidListRepository.findAll();
        BidList lastBidList = bidLists.get(bidLists.size()-1);
        assertThat(lastBidList.getAccount()).isEqualTo(testBidList.getAccount());
        assertThat(lastBidList.getType()).isEqualTo(testBidList.getType());
        assertThat(lastBidList.getBidQuantity()).isEqualTo(testBidList.getBidQuantity());
    }

    @Test
    @WithMockUser(username = loggedUsername)
    @Sql(scripts = populateScriptFilePath)
    void validateValidAnnotationTestIT() throws Exception {
        // Act + Assert
        mockMvc.perform(post("/bidList/validate")) // no params : params = null
                .andExpect(view().name("bidList/add"));
    }

    @Test
    @WithMockUser(username = loggedUsername)
    @Sql(scripts = populateScriptFilePath)
    void showUpdateFormTestIT() throws Exception {
        // Arrange
        List<BidList> bidLists = bidListRepository.findAll();
        BidList testBidList = bidLists.get(new Random().nextInt(bidLists.size()));
        Integer testBidListId = testBidList.getBidListId();
        // Act + Assert
        mockMvc.perform(get("/bidList/update/" + testBidListId))
                .andExpect(status().isOk())
                .andExpect(view().name("bidList/update"))
                .andExpect(content().string(containsString(testBidList.getAccount())))
                .andExpect(content().string(containsString(testBidList.getType())))
                .andExpect(content().string(containsString(testBidList.getBidQuantity().toString())));
    }

    @Test
    @WithMockUser(username = loggedUsername)
    @Sql(scripts = populateScriptFilePath)
    void updateBidTestIT() throws Exception {
        // Arrange
        List<BidList> bidLists = bidListRepository.findAll();
        BidList testBidList = bidLists.get(new Random().nextInt(bidLists.size()));
        Integer testBidListId = testBidList.getBidListId();
        String newAccount = RandomString.make(64);
        String newType = RandomString.make(64);
        Double newBidQuantity = new Random().nextDouble();
        // Act
        mockMvc.perform(post("/bidList/update/" + testBidListId)
                        .param("account", newAccount)
                        .param("type", newType)
                        .param("bidQuantity", newBidQuantity.toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("location", "/bidList/list"));
        // Assert
        BidList testBidListAfter = bidListRepository.findById(testBidListId).orElseThrow(() -> new BidListNotFoundException());
        assertThat(testBidListAfter.getAccount()).isEqualTo(newAccount);
        assertThat(testBidListAfter.getType()).isEqualTo(newType);
        assertThat(testBidListAfter.getBidQuantity()).isEqualTo(newBidQuantity);
    }

    @Test
    @WithMockUser(username = loggedUsername)
    @Sql(scripts = populateScriptFilePath)
    void deleteBidTestIT() throws Exception {
        // Arrange
        List<BidList> bidLists = bidListRepository.findAll();
        BidList testBidList = bidLists.get(new Random().nextInt(bidLists.size()));
        Integer testBidListId = testBidList.getBidListId();
        // Act
        mockMvc.perform(get("/bidList/delete/" + testBidListId))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("location", "/bidList/list"));
        // Arrange
        assertThat(bidListRepository.findById(testBidListId)).isEmpty();

    }

    @Test
    @WithMockUser(username = loggedUsername)
    void wrongUrlTest() throws Exception {
        mockMvc.perform(get("/bidList/wrongUrl"))
                .andExpect(status().isNotFound());
    }


    @Test
    void notLoggedTest() throws Exception {
        mockMvc.perform(get("/bidList/list"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = loggedUsername)
    void bidListNotFoundTest() throws Exception {
        mockMvc.perform(get("/bidList/update/100000"))
                .andExpect(status().isNotFound());
    }
}
