package com.cmcorg.service.engine.web.param.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cmcorg.engine.web.auth.exception.BaseBizCodeEnum;
import com.cmcorg.engine.web.auth.model.entity.BaseEntity;
import com.cmcorg.engine.web.auth.model.vo.ApiResultVO;
import com.cmcorg.engine.web.auth.util.MyEntityUtil;
import com.cmcorg.engine.web.model.model.dto.NotEmptyIdSet;
import com.cmcorg.engine.web.model.model.dto.NotNullId;
import com.cmcorg.service.engine.web.param.mapper.SysParamMapper;
import com.cmcorg.service.engine.web.param.model.dto.SysParamInsertOrUpdateDTO;
import com.cmcorg.service.engine.web.param.model.dto.SysParamPageDTO;
import com.cmcorg.service.engine.web.param.model.entity.SysParamDO;
import com.cmcorg.service.engine.web.param.service.SysParamService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
public class SysParamServiceImpl extends ServiceImpl<SysParamMapper, SysParamDO> implements SysParamService {

    private final Set<Long> notDeleteIdSet = CollUtil.newHashSet(1L, 2L); // 不允许删除的 idSet

    /**
     * 新增/修改
     */
    @Override
    @Transactional
    public String insertOrUpdate(SysParamInsertOrUpdateDTO dto) {

        SysParamDO sysParamDO = new SysParamDO();
        sysParamDO.setName(dto.getName());
        sysParamDO.setValue(dto.getValue());
        sysParamDO.setRemark(MyEntityUtil.getNotNullStr(dto.getRemark()));
        sysParamDO.setEnableFlag(BooleanUtil.isTrue(dto.getEnableFlag()));
        sysParamDO.setDelFlag(false);
        sysParamDO.setId(dto.getId());

        saveOrUpdate(sysParamDO);

        return BaseBizCodeEnum.OK;
    }

    /**
     * 分页排序查询
     */
    @Override
    public Page<SysParamDO> myPage(SysParamPageDTO dto) {

        return lambdaQuery().like(StrUtil.isNotBlank(dto.getName()), SysParamDO::getName, dto.getName())
            .like(StrUtil.isNotBlank(dto.getRemark()), BaseEntity::getRemark, dto.getRemark())
            .eq(dto.getEnableFlag() != null, BaseEntity::getEnableFlag, dto.getEnableFlag())
            .orderByDesc(BaseEntity::getUpdateTime).page(dto.getPage(true));

    }

    /**
     * 通过主键id，查看详情
     */
    @Override
    public SysParamDO infoById(NotNullId notNullId) {
        return getById(notNullId.getId());
    }

    /**
     * 批量删除
     */
    @Override
    @Transactional
    public String deleteByIdSet(NotEmptyIdSet notEmptyIdSet) {

        for (Long item : notDeleteIdSet) {
            if (notEmptyIdSet.getIdSet().contains(item)) {
                ApiResultVO.error("操作失败：id【{}】不允许删除", item);
            }
        }

        removeByIds(notEmptyIdSet.getIdSet());

        return BaseBizCodeEnum.OK;
    }
}



