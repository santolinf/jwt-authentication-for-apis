package com.manning.liveproject.simplysend.service;

import com.manning.liveproject.simplysend.api.dto.PagedResponse;
import com.manning.liveproject.simplysend.api.dto.UserDto;
import com.manning.liveproject.simplysend.entity.User;
import com.manning.liveproject.simplysend.entity.UserAccount;
import com.manning.liveproject.simplysend.exceptions.UsernameAlreadyExistsException;
import com.manning.liveproject.simplysend.mapper.PagedUsersResponseMapper;
import com.manning.liveproject.simplysend.mapper.UserMapper;
import com.manning.liveproject.simplysend.repository.UserAccountRepository;
import com.manning.liveproject.simplysend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import static java.util.Optional.ofNullable;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserAccountRepository userAccountRepository;
    private final UserRepository userRepository;
    private final PagedUsersResponseMapper pagedResponseMapper;
    private final UserMapper mapper;

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

    public PagedResponse<UserDto> findUsers(Integer limit, Integer page) {
        log.debug("Find users: limit=[{}], page=[{}]", limit, page);

        Pageable pageRequest = PageRequest.of(ofNullable(page).orElse(0), ofNullable(limit).orElse(100));

        Page<User> users = userRepository.findAll(pageRequest);
        int totalPages = users.getTotalPages();
        int pageNumber = totalPages == 0 ? users.getNumber() : users.getNumber() + 1;
        log.debug("Loaded {} of {} users (page {} of {})", users.getNumberOfElements(), users.getTotalElements(), pageNumber, totalPages);

        return pagedResponseMapper.pageToPagedResponse(users, mapper);
    }
}
