package com.manning.liveproject.simplysend.repository;

import com.manning.liveproject.simplysend.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Page<Order> findOrdersByStatusIsLike(String status, Pageable pageable);
    Page<Order> findOrdersByStatusIsLikeAndOwnerId(String status, Long reportee, Pageable pageable);
}
