package com.cmcorg.service.engine.web.user.model.vo;

import com.cmcorg.engine.web.model.generate.model.annotation.RequestField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;
import java.util.Set;

@Data
public class SysUserPageVO {

    @Schema(description = "主键id")
    private Long id;

    @RequestField(formDeleteNameFlag = true)
    @Schema(description = "昵称")
    private String nickname;

    @Schema(description = "头像uri")
    private String avatarUri;

    @RequestField(formTitle = "邮箱")
    @Schema(description = "邮箱，备注：会脱敏")
    private String email;

    @RequestField(formTitle = "登录名")
    @Schema(description = "登录名，会脱敏")
    private String signInName;

    @RequestField(formTitle = "是否正常")
    @Schema(description = "正常/冻结")
    private Boolean enableFlag;

    @Schema(description = "是否有密码")
    private Boolean passwordFlag;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "修改时间")
    private Date updateTime;

    @RequestField(tableIgnoreFlag = true)
    @Schema(description = "角色 idSet")
    private Set<Long> roleIdSet;

}
