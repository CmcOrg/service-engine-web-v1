package com.cmcorg.service.engine.web.user.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class SysUserDictListDTO {

    @Schema(description = "是否追加 admin账号")
    private Boolean addAdminFlag;

}
