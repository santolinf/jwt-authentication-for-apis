package com.manning.liveproject.simplysend.repository;

import com.manning.liveproject.simplysend.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
