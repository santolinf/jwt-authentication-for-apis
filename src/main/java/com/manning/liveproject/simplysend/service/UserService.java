package com.manning.liveproject.simplysend.service;

import com.manning.liveproject.simplysend.api.dto.UserDto;
import com.manning.liveproject.simplysend.entity.UserAccount;
import com.manning.liveproject.simplysend.entity.UserProfile;
import com.manning.liveproject.simplysend.exceptions.UsernameAlreadyExistsException;
import com.manning.liveproject.simplysend.mapper.UserMapper;
import com.manning.liveproject.simplysend.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper mapper;
    private final UserAccountRepository userAccountRepository;

    public void createUser(UserDto user) {
        UserAccount account = mapper.userDtoToUserAccount(user);
        account.setEnabled(true);

        UserProfile profile = mapper.userDtoToUserProfile(user);
        account.setProfile(profile);
        // TODO !Strings.isNullOrEmpty(user.getManagerName())

        saveUser(account);
        log.info("Created account: {}", account);
    }

    private void saveUser(UserAccount account) {
        try {
            userAccountRepository.save(account);
        } catch (DataIntegrityViolationException e) {
            throw new UsernameAlreadyExistsException(account.getUsername() + " already exists");
        }
    }
}
