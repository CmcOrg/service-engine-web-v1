package com.cmcorg.service.engine.web.dict.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 字典类型
 */
@AllArgsConstructor
@Getter
public enum SysDictTypeEnum {
    DICT((byte)1, "字典"), //
    DICT_ITEM((byte)2, "字典项"), //
    ;

    @EnumValue
    @JsonValue
    private byte code;
    private String codeDescription; // code 说明

}
