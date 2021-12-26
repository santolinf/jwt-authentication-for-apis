package com.manning.liveproject.simplysend.api.controller;

import com.manning.liveproject.simplysend.api.dto.PagedResponse;
import com.manning.liveproject.simplysend.api.dto.UserDto;
import com.manning.liveproject.simplysend.service.UserService;
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

@Api(tags = { "users" })
@RestController
@RequestMapping("users")
@Validated
@RequiredArgsConstructor
public class UsersController {

    private final UserService userService;

    @Operation(summary = "create a User", tags = { "users" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Account created"),
            @ApiResponse(responseCode = "400", description = "Field is missing or invalid"),
            @ApiResponse(responseCode = "401", description = "Authentication information is missing or invalid"),
            @ApiResponse(responseCode = "409", description = "Account cannot be created"),
    })
    @PostMapping(path = "", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void createUser(
            @Parameter(in = ParameterIn.DEFAULT, description = "Send the User Object", required=true, schema=@Schema())
            @Valid @RequestBody UserDto user
    ) {
        userService.createUser(user);
    }

    @Operation(summary = "List all users", tags = { "users" }, security = {
            @SecurityRequirement(name = "jwt")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "A paged array of users"),
            @ApiResponse(responseCode = "401", description = "Authentication information is missing or invalid")
    })
    @PreAuthorize("hasAuthority(T(com.manning.liveproject.simplysend.api.enums.Role).ADMIN.name())")
    @GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public PagedResponse<UserDto> listUsers(
            @Parameter(
                    in = ParameterIn.QUERY,
                    description = "How many users to return at one time (max 100)"
            )
            @Valid @Min(1) @Max(100)
            @RequestParam(value = "limit", required = false) Integer limit,
            @Parameter(
                    in = ParameterIn.QUERY,
                    description = "page number"
            )
            @RequestParam(value = "page", required = false) Integer page
    ) {
        return userService.findUsers(limit, page);
    }
}
