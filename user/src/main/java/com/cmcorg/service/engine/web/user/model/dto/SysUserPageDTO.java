package com.cmcorg.service.engine.web.user.model.dto;

import com.cmcorg.engine.web.auth.model.dto.MyPageDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SysUserPageDTO extends MyPageDTO {

    @Schema(description = "昵称")
    private String nickname;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "正常/冻结")
    private Boolean enableFlag;

    @Schema(description = "是否有密码")
    private Boolean passwordFlag;

}
