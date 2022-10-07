package com.cmcorg.service.engine.web.user.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cmcorg.engine.web.auth.model.vo.ApiResultVO;
import com.cmcorg.engine.web.model.generate.model.annotation.WebPage;
import com.cmcorg.engine.web.model.generate.model.enums.PageTypeEnum;
import com.cmcorg.engine.web.model.model.dto.NotEmptyIdSet;
import com.cmcorg.engine.web.model.model.dto.NotNullId;
import com.cmcorg.engine.web.model.model.vo.DictResultVO;
import com.cmcorg.service.engine.web.user.model.dto.SysUserDictListDTO;
import com.cmcorg.service.engine.web.user.model.dto.SysUserInsertOrUpdateDTO;
import com.cmcorg.service.engine.web.user.model.dto.SysUserPageDTO;
import com.cmcorg.service.engine.web.user.model.dto.SysUserUpdatePasswordDTO;
import com.cmcorg.service.engine.web.user.model.vo.SysUserInfoByIdVO;
import com.cmcorg.service.engine.web.user.model.vo.SysUserPageVO;
import com.cmcorg.service.engine.web.user.service.SysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

@WebPage(type = PageTypeEnum.ADMIN, title = "用户管理")
@RestController
@RequestMapping(value = "/sys/user")
@Tag(name = "用户-管理")
public class SysUserController {

    @Resource
    SysUserService baseService;

    @Operation(summary = "分页排序查询")
    @PostMapping("/page")
    @PreAuthorize("hasAuthority('sysUser:page')")
    public ApiResultVO<Page<SysUserPageVO>> myPage(@RequestBody @Valid SysUserPageDTO dto) {
        return ApiResultVO.ok(baseService.myPage(dto));
    }

    @Operation(summary = "下拉列表")
    @PostMapping("/dictList")
    @PreAuthorize("hasAuthority('sysUser:page')")
    public ApiResultVO<Page<DictResultVO>> dictList(@RequestBody @Valid SysUserDictListDTO dto) {
        return ApiResultVO.ok(baseService.dictList(dto));
    }

    @Operation(summary = "新增/修改")
    @PostMapping("/insertOrUpdate")
    @PreAuthorize("hasAuthority('sysUser:insertOrUpdate')")
    public ApiResultVO<String> insertOrUpdate(@RequestBody @Valid SysUserInsertOrUpdateDTO dto) {
        return ApiResultVO.ok(baseService.insertOrUpdate(dto));
    }

    @Operation(summary = "批量：注销用户")
    @PostMapping("/deleteByIdSet")
    @PreAuthorize("hasAuthority('sysUser:deleteByIdSet')")
    public ApiResultVO<String> deleteByIdSet(@RequestBody @Valid NotEmptyIdSet notEmptyIdSet) {
        return ApiResultVO.ok(baseService.deleteByIdSet(notEmptyIdSet));
    }

    @Operation(summary = "通过主键id，查看详情")
    @PostMapping("/infoById")
    @PreAuthorize("hasAuthority('sysUser:infoById')")
    public ApiResultVO<SysUserInfoByIdVO> infoById(@RequestBody @Valid NotNullId notNullId) {
        return ApiResultVO.ok(baseService.infoById(notNullId));
    }

    @Operation(summary = "批量：刷新用户 jwt私钥后缀")
    @PostMapping(value = "/refreshJwtSecretSuf")
    @PreAuthorize("hasAuthority('sysUser:insertOrUpdate')")
    public ApiResultVO<String> refreshJwtSecretSuf(@RequestBody @Valid NotEmptyIdSet notEmptyIdSet) {
        return ApiResultVO.ok(baseService.refreshJwtSecretSuf(notEmptyIdSet, null));
    }

    @Operation(summary = "批量：重置头像")
    @PostMapping("/resetAvatar")
    @PreAuthorize("hasAuthority('sysUser:insertOrUpdate')")
    public ApiResultVO<String> resetAvatar(@RequestBody @Valid NotEmptyIdSet notEmptyIdSet) {
        return ApiResultVO.ok(baseService.resetAvatar(notEmptyIdSet));
    }

    @Operation(summary = "批量：修改密码")
    @PostMapping("/updatePassword")
    @PreAuthorize("hasAuthority('sysUser:insertOrUpdate')")
    public ApiResultVO<String> updatePassword(@RequestBody @Valid SysUserUpdatePasswordDTO dto) {
        return ApiResultVO.ok(baseService.updatePassword(dto));
    }

}
