package com.cmcorg.service.engine.web.dict.model.dto;

import com.cmcorg.engine.web.model.model.annotation.RequestField;
import com.cmcorg.engine.web.model.model.dto.BaseInsertOrUpdateDTO;
import com.cmcorg.service.engine.web.dict.model.enums.SysDictTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@Data
public class SysDictInsertOrUpdateDTO extends BaseInsertOrUpdateDTO {

    @RequestField(formTitle = "key")
    @NotBlank
    @Schema(description = "字典 key（不能重复），字典项要冗余这个 key，目的：方便操作")
    private String dictKey;

    @RequestField(formTitle = "名称")
    @NotBlank
    @Schema(description = "字典/字典项 名")
    private String name;

    @RequestField(formTitle = "字典类型", formTooltip = "1 字典 2 字典项")
    @NotNull
    @Schema(description = "字典类型：1 字典 2 字典项")
    private SysDictTypeEnum type;

    @RequestField(formTitle = "value", formTooltip = "数字 1 2 3 ...")
    @Schema(description = "字典项 value（数字 123...）备注：字典为 -1")
    private Byte value;

    @RequestField(formTitle = "排序号")
    @Schema(description = "排序号（值越大越前面，默认为 0）")
    private Integer orderNo;

    @Schema(description = "是否启用")
    private Boolean enableFlag;

    @Schema(description = "备注")
    private String remark;

}
