package com.manning.liveproject.simplysend.service;

import com.manning.liveproject.simplysend.api.dto.OrderApprovalDto;
import com.manning.liveproject.simplysend.api.dto.OrderDto;
import com.manning.liveproject.simplysend.api.dto.PagedResponse;
import com.manning.liveproject.simplysend.api.enums.OrderStatus;
import com.manning.liveproject.simplysend.auth.util.SecurityHelper;
import com.manning.liveproject.simplysend.entity.Order;
import com.manning.liveproject.simplysend.entity.UserAccount;
import com.manning.liveproject.simplysend.entity.User;
import com.manning.liveproject.simplysend.exceptions.InvalidIdentifierException;
import com.manning.liveproject.simplysend.exceptions.OrderApprovalException;
import com.manning.liveproject.simplysend.mapper.OrderMapper;
import com.manning.liveproject.simplysend.mapper.PagedOrdersResponseMapper;
import com.manning.liveproject.simplysend.repository.OrderRepository;
import com.manning.liveproject.simplysend.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Objects;

import static java.util.Optional.ofNullable;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class OrdersService {

    private final PagedOrdersResponseMapper pagedResponseMapper;
    private final OrderMapper orderMapper;
    private final OrderRepository orderRepository;
    private final UserAccountRepository userAccountRepository;

    public void requestOrder(OrderDto orderDto) {
        Order order = orderMapper.orderDtoToOrder(orderDto);

        try {
            order.setStatus(OrderStatus.REQUESTED.getValue());

            String username = SecurityHelper.getAuthenticatedUsername();
            User owner = userAccountRepository.findByUsername(username).map(UserAccount::getUser).orElse(null);
            order.setOwner(owner);

            order = orderRepository.save(order);
            log.debug(username + " requested order: {}", order);
        } catch (JpaObjectRetrievalFailureException e) {
            String message = ofNullable(e.getMostSpecificCause()).map(Throwable::getMessage).orElse(e.getMessage());
            log.debug("{}: {}", message, order);
            throw new InvalidIdentifierException(message);
        }
    }

    public PagedResponse<OrderDto> findOrders(OrderStatus status, String reportee, Integer limit, Integer page) {
        log.debug("Find orders: status=[{}], reportee=[{}], limit=[{}], page=[{}]", status, reportee, limit, page);

        String statusAsText = ofNullable(status).map(OrderStatus::getValue).orElse("%");
        Pageable pageRequest = PageRequest.of(ofNullable(page).orElse(0), ofNullable(limit).orElse(100));

        // find reportee
        Page<Order> orders;
        User owner;
        if (Strings.isNotEmpty(reportee)
                && Objects.nonNull(owner = userAccountRepository.findByUsername(reportee).map(UserAccount::getUser).orElse(null))
        ) {
            orders = orderRepository.findOrdersByStatusIsLikeAndOwnerId(statusAsText, owner.getId(), pageRequest);
        } else {
            orders = orderRepository.findOrdersByStatusIsLike(statusAsText, pageRequest);
        }

        int totalPages = orders.getTotalPages();
        int pageNumber = totalPages == 0 ? orders.getNumber() : orders.getNumber() + 1;
        log.debug("Loaded {} of {} items (page {} of {})", orders.getNumberOfElements(), orders.getTotalElements(), pageNumber, totalPages);

        return pagedResponseMapper.pageToPagedResponse(orders, orderMapper);
    }

    public void approveOrder(Long orderId, OrderApprovalDto approval) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new InvalidIdentifierException("Order ID does not exits: " + orderId));

        if (Objects.isNull(order.getOwner()) || SecurityHelper.isAuthenticatedUsername(order.getOwner().getEmail())) {
            throw new OrderApprovalException("Cannot approve order");
        }

        order.setStatus(Boolean.TRUE.equals(approval.getApprove()) ? OrderStatus.APPROVED.getValue() : OrderStatus.DENIED.getValue());
        order.setComment(approval.getComment());
        orderRepository.save(order);
    }
}
