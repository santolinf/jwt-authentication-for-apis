package com.manning.liveproject.simplysend.api.dto;

import com.manning.liveproject.simplysend.api.enums.ItemType;
import io.swagger.annotations.ApiModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ApiModel("item")
@Data
@EqualsAndHashCode
@ToString
public class ItemDto {

    @Schema(required = true)
    private long id;

    private ItemType type;

    @Schema(required = true)
    private String name;

    @Schema(required = true, description = "price of the item in any currency")
    private Integer price;

    @Schema(description = "Description of the Item")
    private String description;
}
