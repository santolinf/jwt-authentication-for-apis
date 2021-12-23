package com.manning.liveproject.simplysend.auth.service;

import com.google.common.collect.Lists;
import com.manning.liveproject.simplysend.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@RequiredArgsConstructor
public class SimplySendUserDetailsService implements UserDetailsService {

    private final UserAccountRepository userAccountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userAccountRepository.findByUsername(username)
                .map(account -> {
                    SimpleGrantedAuthority authority = new SimpleGrantedAuthority(account.getUser().getRole().name());
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
