package com.manning.liveproject.simplysend.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@ApiModel("approval")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class OrderApprovalDto {

    @Schema(required = true)
    @NotNull
    private Boolean approve;

    @Schema(required = true, description = "Approver Comments")
    @NotEmpty
    private String comment;
}
