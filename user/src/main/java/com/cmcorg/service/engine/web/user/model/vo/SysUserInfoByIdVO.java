package com.cmcorg.service.engine.web.user.model.vo;

import com.cmcorg.engine.web.auth.model.entity.SysUserDO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
public class SysUserInfoByIdVO extends SysUserDO {

    @Schema(description = "角色 idSet")
    private Set<Long> roleIdSet;

    @Schema(description = "昵称")
    private String nickname;

    @Schema(description = "个人简介")
    private String bio;

    @Schema(description = "头像uri")
    private String avatarUri;

}
