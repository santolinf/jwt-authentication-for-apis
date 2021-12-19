package com.manning.liveproject.simplysend.api.controller;

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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(tags = { "users" })
@RestController
@RequestMapping("users")
@Validated
@RequiredArgsConstructor
public class UsersController {

    private final UserService userService;

    @Operation(summary = "create a User", tags = { "users" }/*, security = {
            @SecurityRequirement(name = "jwt"),
    }*/)
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
}
