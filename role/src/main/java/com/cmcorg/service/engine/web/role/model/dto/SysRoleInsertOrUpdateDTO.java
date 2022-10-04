package com.cmcorg.service.engine.web.role.model.dto;

import com.cmcorg.engine.web.model.generate.model.annotation.RequestField;
import com.cmcorg.engine.web.model.generate.model.enums.FormInputTypeEnum;
import com.cmcorg.engine.web.model.model.dto.BaseInsertOrUpdateDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
public class SysRoleInsertOrUpdateDTO extends BaseInsertOrUpdateDTO {

    @RequestField(formTitle = "角色名", formTooltip = "不能重复")
    @NotBlank
    @Schema(description = "角色名，不能重复")
    private String name;

    @RequestField(tableTitle = "关联菜单", formInputType = FormInputTypeEnum.TREE_SELECT, formSelectMultipleFlag = true, formSelectRequestStr = "SysMenuPage", formSelectOptionsOrRequestImportStr = "import {SysMenuPage} from \"@/api/admin/SysMenuController\";\n", formSelectRequestTreeFlag = true)
    @Schema(description = "菜单 idSet")
    private Set<Long> menuIdSet;

    @RequestField(tableTitle = "关联用户", formInputType = FormInputTypeEnum.SELECT, formSelectMultipleFlag = true, formSelectRequestStr = "SysUserDictList", formSelectOptionsOrRequestImportStr = "import {SysUserDictList} from \"@/api/admin/SysUserController\";\n")
    @Schema(description = "用户 idSet")
    private Set<Long> userIdSet;

    @RequestField(formTitle = "默认角色", formTooltip = "每个用户都拥有此角色权限，备注：只会有一个默认角色")
    @Schema(description = "是否是默认角色，备注：只会有一个默认角色")
    private Boolean defaultFlag;

    @Schema(description = "是否启用")
    private Boolean enableFlag;

    @Schema(description = "备注")
    private String remark;
}
