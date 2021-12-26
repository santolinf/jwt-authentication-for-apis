package com.manning.liveproject.simplysend.repository;

import com.manning.liveproject.simplysend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User getUserByEmail(String email);
}
