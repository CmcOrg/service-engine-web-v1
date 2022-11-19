package com.cmcorg.service.engine.web.sign.phone.dto;

import com.cmcorg.engine.web.model.model.constant.BaseRegexConstant;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@EqualsAndHashCode(callSuper = true)
@Data
public class SignPhoneSignUpDTO extends PhoneNotBlankDTO {

    @Pattern(regexp = BaseRegexConstant.CODE_6_REGEXP)
    @NotBlank
    @Schema(description = "手机验证码")
    private String code;

    @NotBlank
    @Schema(description = "前端加密之后的密码")
    private String password;

    @NotBlank
    @Schema(description = "前端加密之后的原始密码")
    private String origPassword;

}
