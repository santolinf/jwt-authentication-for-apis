package com.manning.liveproject.simplysend.api.dto;

import com.manning.liveproject.simplysend.api.enums.Role;
import io.swagger.annotations.ApiModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@ApiModel("user")
@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserDto {

    private Long id;

    private String firstName;

    private String lastName;

    @Schema(required = true)
    @NotNull
    @Email(regexp = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$")
    private String emailId;

    private Integer age;

    @Schema(required = true)
    @NotNull
    private String phone;

    @Schema(description = "Manager or Reportee role")
    private Role role;

    @NotNull
    @Size(min = 8, max = 16, message = "password should contain a minimum of 8 characters and a maximum of 16 characters")
    @Pattern(regexp = "^(?=.*?\\p{Lu})(?=.*?\\p{Ll})(?=.*?\\d)(?=.*?[`~!@#$%^&*()\\-_=+\\\\|\\[{\\]};:'\",<.>/?]).*$",
            message = "password should contain, at minimum, one number, one capital letter, and one special character")
    private String password;

    @Schema(description = "Id of the Manager to whom current User reports to")
    private Long managerId;

    private String address;

    private String tag;
}
