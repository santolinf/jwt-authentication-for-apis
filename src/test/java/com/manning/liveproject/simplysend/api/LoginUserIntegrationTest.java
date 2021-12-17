package com.manning.liveproject.simplysend.api;

import com.manning.liveproject.simplysend.BaseIntegrationTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.matchesPattern;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LoginUserIntegrationTest extends BaseIntegrationTest {

    @AfterAll
    public void tearDown() {
        cleanDb();
    }

    @Test
    public void givenNonExistentUsername_whenLogin_thenReturnUnauthorised() throws Exception {
        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("emailId", "I.dont.exist@test.com")
                .param("password", "opensesame"))
                .andDo(print()).andExpect(status().isUnauthorized());
    }

    @Test
    public void givenIncorrectPassword_whenLogin_thenReturnUnauthorised() throws Exception {
        String emailId = "bob@test.com";
        String password = "Ch4ng*me0lease";

        createAccountInDb(emailId, password);

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("emailId", emailId)
                        .param("password", "opensesame"))
                .andDo(print()).andExpect(status().isUnauthorized());
    }

    @Test
    public void givenValidCredentials_whenLogin_thenReturnToken() throws Exception {
        String emailId = "mary@test.com";
        String password = "Ch4ng*me0lease";

        createAccountInDb(emailId, password);

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("emailId", emailId)
                        .param("password", password))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(header().exists("Authorization"))
                .andExpect(header().string("Authorization", matchesPattern("Bearer [^\\s]+")));
    }
}
