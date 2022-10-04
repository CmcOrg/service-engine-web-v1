package com.cmcorg.service.engine.web.menu.model.dto;

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
public class SysMenuInsertOrUpdateDTO extends BaseInsertOrUpdateDTO {

    @RequestField(formTitle = "上级菜单", formInputType = FormInputTypeEnum.TREE_SELECT, formSelectRequestStr = "SysMenuPage", formSelectOptionsOrRequestImportStr = "import {SysMenuPage} from \"@/api/admin/SysMenuController\";\n", formSelectRequestTreeFlag = true)
    @Schema(description = "父节点id（顶级则为0）")
    private Long parentId;

    @NotBlank
    @Schema(description = "菜单名")
    private String name;

    @RequestField(formTitle = "路径", formTooltip = "相同父菜单下，子菜单路径不能重复")
    @Schema(description = "页面的 path，备注：相同父菜单下，子菜单 path不能重复")
    private String path;

    @RequestField(formInputType = FormInputTypeEnum.SELECT, formSelectOptionsStr = "RouterMapKeyList", formSelectOptionsOrRequestImportStr = "import {RouterMapKeyList} from \"@/router/RouterMap\";\n")
    @Schema(description = "路由")
    private String router;

    @Schema(description = "图标")
    private String icon;

    @RequestField(formTitle = "权限", formTooltip = "示例：menu:insertOrUpdate,menu:page,menu:deleteByIdSet,menu:infoById")
    @Schema(description = "权限，多个可用逗号拼接，例如：menu:insertOrUpdate,menu:page,menu:deleteByIdSet,menu:infoById")
    private String auths;

    @RequestField(formTitle = "权限菜单", formTooltip = "不显示，只代表菜单权限")
    @Schema(description = "是否是权限菜单，权限菜单：不显示，只代表菜单权限")
    private Boolean authFlag;

    @RequestField(formTitle = "关联角色", formInputType = FormInputTypeEnum.SELECT, formSelectMultipleFlag = true, formSelectRequestStr = "SysRolePage", formSelectOptionsOrRequestImportStr = "import {SysRolePage} from \"@/api/admin/SysRoleController\";\n")
    @Schema(description = "角色 idSet")
    private Set<Long> roleIdSet;

    @Schema(description = "是否启用")
    private Boolean enableFlag;

    @RequestField(formTitle = "起始页面", formTooltip = "是否为默认打开的页面")
    @Schema(description = "是否是起始页面，备注：只能存在一个 firstFlag === true 的菜单")
    private Boolean firstFlag;

    @RequestField(formTitle = "排序号", formTooltip = "值越大越前面")
    @Schema(description = "排序号（值越大越前面，默认为 0）")
    private Integer orderNo;

    @RequestField(formTitle = "是否显示", formTooltip = "是否在左侧菜单栏显示")
    @Schema(description = "是否显示在 左侧的菜单栏里面，如果为 false，也可以通过 $router.push()访问到")
    private Boolean showFlag;

    @Schema(description = "备注")
    private String remark;

    @RequestField(tableTitle = "重定向", formTooltip = "优先级最高")
    @Schema(description = "重定向，优先级最高")
    private String redirect;

}
