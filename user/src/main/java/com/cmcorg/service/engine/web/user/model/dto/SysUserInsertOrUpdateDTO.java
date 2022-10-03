package com.cmcorg.service.engine.web.user.model.dto;

import com.cmcorg.engine.web.model.model.constant.BaseRegexConstant;
import com.cmcorg.engine.web.model.model.dto.BaseInsertOrUpdateDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
public class SysUserInsertOrUpdateDTO extends BaseInsertOrUpdateDTO {

    @Size(max = 20)
    @Pattern(regexp = BaseRegexConstant.SIGN_IN_NAME_REGEXP)
    @Schema(description = "登录名")
    private String signInName;

    @Size(max = 200)
    @Pattern(regexp = BaseRegexConstant.EMAIL)
    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "前端加密之后的密码")
    private String password;

    @Schema(description = "前端加密之后的原始密码")
    private String origPassword;

    @NotBlank
    @Pattern(regexp = BaseRegexConstant.NICK_NAME_REGEXP)
    @Schema(description = "昵称")
    private String nickname;

    @Schema(description = "个人简介")
    private String bio;

    @Schema(description = "头像uri")
    private String avatarUri;

    @Schema(description = "正常/冻结")
    private Boolean enableFlag;

    @Schema(description = "角色 idSet")
    private Set<Long> roleIdSet;

}