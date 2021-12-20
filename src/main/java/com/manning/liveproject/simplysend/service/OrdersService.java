package com.manning.liveproject.simplysend.service;

import com.manning.liveproject.simplysend.api.dto.OrderDto;
import com.manning.liveproject.simplysend.api.enums.OrderStatus;
import com.manning.liveproject.simplysend.entity.Order;
import com.manning.liveproject.simplysend.entity.UserAccount;
import com.manning.liveproject.simplysend.entity.UserProfile;
import com.manning.liveproject.simplysend.exceptions.InvalidIdentifierException;
import com.manning.liveproject.simplysend.mapper.OrderMapper;
import com.manning.liveproject.simplysend.repository.OrderRepository;
import com.manning.liveproject.simplysend.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import static java.util.Optional.ofNullable;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrdersService {

    private final OrderMapper mapper;
    private final OrderRepository orderRepository;
    private final UserAccountRepository userAccountRepository;

    public void requestOrder(OrderDto orderDto) {
        Order order = mapper.orderDtoToOrder(orderDto);

        try {
            order.setStatus(OrderStatus.REQUESTED.getValue());

            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            UserProfile owner = userAccountRepository.findByUsername(username).map(UserAccount::getProfile).orElse(null);
            order.setOwner(owner);

            order = orderRepository.save(order);
            log.debug(username + " requested order: {}", order);
        } catch (JpaObjectRetrievalFailureException e) {
            String message = ofNullable(e.getMostSpecificCause()).map(Throwable::getMessage).orElse(e.getMessage());
            log.debug("{}: {}", message, order);
            throw new InvalidIdentifierException(message);
        }
    }
}
