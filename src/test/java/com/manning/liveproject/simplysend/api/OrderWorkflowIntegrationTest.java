package com.manning.liveproject.simplysend.api;

import com.manning.liveproject.simplysend.BaseIntegrationTest;
import com.manning.liveproject.simplysend.api.dto.ItemDto;
import com.manning.liveproject.simplysend.api.dto.OrderApprovalDto;
import com.manning.liveproject.simplysend.api.dto.OrderDto;
import com.manning.liveproject.simplysend.api.enums.ItemType;
import com.manning.liveproject.simplysend.api.enums.Role;
import com.manning.liveproject.simplysend.entity.Item;
import com.manning.liveproject.simplysend.entity.Order;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.http.MediaType;

import static com.manning.liveproject.simplysend.auth.SecurityConstants.TOKEN_PREFIX;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OrderWorkflowIntegrationTest extends BaseIntegrationTest {

    @AfterAll
    public void tearDown() {
        cleanDb();
    }

    @Test
    public void givenAuthenticationFailure_whenRequestOrder_thenReturnUnauthorised() throws Exception {
        OrderDto orderPayload = OrderDto.builder()
                .reason("empty order")
                .build();

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderPayload)))
                .andDo(print()).andExpect(status().isUnauthorized());
    }

    @Test
    public void givenMissingOrIncorrectItemIds_whenRequestOrder_thenReturnBadRequest() throws Exception {
        String token = loginAs("zach@test.com");

        OrderDto orderPayload = OrderDto.builder()
                .reason("Need stationary")
                // missing item Ids list
                .build();

        mockMvc.perform(post("/orders")
                        .header("Authorization", TOKEN_PREFIX + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderPayload)))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.violations.items").value("must not be empty"));
    }

    @Test
    public void whenRequestOrder_thenCreateNewOrderDetails() throws Exception {
        Item laptop = Item.builder().name("laptop").type(ItemType.HARDWARE.getValue()).price(1_000).build();
        Long laptopId = itemRepository.save(laptop).getId();

        String token = loginAs("zach@test.com");
        assertNewRequestOrder(laptopId, "Need a laptop", token);
    }

    private void assertNewRequestOrder(Long itemId, String reason, String token) throws Exception {
        OrderDto orderPayload = OrderDto.builder()
                .reason(reason)
                .items(Lists.newArrayList(ItemDto.builder().id(itemId).build()))
                .build();

        mockMvc.perform(post("/orders")
                        .header("Authorization", TOKEN_PREFIX + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderPayload)))
                .andDo(print()).andExpect(status().isCreated());
    }

    @Test
    public void givenReporteeRole_whenListOrders_thenDenyRequest() throws Exception {
        String token = loginAs("zach@test.com");

        mockMvc.perform(get("/orders")
                        .header("Authorization", TOKEN_PREFIX + token))
                .andDo(print()).andExpect(status().isForbidden());
    }

    @Test
    public void givenManagerRole_whenListOrders_thenAllowRequest() throws Exception {
        String token = loginAs("jen@test.com", Role.MGR);

        mockMvc.perform(get("/orders")
                        .header("Authorization", TOKEN_PREFIX + token))
                .andDo(print()).andExpect(status().isOk());
    }

    @Test
    public void givenReporteeRole_whenRequestOrder_thenCannotApproveIt() throws Exception {
        Item liveProject = Item.builder().name("Manning liveProject").type(ItemType.TRAINING.getValue()).price(60).build();
        Long liveProjectId = itemRepository.save(liveProject).getId();

        String token = loginAs("zach@test.com");
        assertNewRequestOrder(liveProjectId, "Learning all about JWT Authentication and Authorisation", token);

        Long orderId = orderRepository.findAll().stream().findFirst().map(Order::getId).orElse(0L);

        mockMvc.perform(post("/orders/" + orderId + "/approve")
                        .header("Authorization", TOKEN_PREFIX + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(OrderApprovalDto.builder()
                                        .approve(true)
                                        .comment("this is mine")
                                        .build())
                        ))
                .andDo(print()).andExpect(status().isForbidden());
    }

    @Test
    public void givenManagerRole_whenApproveAnOrder_thenOrderIsApproved() throws Exception {
        Item pencils = Item.builder().name("pens and pencils").type(ItemType.STATIONARY.getValue()).price(30).build();
        Long pencilsId = itemRepository.save(pencils).getId();

        String token = loginAs("zach@test.com");
        assertNewRequestOrder(pencilsId, "Taking notes", token);

        token = loginAs("jen@test.com", Role.MGR);
        Long orderId = orderRepository.findAll().stream().findFirst().map(Order::getId).orElse(0L);

        mockMvc.perform(post("/orders/" + orderId + "/approve")
                        .header("Authorization", TOKEN_PREFIX + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(OrderApprovalDto.builder()
                                        .approve(true)
                                        .comment("approved")
                                        .build())
                        ))
                .andDo(print()).andExpect(status().isCreated());
    }
}
