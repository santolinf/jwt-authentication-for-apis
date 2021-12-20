package com.manning.liveproject.simplysend.api.dto;

import com.manning.liveproject.simplysend.api.enums.OrderStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@ApiModel("order")
@Data
@EqualsAndHashCode
@ToString
public class OrderDto {

    @Schema(required = true)
    private long id;

    private OrderStatus status;

    @Schema(required = true)
    @NotNull
    private String reason;

    @Schema(description = "Approver Comments")
    private String comment;

    @NotEmpty
    private List<ItemDto> items;
}
