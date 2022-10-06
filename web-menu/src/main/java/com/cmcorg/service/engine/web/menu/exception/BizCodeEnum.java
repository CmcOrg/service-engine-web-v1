package com.cmcorg.service.engine.web.menu.exception;

import com.cmcorg.engine.web.model.exception.IBizCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum BizCodeEnum implements IBizCode {

    MENU_URI_IS_EXIST(300011, "操作失败：path 重复"), //

    ;

    private int code;
    private String msg;
}
