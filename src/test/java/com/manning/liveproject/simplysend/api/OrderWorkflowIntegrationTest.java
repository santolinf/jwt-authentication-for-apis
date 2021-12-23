package com.manning.liveproject.simplysend.api;

import com.manning.liveproject.simplysend.BaseIntegrationTest;
import com.manning.liveproject.simplysend.api.dto.ItemDto;
import com.manning.liveproject.simplysend.api.dto.OrderDto;
import com.manning.liveproject.simplysend.api.enums.ItemType;
import com.manning.liveproject.simplysend.entity.Item;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.http.MediaType;

import static com.manning.liveproject.simplysend.auth.SecurityConstants.TOKEN_PREFIX;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

        OrderDto orderPayload = OrderDto.builder()
                        .reason("Need a laptop")
                        .items(Lists.newArrayList(ItemDto.builder().id(laptopId).build()))
                        .build();

        mockMvc.perform(post("/orders")
                        .header("Authorization", TOKEN_PREFIX + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderPayload)))
                .andDo(print()).andExpect(status().isCreated());
    }
}
