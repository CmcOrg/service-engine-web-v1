package com.cmcorg.service.engine.web.role.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cmcorg.engine.web.auth.model.entity.SysRoleDO;
import com.cmcorg.engine.web.model.model.dto.NotEmptyIdSet;
import com.cmcorg.engine.web.model.model.dto.NotNullId;
import com.cmcorg.service.engine.web.role.model.dto.SysRoleInsertOrUpdateDTO;
import com.cmcorg.service.engine.web.role.model.dto.SysRolePageDTO;
import com.cmcorg.service.engine.web.role.model.vo.SysRoleInfoByIdVO;

public interface SysRoleService extends IService<SysRoleDO> {

    String insertOrUpdate(SysRoleInsertOrUpdateDTO dto);

    Page<SysRoleDO> myPage(SysRolePageDTO dto);

    SysRoleInfoByIdVO infoById(NotNullId notNullId);

    String deleteByIdSet(NotEmptyIdSet notEmptyIdSet);

}
