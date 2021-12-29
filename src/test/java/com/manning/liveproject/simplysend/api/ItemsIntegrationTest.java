package com.manning.liveproject.simplysend.api;

import com.manning.liveproject.simplysend.BaseIntegrationTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static com.manning.liveproject.simplysend.auth.SecurityConstants.TOKEN_PREFIX;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ItemsIntegrationTest extends BaseIntegrationTest {

    @AfterAll
    public void tearDown() {
        cleanDb();
    }

    @Test
    public void givenUnauthenticatedRequest_whenListItems_thenReturnUnauthorised() throws Exception {
        mockMvc.perform(get("/items"))
                .andDo(print()).andExpect(status().isUnauthorized());
    }

    @Test
    public void givenNoItemsInDb_whenListItems_thenReturnEmptyResponse() throws Exception {
        String token = loginAs("james@test.com");

        mockMvc.perform(get("/items")
                        .with(csrf().asHeader())
                        .header("Authorization", TOKEN_PREFIX + token))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty())
                .andExpect(header().doesNotExist(ApiConstants.HEADER_LINK))
                .andExpect(header().string(ApiConstants.HEADER_X_PAGE_NUMBER, "0"))
                .andExpect(header().string(ApiConstants.HEADER_X_TOTAL_PAGES, "0"))
                .andExpect(header().string(ApiConstants.HEADER_X_TOTAL_ELEMENTS, "0"));
    }
}
