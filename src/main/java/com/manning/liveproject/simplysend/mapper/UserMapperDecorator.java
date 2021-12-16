package com.manning.liveproject.simplysend.mapper;

import com.manning.liveproject.simplysend.api.dto.UserDto;
import com.manning.liveproject.simplysend.entity.UserAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;

public abstract class UserMapperDecorator implements UserMapper {

    @Autowired
    @Qualifier("delegate")
    private UserMapper delegate;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserAccount userDtoToUserAccount(UserDto user) {
        UserAccount account = delegate.userDtoToUserAccount(user);
        account.setPassword(passwordEncoder.encode(user.getPassword()));
        account.setEnabled(false);

        return account;
    }
}
