package com.cmcorg.service.engine.web.sign.signinname.mode.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class SignSignInNameSignDeleteDTO {

    @NotBlank
    @Schema(description = "前端加密之后的密码")
    private String currentPassword;

}
