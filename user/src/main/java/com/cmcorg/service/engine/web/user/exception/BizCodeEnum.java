package com.cmcorg.service.engine.web.user.exception;

import com.cmcorg.engine.web.model.exception.IBizCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum BizCodeEnum implements IBizCode {

    ACCOUNT_CANNOT_BE_EMPTY(300011, "操作失败：邮箱/登录名 不能都为空"), //

    ;

    private int code;
    private String msg;
}