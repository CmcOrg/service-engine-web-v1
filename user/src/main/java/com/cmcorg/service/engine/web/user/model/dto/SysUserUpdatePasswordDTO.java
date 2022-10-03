package com.cmcorg.service.engine.web.user.model.dto;

import com.cmcorg.engine.web.model.model.dto.NotEmptyIdSet;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SysUserUpdatePasswordDTO extends NotEmptyIdSet {

    @Schema(description = "前端加密之后的，新密码")
    private String newPassword;

    @Schema(description = "前端加密之后的原始密码，新密码")
    private String newOrigPassword;

}
