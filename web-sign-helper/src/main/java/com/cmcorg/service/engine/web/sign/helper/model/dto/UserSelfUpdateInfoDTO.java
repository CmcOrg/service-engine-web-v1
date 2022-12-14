package com.cmcorg.service.engine.web.sign.helper.model.dto;

import com.cmcorg.engine.web.model.model.constant.BaseRegexConstant;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Pattern;

@Data
public class UserSelfUpdateInfoDTO {

    @Schema(description = "头像uri")
    private String avatarUri;

    @Pattern(regexp = BaseRegexConstant.NICK_NAME_REGEXP)
    @Schema(description = "昵称")
    private String nickname;

    @Schema(description = "个人简介")
    private String bio;

}
