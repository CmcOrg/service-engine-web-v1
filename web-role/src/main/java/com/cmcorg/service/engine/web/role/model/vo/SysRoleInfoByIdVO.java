package com.cmcorg.service.engine.web.role.model.vo;

import com.cmcorg.engine.web.auth.model.entity.SysRoleDO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SysRoleInfoByIdVO extends SysRoleDO {

    @Schema(description = "用户 idSet")
    private java.util.Set<Long> userIdSet;

    @Schema(description = "菜单 idSet")
    private java.util.Set<Long> menuIdSet;
}
