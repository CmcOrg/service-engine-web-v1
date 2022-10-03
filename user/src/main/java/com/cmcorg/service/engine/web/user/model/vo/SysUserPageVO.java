package com.cmcorg.service.engine.web.user.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;
import java.util.Set;

@Data
public class SysUserPageVO {

    @Schema(description = "主键id")
    private Long id;

    @Schema(description = "昵称")
    private String nickname;

    @Schema(description = "头像uri")
    private String avatarUri;

    @Schema(description = "邮箱，备注：会脱敏")
    private String email;

    @Schema(description = "登录名，会脱敏")
    private String signInName;

    @Schema(description = "正常/冻结")
    private Boolean enableFlag;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "修改时间")
    private Date updateTime;

    @Schema(description = "是否有密码")
    private Boolean passwordFlag;

    @Schema(description = "角色 idSet")
    private Set<Long> roleIdSet;

}
