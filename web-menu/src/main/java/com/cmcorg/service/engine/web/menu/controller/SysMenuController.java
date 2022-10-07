package com.cmcorg.service.engine.web.menu.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cmcorg.engine.web.auth.model.entity.SysMenuDO;
import com.cmcorg.engine.web.auth.model.vo.ApiResultVO;
import com.cmcorg.engine.web.model.generate.model.annotation.WebPage;
import com.cmcorg.engine.web.model.generate.model.enums.PageTypeEnum;
import com.cmcorg.engine.web.model.model.dto.AddOrderNoDTO;
import com.cmcorg.engine.web.model.model.dto.NotEmptyIdSet;
import com.cmcorg.engine.web.model.model.dto.NotNullId;
import com.cmcorg.service.engine.web.menu.model.dto.SysMenuInsertOrUpdateDTO;
import com.cmcorg.service.engine.web.menu.model.dto.SysMenuPageDTO;
import com.cmcorg.service.engine.web.menu.model.vo.SysMenuInfoByIdVO;
import com.cmcorg.service.engine.web.menu.service.SysMenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@WebPage(type = PageTypeEnum.ADMIN, title = "菜单管理")
@RestController
@RequestMapping(value = "/sys/menu")
@Tag(name = "菜单-管理")
public class SysMenuController {

    @Resource
    SysMenuService baseService;

    @Operation(summary = "新增/修改")
    @PostMapping("/insertOrUpdate")
    @PreAuthorize("hasAuthority('sysMenu:insertOrUpdate')")
    public ApiResultVO<String> insertOrUpdate(@RequestBody @Valid SysMenuInsertOrUpdateDTO dto) {
        return ApiResultVO.ok(baseService.insertOrUpdate(dto));
    }

    @Operation(summary = "分页排序查询")
    @PostMapping("/page")
    @PreAuthorize("hasAuthority('sysMenu:page')")
    public ApiResultVO<Page<SysMenuDO>> myPage(@RequestBody @Valid SysMenuPageDTO dto) {
        return ApiResultVO.ok(baseService.myPage(dto));
    }

    @Operation(summary = "查询：树结构")
    @PostMapping("/tree")
    @PreAuthorize("hasAuthority('sysMenu:page')")
    public ApiResultVO<List<SysMenuDO>> tree(@RequestBody @Valid SysMenuPageDTO dto) {
        return ApiResultVO.ok(baseService.tree(dto));
    }

    @Operation(summary = "批量删除")
    @PostMapping("/deleteByIdSet")
    @PreAuthorize("hasAuthority('sysMenu:deleteByIdSet')")
    public ApiResultVO<String> deleteByIdSet(@RequestBody @Valid NotEmptyIdSet notEmptyIdSet) {
        return ApiResultVO.ok(baseService.deleteByIdSet(notEmptyIdSet));
    }

    @Operation(summary = "通过主键id，查看详情")
    @PostMapping("/infoById")
    @PreAuthorize("hasAuthority('sysMenu:infoById')")
    public ApiResultVO<SysMenuInfoByIdVO> infoById(@RequestBody @Valid NotNullId notNullId) {
        return ApiResultVO.ok(baseService.infoById(notNullId));
    }

    @PostMapping("/userSelfMenuList")
    @Operation(summary = "获取：当前用户绑定的菜单")
    public ApiResultVO<List<SysMenuDO>> userSelfMenuList() {
        return ApiResultVO.ok(baseService.userSelfMenuList());
    }

    @Operation(summary = "通过主键 idSet，加减排序号")
    @PostMapping("/addOrderNo")
    @PreAuthorize("hasAuthority('sysMenu:insertOrUpdate')")
    public ApiResultVO<String> addOrderNo(@RequestBody @Valid AddOrderNoDTO dto) {
        return ApiResultVO.ok(baseService.addOrderNo(dto));
    }

}
