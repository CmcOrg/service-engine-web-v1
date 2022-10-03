package com.cmcorg.service.engine.web.user.model.vo;

import com.cmcorg.service.engine.web.user.model.dto.UserSelfUpdateBaseInfoDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserSelfBaseInfoVO extends UserSelfUpdateBaseInfoDTO {

    @Schema(description = "邮箱，会脱敏")
    private String email;

    @Schema(description = "是否有密码，用于前端显示，修改密码/设置密码")
    private Boolean passwordFlag;

    @Schema(description = "登录名，会脱敏")
    private String signInName;

}
