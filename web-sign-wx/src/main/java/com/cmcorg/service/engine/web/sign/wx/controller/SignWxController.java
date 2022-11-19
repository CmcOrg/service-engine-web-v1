package com.cmcorg.service.engine.web.sign.wx.controller;

import com.cmcorg.engine.web.auth.exception.BaseBizCodeEnum;
import com.cmcorg.engine.web.auth.model.vo.ApiResultVO;
import com.cmcorg.engine.web.model.generate.model.annotation.WebPage;
import com.cmcorg.engine.web.model.generate.model.enums.PageTypeEnum;
import com.cmcorg.service.engine.web.sign.wx.model.dto.SignInPhoneCodeDTO;
import com.cmcorg.service.engine.web.sign.wx.service.SignWxService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

@WebPage(type = PageTypeEnum.SIGN, title = "微信")
@RestController
@RequestMapping(value = "/sign/wx")
@Tag(name = "登录注册-微信")
public class SignWxController {

    @Resource
    SignWxService signWxService;

    @PostMapping(value = "/sign/in/phoneCode")
    @Operation(summary = "手机号 code登录")
    public ApiResultVO<String> signInPhoneCode(@RequestBody @Valid SignInPhoneCodeDTO dto) {
        return ApiResultVO.ok(BaseBizCodeEnum.OK, signWxService.signInPhoneCode(dto));
    }

}
