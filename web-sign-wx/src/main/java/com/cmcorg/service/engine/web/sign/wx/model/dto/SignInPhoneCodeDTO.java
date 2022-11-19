package com.cmcorg.service.engine.web.sign.wx.model.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class SignInPhoneCodeDTO {

    @NotBlank
    private String phoneCode;

}
