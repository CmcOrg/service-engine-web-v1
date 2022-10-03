package com.cmcorg.service.engine.web.user.controller;

import com.cmcorg.engine.web.auth.model.vo.ApiResultVO;
import com.cmcorg.service.engine.web.user.model.dto.UserSelfUpdateBaseInfoDTO;
import com.cmcorg.service.engine.web.user.model.vo.UserSelfBaseInfoVO;
import com.cmcorg.service.engine.web.user.service.UserSelfService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping(value = "/userSelf")
@Tag(name = "用户-自我-管理")
public class UserSelfController {

    @Resource
    UserSelfService baseService;

    @Operation(summary = "获取：当前用户，基本信息")
    @PostMapping(value = "/baseInfo")
    public ApiResultVO<UserSelfBaseInfoVO> userSelfBaseInfo() {
        return ApiResultVO.ok(baseService.userSelfBaseInfo());
    }

    @Operation(summary = "当前用户：基本信息：修改")
    @PostMapping(value = "/updateBaseInfo")
    public ApiResultVO<String> userSelfUpdateBaseInfo(@RequestBody @Valid UserSelfUpdateBaseInfoDTO dto) {
        return ApiResultVO.ok(baseService.userSelfUpdateBaseInfo(dto));
    }

    @Operation(summary = "当前用户：刷新jwt私钥后缀")
    @PostMapping(value = "/refreshJwtSecretSuf")
    public ApiResultVO<String> userSelfRefreshJwtSecretSuf() {
        return ApiResultVO.ok(baseService.userSelfRefreshJwtSecretSuf());
    }

}