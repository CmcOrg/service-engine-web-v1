package com.cmcorg.service.engine.web.sign.email.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

@EqualsAndHashCode(callSuper = true)
@Data
public class SignEmailSignInPasswordDTO extends EmailNotBlankDTO {

    @NotBlank
    @Schema(description = "密码")
    private String password;

}
