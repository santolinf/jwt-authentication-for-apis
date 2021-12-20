package com.manning.liveproject.simplysend.api.controller;

import com.manning.liveproject.simplysend.api.dto.OrderDto;
import com.manning.liveproject.simplysend.service.OrdersService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(tags = { "orders" })
@RestController
@RequestMapping("orders")
@Validated
@RequiredArgsConstructor
public class OrdersController {

    private final OrdersService ordersService;

    @Operation(summary = "Request an order", tags = { "orders" }, security = {
            @SecurityRequirement(name = "jwt")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Order requested"),
            @ApiResponse(responseCode = "401", description = "Authentication information is missing or invalid")
    })
    @PostMapping(path = "", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void requestOrder(
            @Parameter(in = ParameterIn.DEFAULT, description = "Send the Order Object", required = true, schema = @Schema())
            @Valid @RequestBody OrderDto order
    ) {
        ordersService.requestOrder(order);
    }
}
