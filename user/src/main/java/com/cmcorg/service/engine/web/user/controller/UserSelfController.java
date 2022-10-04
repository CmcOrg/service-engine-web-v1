package com.cmcorg.service.engine.web.user.controller;

import com.cmcorg.engine.web.auth.model.vo.ApiResultVO;
import com.cmcorg.engine.web.model.generate.model.annotation.WebPage;
import com.cmcorg.engine.web.model.generate.model.enums.PageTypeEnum;
import com.cmcorg.service.engine.web.user.model.dto.UserSelfUpdateInfoDTO;
import com.cmcorg.service.engine.web.user.model.vo.UserSelfInfoVO;
import com.cmcorg.service.engine.web.user.service.UserSelfService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

@WebPage(type = PageTypeEnum.NONE)
@RestController
@RequestMapping(value = "/userSelf")
@Tag(name = "用户-自我-管理")
public class UserSelfController {

    @Resource
    UserSelfService baseService;

    @Operation(summary = "获取：当前用户，基本信息")
    @PostMapping(value = "/info")
    public ApiResultVO<UserSelfInfoVO> userSelfInfo() {
        return ApiResultVO.ok(baseService.userSelfInfo());
    }

    @Operation(summary = "当前用户：基本信息：修改")
    @PostMapping(value = "/updateInfo")
    public ApiResultVO<String> userSelfUpdateInfo(@RequestBody @Valid UserSelfUpdateInfoDTO dto) {
        return ApiResultVO.ok(baseService.userSelfUpdateInfo(dto));
    }

    @Operation(summary = "当前用户：刷新jwt私钥后缀")
    @PostMapping(value = "/refreshJwtSecretSuf")
    public ApiResultVO<String> userSelfRefreshJwtSecretSuf() {
        return ApiResultVO.ok(baseService.userSelfRefreshJwtSecretSuf());
    }

}
