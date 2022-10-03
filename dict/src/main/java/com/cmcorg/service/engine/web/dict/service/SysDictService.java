package com.cmcorg.service.engine.web.dict.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cmcorg.engine.web.model.model.dto.AddOrderNoDTO;
import com.cmcorg.engine.web.model.model.dto.NotEmptyIdSet;
import com.cmcorg.engine.web.model.model.dto.NotNullId;
import com.cmcorg.service.engine.web.dict.model.dto.SysDictInsertOrUpdateDTO;
import com.cmcorg.service.engine.web.dict.model.dto.SysDictPageDTO;
import com.cmcorg.service.engine.web.dict.model.entity.SysDictDO;
import com.cmcorg.service.engine.web.dict.model.vo.SysDictTreeVO;

import java.util.List;

public interface SysDictService extends IService<SysDictDO> {

    String insertOrUpdate(SysDictInsertOrUpdateDTO dto);

    Page<SysDictDO> myPage(SysDictPageDTO dto);

    List<SysDictTreeVO> tree(SysDictPageDTO dto);

    String deleteByIdSet(NotEmptyIdSet notEmptyIdSet);

    SysDictDO infoById(NotNullId notNullId);

    String addOrderNo(AddOrderNoDTO dto);
}
