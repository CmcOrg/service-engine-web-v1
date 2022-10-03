package com.cmcorg.service.engine.web.param.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cmcorg.engine.web.auth.model.constant.WebModelConstant;
import com.cmcorg.engine.web.auth.model.entity.BaseEntity;
import com.cmcorg.engine.web.model.model.annotation.RequestClass;
import com.cmcorg.engine.web.model.model.annotation.RequestField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@RequestClass(tableIgnoreFields = WebModelConstant.TABLE_IGNORE_FIELDS_TWO)
@EqualsAndHashCode(callSuper = true)
@TableName(value = "sys_param")
@Data
@Schema(description = "系统参数主表")
public class SysParamDO extends BaseEntity {

    @RequestField(formTitle = "配置名")
    @Schema(description = "配置名，以 id为不变值进行使用，不要用此属性")
    private String name;

    @RequestField(hideInSearchFlag = true)
    @Schema(description = "值")
    private String value;

}
