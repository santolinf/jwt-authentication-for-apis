package com.manning.liveproject.simplysend.api;

import com.manning.liveproject.simplysend.BaseIntegrationTest;
import com.manning.liveproject.simplysend.api.dto.UserDto;
import com.manning.liveproject.simplysend.api.enums.Role;
import com.manning.liveproject.simplysend.entity.UserAccount;
import com.manning.liveproject.simplysend.entity.UserProfile;
import com.manning.liveproject.simplysend.repository.UserAccountRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CreateUserIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Test
    public void givenMissingEmailId_whenCreateUser_thenReturnBadRequest() throws Exception {
        UserDto emptyUserPayload = UserDto.builder().build();

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(emptyUserPayload)))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Field values violations"))
                .andExpect(jsonPath("$.violations.emailId").value("must not be null"));
    }

    @Test
    public void givenInvalidEmailId_whenCreateUser_thenReturnBadRequest() throws Exception {
        UserDto newUserPayload = UserDto.builder().emailId("frank@test").build();

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newUserPayload)))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Field values violations"))
                .andExpect(jsonPath("$.violations.emailId").value("must be a well-formed email address"));
    }

    @Test
    public void givenExistingUsername_whenCreateUser_thenReturnConflict() throws Exception {
        String email = "bob@test.com";
        UserAccount existingUser = UserAccount.builder()
                .username(email)
                .profile(UserProfile.builder().email(email).build())
                .build();
        userAccountRepository.save(existingUser);

        UserDto newUserPayload = UserDto.builder().emailId(email)
                .password("Ch4ng*me0lease")
                .phone("0555555555")
                .build();

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUserPayload)))
                .andDo(print()).andExpect(status().isConflict());
    }

    @Test
    public void givenInvalidPassword_whenCreateUser_thenReturnBadRequest() throws Exception {
        UserDto newUserPayload = UserDto.builder().emailId("fred@test.com").phone("0555555555")
                .password("opensesame")
                .build();

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUserPayload)))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Field values violations"))
                .andExpect(jsonPath("$.violations.password").value("password should contain, at minimum, one number, one capital letter, and one special character"));
    }

    @Test
    public void whenCreateUser_thenPersistUserDetails() throws Exception {
        String username = "mary@test.com";
        String plainTextPassword = "Open5esame!";
        UserDto newUserPayload = UserDto.builder()
                .emailId(username)
                .password(plainTextPassword)
                .firstName("Mary")
                .lastName("Smith")
                .age(30)
                .address("123 Phantom Road")
                .phone("0555555555")
                .role(Role.REPORTEE)
                .managerName("Adam Smith")
                .tag("my-tag")
                .build();

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUserPayload)))
                .andDo(print()).andExpect(status().isCreated());

        UserAccount account = userAccountRepository.findByUsername(username).orElse(null);
        assertThat(account).isNotNull();
        assertThat(account.getEnabled()).isFalse();
        assertThat(account.getPassword()).isNotNull();
        assertThat(account.getPassword()).isNotEqualTo(plainTextPassword);

        assertThat(account.getProfile()).isNotNull();
    }
}
