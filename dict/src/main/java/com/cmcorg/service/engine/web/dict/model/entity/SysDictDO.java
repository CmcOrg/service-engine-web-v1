package com.cmcorg.service.engine.web.dict.model.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cmcorg.engine.web.auth.model.constant.WebModelConstant;
import com.cmcorg.engine.web.auth.model.entity.BaseEntity;
import com.cmcorg.engine.web.model.model.annotation.RequestClass;
import com.cmcorg.engine.web.model.model.annotation.RequestField;
import com.cmcorg.service.engine.web.dict.model.enums.SysDictTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@RequestClass(tableIgnoreFields = WebModelConstant.TABLE_IGNORE_FIELDS_TWO)
@EqualsAndHashCode(callSuper = true)
@TableName(value = "sys_dict")
@Data
@Schema(description = "字典主表")
public class SysDictDO extends BaseEntity {

    @RequestField(formTitle = "key")
    @Schema(description = "字典 key（不能重复），字典项要冗余这个 key，目的：方便操作")
    private String dictKey;

    @RequestField(formTitle = "名称")
    @Schema(description = "字典/字典项 名")
    private String name;

    @RequestField(formTitle = "字典类型", tableIgnoreFlag = true)
    @Schema(description = "字典类型：1 字典 2 字典项")
    private SysDictTypeEnum type;

    @RequestField(formTitle = "value")
    @Schema(description = "字典项 value（数字 123...）备注：字典为 -1")
    private Byte value;

    @RequestField(formTitle = "排序号", hideInSearchFlag = true)
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "排序号（值越大越前面，默认为 0）")
    private Integer orderNo;

}
