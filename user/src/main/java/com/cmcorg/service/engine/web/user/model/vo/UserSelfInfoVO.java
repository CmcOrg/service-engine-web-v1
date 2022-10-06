package com.cmcorg.service.engine.web.user.model.vo;

import com.cmcorg.service.engine.web.sign.helper.model.dto.UserSelfUpdateInfoDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserSelfInfoVO extends UserSelfUpdateInfoDTO {

    @Schema(description = "邮箱，会脱敏")
    private String email;

    @Schema(description = "是否有密码，用于前端显示，修改密码/设置密码")
    private Boolean passwordFlag;

    @Schema(description = "登录名，会脱敏")
    private String signInName;

    @Schema(description = "账号注册时间")
    private Date createTime;

}
