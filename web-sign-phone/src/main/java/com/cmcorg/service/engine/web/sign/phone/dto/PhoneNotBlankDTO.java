package com.cmcorg.service.engine.web.sign.phone.dto;

import com.cmcorg.engine.web.model.model.constant.BaseRegexConstant;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class PhoneNotBlankDTO {

    @Size(max = 100)
    @NotBlank
    @Pattern(regexp = BaseRegexConstant.PHONE)
    @Schema(description = "手机号码")
    private String phone;

}
