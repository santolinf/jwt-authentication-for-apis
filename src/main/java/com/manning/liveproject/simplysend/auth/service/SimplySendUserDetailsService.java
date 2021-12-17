package com.manning.liveproject.simplysend.auth.service;

import com.google.common.collect.Lists;
import com.manning.liveproject.simplysend.repository.UserAccountRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class SimplySendUserDetailsService implements UserDetailsService {

    private final UserAccountRepository userAccountRepository;

    public SimplySendUserDetailsService(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userAccountRepository.findByUsername(username)
                .map(account -> {
                    SimpleGrantedAuthority authority = new SimpleGrantedAuthority(account.getProfile().getRole().name());
                    return new User(
                            username,
                            account.getPassword(),
                            account.getEnabled(),
                            true,
                            true,
                            true,
                            Lists.newArrayList(authority)
                    );
                })
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }
}
