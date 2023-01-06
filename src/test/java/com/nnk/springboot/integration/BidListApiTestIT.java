package com.nnk.springboot.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.exceptions.BidListNotFoundException;
import com.nnk.springboot.repositories.BidListRepository;
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

@SpringBootTest()
@AutoConfigureMockMvc
@ActiveProfiles("test")
class BidListApiTestIT {

    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), StandardCharsets.UTF_8);
    private static final String loggedUsername = "testUser";
    private static final String populateScriptFilePath = "classpath:populate_test_db.sql";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BidListRepository bidListRepository;

    ObjectMapper mapper = new ObjectMapper();

    @Test
    @WithMockUser(username = loggedUsername)
    @Sql(scripts = populateScriptFilePath)
    void createBidListTestIT() throws Exception {
        // Arrange
        BidList testBidList = new TestDataService().makeTestBidList();
        testBidList.setBidListId(null);
        String requestJson = mapper.writeValueAsString(testBidList);
        // Act
        mockMvc.perform(post("/api/bidList").contentType(APPLICATION_JSON_UTF8).content(requestJson))
                .andExpect(status().isCreated());
        // Assert
        List<BidList> bidLists = bidListRepository.findAll();
        BidList lastBidList = bidLists.get(bidLists.size()-1);
        testBidList.setBidListId(lastBidList.getBidListId());
        assertThat(lastBidList).isEqualTo(testBidList);
    }

    @Test
    @WithMockUser(username = loggedUsername)
    @Sql(scripts = populateScriptFilePath)
    void createBidListWithInvalidAccountTestIT() throws Exception {
        // Arrange
        BidList testBidList = new TestDataService().makeTestBidList();
        testBidList.setBidListId(null);
        testBidList.setAccount("");
        String requestJson = mapper.writeValueAsString(testBidList);
        int bidListCountBefore = bidListRepository.findAll().size();
        // Act
        mockMvc.perform(post("/api/bidList").contentType(APPLICATION_JSON_UTF8).content(requestJson))
                .andExpect(status().isBadRequest());
        // Assert
        assertThat(bidListCountBefore).isEqualTo(bidListRepository.findAll().size()); // verify that no bidlist were added
    }

    @Test
    @WithMockUser(username = loggedUsername)
    @Sql(scripts = populateScriptFilePath)
    void getBidListsTestIT() throws Exception {
        // Arrange
        List<BidList> bidLists = bidListRepository.findAll();
        // Act
        ResultActions resultActions = mockMvc.perform(get("/api/bidList/list")).andExpect(status().isOk());
        // Assert
        MvcResult result = resultActions.andReturn();
        String contentAsString = result.getResponse().getContentAsString();
        List<BidList> resultData = mapper.readValue(contentAsString, new TypeReference<List<BidList>>() {});
        assertThat(resultData).hasSize(bidLists.size());
        int randomId = new Random().nextInt(bidLists.size());
        assertThat(resultData.get(randomId)).isEqualTo(bidLists.get(randomId));
    }

    @Test
    @WithMockUser(username = loggedUsername)
    @Sql(scripts = populateScriptFilePath)
    void getBidListTestIT() throws Exception {
        // Arrange
        List<BidList> bidLists = bidListRepository.findAll();
        BidList testBidList = bidLists.get(new Random().nextInt(bidLists.size()));
        // Act
        ResultActions resultActions = mockMvc.perform(get("/api/bidList")
                    .param("id", testBidList.getBidListId().toString()))
                .andExpect(status().isOk());
        // Assert
        MvcResult result = resultActions.andReturn();
        String contentAsString = result.getResponse().getContentAsString();
        BidList resultData = mapper.readValue(contentAsString, BidList.class);
        assertThat(resultData).isEqualTo(testBidList);
    }


    @Test
    @WithMockUser(username = loggedUsername)
    @Sql(scripts = populateScriptFilePath)
    void updateBidListTest() throws Exception {
        // Arrange
        List<BidList> bidLists = bidListRepository.findAll();
        BidList testBidList = bidLists.get(new Random().nextInt(bidLists.size()));
        String accountAfterUpdate = RandomString.make(64);
        testBidList.setAccount(accountAfterUpdate);
        String requestJson = mapper.writeValueAsString(testBidList);
        // Act
        mockMvc.perform(put("/api/bidList").contentType(APPLICATION_JSON_UTF8).content(requestJson))
                .andExpect(status().isOk());
        // Assert
        BidList afterUpdate = bidListRepository.findById(testBidList.getBidListId()).orElseThrow(() -> new BidListNotFoundException());
        assertThat(afterUpdate.getAccount()).isEqualTo(accountAfterUpdate);

    }

    @Test
    @WithMockUser(username = loggedUsername)
    @Sql(scripts = populateScriptFilePath)
    void deleteBidListTest() throws Exception {
        // Arrange
        List<BidList> bidLists = bidListRepository.findAll();
        BidList testBidList = bidLists.get(new Random().nextInt(bidLists.size()));
        int sizeBefore = bidLists.size();
        // Act
        mockMvc.perform(delete("/api/bidList")
                        .param("id", testBidList.getBidListId().toString()))
                .andExpect(status().isOk());
        // Assert
        assertThat(bidListRepository.findAll()).hasSize(sizeBefore - 1);
        assertThat(bidListRepository.findById(testBidList.getBidListId())).isEmpty();
    }


    @Test
    @WithMockUser(username = loggedUsername)
    @Sql(scripts = populateScriptFilePath)
    void getBidListWithNotFoundExceptionTestIT() throws Exception {
        // Arrange
       Integer testId = 1000000;
        // Act + Assert
        mockMvc.perform(get("/api/bidList").param("id", testId.toString()))
                .andExpect(status().isNotFound());
    }

    @Test
    void apiWithNoLoggedUserTestIT() throws Exception {
        mockMvc.perform(get("/api/bidList/list"))
                .andExpect(status().isUnauthorized());
    }


}
