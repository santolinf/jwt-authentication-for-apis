package com.manning.liveproject.simplysend;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.manning.liveproject.simplysend.api.enums.Role;
import com.manning.liveproject.simplysend.auth.SecurityConstants;
import com.manning.liveproject.simplysend.entity.UserAccount;
import com.manning.liveproject.simplysend.entity.User;
import com.manning.liveproject.simplysend.repository.ItemRepository;
import com.manning.liveproject.simplysend.repository.OrderRepository;
import com.manning.liveproject.simplysend.repository.UserAccountRepository;
import com.manning.liveproject.simplysend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static com.manning.liveproject.simplysend.auth.SecurityConstants.TOKEN_PREFIX;
import static org.assertj.core.api.Assertions.fail;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
public abstract class BaseIntegrationTest {

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    protected UserAccountRepository userAccountRepository;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected OrderRepository orderRepository;

    @Autowired
    protected ItemRepository itemRepository;

    protected void createAccountInDb(String emailId, String password) {
        createAccountInDb(emailId, password, Role.REPORTEE);
    }

    protected void createAccountInDb(String emailId, String password, Role role) {
        UserAccount account = UserAccount.builder()
                .username(emailId)
                .password(passwordEncoder.encode(password))
                .enabled(true)
                .user(User.builder()
                        .email(emailId)
                        .role(role)
                        .build()
                )
                .build();
        userAccountRepository.save(account);
    }

    protected String loginAs(String emailId) throws Exception {
        return loginAs(emailId, Role.REPORTEE);
    }

    protected String loginAs(String emailId, Role role) throws Exception {
        String password = "Ch4ng*me0lease";
        if (userAccountRepository.findByUsername(emailId).isEmpty()) {
            createAccountInDb(emailId, password, role);
        }

        String token = mockMvc.perform(post("/login")
                        .with(csrf().asHeader())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("emailId", emailId)
                        .param("password", password))
                .andDo(print()).andExpect(status().isOk())
                .andReturn().getResponse().getHeader("Authorization");

        return token.replace(SecurityConstants.TOKEN_PREFIX, "");
    }

    protected void failLoginAs(String emailId) throws Exception {
        failLoginAs(emailId, Role.REPORTEE);
    }

    protected void failLoginAs(String emailId, Role role)  throws Exception {
        try {
            loginAs(emailId, role);
        } catch (AssertionError loginFailure) {
            // expecting this to happen
            return;
        }

        fail(emailId  + " has successfully logged in");
    }

    protected void logout(String token) throws Exception {
        mockMvc.perform(post("/logout")
                        .with(csrf().asHeader())
                        .header("Authorization", TOKEN_PREFIX + token))
                .andDo(print()).andExpect(status().isOk());
    }

    protected void cleanDb() {
        orderRepository.deleteAll();
        itemRepository.deleteAll();
        userAccountRepository.deleteAll();
    }
}
