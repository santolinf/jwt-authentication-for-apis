package com.manning.liveproject.simplysend;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.manning.liveproject.simplysend.api.enums.Role;
import com.manning.liveproject.simplysend.auth.service.TokenService;
import com.manning.liveproject.simplysend.entity.UserAccount;
import com.manning.liveproject.simplysend.entity.UserProfile;
import com.manning.liveproject.simplysend.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

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
    private TokenService tokenService;

    @Autowired
    protected UserAccountRepository userAccountRepository;

    protected void createAccountInDb(String emailId, String password) {
        UserAccount account = UserAccount.builder()
                .username(emailId)
                .password(passwordEncoder.encode(password))
                .enabled(true)
                .profile(UserProfile.builder()
                        .email(emailId)
                        .role(Role.REPORTEE)
                        .build()
                )
                .build();
        userAccountRepository.save(account);
    }

    protected String login(String emailId, String password) {
        createAccountInDb(emailId, password);
        return tokenService.generateToken(new TestingAuthenticationToken(emailId, password));
    }

    protected void cleanDb() {
        userAccountRepository.deleteAll();
    }
}
