package com.manning.liveproject.simplysend.auth.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Service
public class InMemorySessionService {

    private final Set<String> loggedOnUsers = new HashSet<>();

    public void add(String username) {
        loggedOnUsers.add(username);
        log.debug("User Session created for [{}]", username);
    }

    public boolean exists(String username) {
        return loggedOnUsers.contains(username);
    }

    public void remove(String username) {
        loggedOnUsers.remove(username);
        log.debug("User Session removed for [{}]", username);
    }
}
