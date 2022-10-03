package com.cmcorg.service.engine.web.sign.email.model.dto;

import com.cmcorg.engine.web.model.model.constant.BaseRegexConstant;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@EqualsAndHashCode(callSuper = true)
@Data
public class SignEmailBindAccountDTO extends EmailNotBlankDTO {

    @Pattern(regexp = BaseRegexConstant.CODE_6_REGEXP)
    @NotBlank
    @Schema(description = "邮箱验证码")
    private String code;

}
