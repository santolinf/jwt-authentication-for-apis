package com.manning.liveproject.simplysend.api.controller;

import com.manning.liveproject.simplysend.api.dto.ItemDto;
import com.manning.liveproject.simplysend.api.enums.ItemType;
import com.manning.liveproject.simplysend.service.ItemsService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.headers.Header;
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
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

@Api(tags = { "items" })
@RestController
@RequestMapping("items")
@Validated
@RequiredArgsConstructor
public class ItemsController {

    private final ItemsService itemsService;

    @Operation(summary = "List all items available for purchase", tags = { "items" }, security = {
            @SecurityRequirement(name = "jwt")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "A paged array of items",
                    headers = { @Header(name = "x-next", description = "A link to the next page of responses") }
            ),
            @ApiResponse(responseCode = "401", description = "Authentication information is missing or invalid")
    })
    @GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<ItemDto> listItems(
            @Parameter(
                    in = ParameterIn.QUERY,
                    description = "type of the item",
                    schema = @Schema(implementation = ItemType.class)
            )
            @RequestParam(value = "type", required = false) ItemType type,
            @Parameter(
                    in = ParameterIn.QUERY,
                    description = "How many items to return at one time (max 100)"
            )
            @Valid @Min(1) @Max(100)
            @RequestParam(value = "limit", required = false) Integer limit
    ) {
        return itemsService.findItems(type, limit);
    }
}
