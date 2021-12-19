package com.manning.liveproject.simplysend.api;

import com.manning.liveproject.simplysend.BaseIntegrationTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static com.manning.liveproject.simplysend.auth.SecurityConstants.TOKEN_PREFIX;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserSessionLogoutIntegrationTest extends BaseIntegrationTest {

    private String token;

    @AfterAll
    public void tearDown() {
        cleanDb();
    }

    @BeforeEach
    public void commenceUserSession() throws Exception {
        token = login("rick@test.com", "Ch4ng*me0lease");
    }

    @Test
    public void givenUserSession_whenSendingRequest_thenReturnSuccess() throws Exception {
        mockMvc.perform(get("/items")
                        .header("Authorization", TOKEN_PREFIX + token))
                .andDo(print()).andExpect(status().isOk());
    }

    @Test
    public void givenNoUserSession_whenSendingRequest_thenReturnUnauthorised() throws Exception {
        logout(token);

        mockMvc.perform(get("/items")
                        .header("Authorization", TOKEN_PREFIX + token))
                .andDo(print()).andExpect(status().isUnauthorized());
    }
}
