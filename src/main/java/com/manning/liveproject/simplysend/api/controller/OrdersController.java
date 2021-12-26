package com.manning.liveproject.simplysend.api.controller;

import com.manning.liveproject.simplysend.api.dto.OrderApprovalDto;
import com.manning.liveproject.simplysend.api.dto.OrderDto;
import com.manning.liveproject.simplysend.api.dto.PagedResponse;
import com.manning.liveproject.simplysend.api.enums.OrderStatus;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

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

    @Operation(summary = "List all orders", tags = { "orders" }, security = {
            @SecurityRequirement(name = "jwt")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "A paged array of orders"),
            @ApiResponse(responseCode = "401", description = "Authentication information is missing or invalid")
    })
    @PreAuthorize("hasAuthority(T(com.manning.liveproject.simplysend.api.enums.Role).MGR.name())")
    @GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public PagedResponse<OrderDto> listOrders(
            @Parameter(
                    in = ParameterIn.QUERY,
                    description = "status of the order REQUESTED,APPROVED,DENIED",
                    schema = @Schema(implementation = OrderStatus.class)
            )
            @RequestParam(value = "status", required = false) OrderStatus status,
            @Parameter(
                    in = ParameterIn.QUERY,
                    description = "if true returns orders of reportees by name for approval",
                    schema = @Schema(implementation = String.class, description = "retrieve reportee order requests by emailId")
            )
            @RequestParam(value = "reportee", required = false) String reportee,
            @Parameter(
                    in = ParameterIn.QUERY,
                    description = "How many items to return at one time (max 100)"
            )
            @Valid @Min(1) @Max(100)
            @RequestParam(value = "limit", required = false) Integer limit,
            @Parameter(
                    in = ParameterIn.QUERY,
                    description = "page number"
            )
            @RequestParam(value = "page", required = false) Integer page
    ) {
        return ordersService.findOrders(status, reportee, limit, page);
    }

    @Operation(summary = "Approve an order", tags = { "orders" }, security = {
            @SecurityRequirement(name = "jwt")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Order approved"),
            @ApiResponse(responseCode = "401", description = "Authentication information is missing or invalid")
    })
    @PreAuthorize("hasAuthority(T(com.manning.liveproject.simplysend.api.enums.Role).MGR.name())")
    @PostMapping(path = "{orderId}/approve", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void approveOrder(
            @Parameter(in = ParameterIn.PATH, description = "The id of the order to retrieve", required = true, schema = @Schema())
            @PathVariable("orderId") Long orderId,
            @Parameter(in = ParameterIn.DEFAULT, description = "Send the Approval Object", required = true, schema = @Schema())
            @Valid @RequestBody OrderApprovalDto approval
    ) {
        ordersService.approveOrder(orderId, approval);
    }
}
