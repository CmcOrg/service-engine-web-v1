package com.cmcorg.service.engine.web.param.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cmcorg.engine.web.model.model.dto.NotEmptyIdSet;
import com.cmcorg.engine.web.model.model.dto.NotNullId;
import com.cmcorg.service.engine.web.param.model.dto.SysParamInsertOrUpdateDTO;
import com.cmcorg.service.engine.web.param.model.dto.SysParamPageDTO;
import com.cmcorg.service.engine.web.param.model.entity.SysParamDO;

public interface SysParamService extends IService<SysParamDO> {

    String insertOrUpdate(SysParamInsertOrUpdateDTO dto);

    Page<SysParamDO> myPage(SysParamPageDTO dto);

    SysParamDO infoById(NotNullId notNullId);

    String deleteByIdSet(NotEmptyIdSet notEmptyIdSet);

}
