package com.manning.liveproject.simplysend.service;

import com.manning.liveproject.simplysend.api.dto.UserDto;
import com.manning.liveproject.simplysend.entity.User;
import com.manning.liveproject.simplysend.entity.UserAccount;
import com.manning.liveproject.simplysend.exceptions.UsernameAlreadyExistsException;
import com.manning.liveproject.simplysend.mapper.UserMapper;
import com.manning.liveproject.simplysend.repository.UserAccountRepository;
import com.manning.liveproject.simplysend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import static java.util.Optional.ofNullable;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper mapper;
    private final UserAccountRepository userAccountRepository;
    private final UserRepository userRepository;

    public void createUser(UserDto userDto) {
        UserAccount account = mapper.userDtoToUserAccount(userDto);
        account.setEnabled(true);

        User user = mapper.userDtoToUser(userDto);
        account.setUser(user);

        user.setManager(findManager(userDto.getManagerId()));

        saveUser(account);
        log.info("Created account: {}", account);
    }

    private User findManager(Long managerId) {
        return ofNullable(managerId).flatMap(userRepository::findById).orElse(null);
    }

    private void saveUser(UserAccount account) {
        try {
            userAccountRepository.save(account);
        } catch (DataIntegrityViolationException e) {
            throw new UsernameAlreadyExistsException(account.getUsername() + " already exists");
        }
    }
}
