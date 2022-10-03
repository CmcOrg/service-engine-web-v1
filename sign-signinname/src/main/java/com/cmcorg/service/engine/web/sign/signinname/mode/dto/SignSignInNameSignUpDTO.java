package com.cmcorg.service.engine.web.sign.signinname.mode.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

@EqualsAndHashCode(callSuper = true)
@Data
public class SignSignInNameSignUpDTO extends SignInNameNotBlankDTO {

    @NotBlank
    @Schema(description = "前端加密之后的密码")
    private String password;

    @NotBlank
    @Schema(description = "前端加密之后的原始密码")
    private String origPassword;

}
