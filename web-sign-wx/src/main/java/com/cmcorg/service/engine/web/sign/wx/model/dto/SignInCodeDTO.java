package com.cmcorg.service.engine.web.sign.wx.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class SignInCodeDTO {

    @NotBlank
    @Schema(description = "微信 code")
    private String code;

}
