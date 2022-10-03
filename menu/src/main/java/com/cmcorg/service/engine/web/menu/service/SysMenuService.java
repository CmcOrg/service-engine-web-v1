package com.cmcorg.service.engine.web.menu.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cmcorg.engine.web.auth.model.entity.SysMenuDO;
import com.cmcorg.engine.web.model.model.dto.AddOrderNoDTO;
import com.cmcorg.engine.web.model.model.dto.NotEmptyIdSet;
import com.cmcorg.engine.web.model.model.dto.NotNullId;
import com.cmcorg.service.engine.web.menu.model.dto.SysMenuInsertOrUpdateDTO;
import com.cmcorg.service.engine.web.menu.model.dto.SysMenuPageDTO;
import com.cmcorg.service.engine.web.menu.model.vo.SysMenuInfoByIdVO;

import java.util.List;

public interface SysMenuService extends IService<SysMenuDO> {

    String insertOrUpdate(SysMenuInsertOrUpdateDTO dto);

    Page<SysMenuDO> myPage(SysMenuPageDTO dto);

    List<SysMenuDO> tree(SysMenuPageDTO dto);

    String deleteByIdSet(NotEmptyIdSet notEmptyIdSet);

    List<SysMenuDO> userSelfMenuList();

    SysMenuInfoByIdVO infoById(NotNullId notNullId);

    String addOrderNo(AddOrderNoDTO dto);
}
