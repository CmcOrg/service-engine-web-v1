package com.cmcorg.service.engine.web.role.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cmcorg.engine.web.auth.exception.BaseBizCodeEnum;
import com.cmcorg.engine.web.auth.mapper.SysRoleMapper;
import com.cmcorg.engine.web.auth.model.entity.BaseEntity;
import com.cmcorg.engine.web.auth.model.entity.SysRoleDO;
import com.cmcorg.engine.web.auth.model.entity.SysRoleRefMenuDO;
import com.cmcorg.engine.web.auth.model.entity.SysRoleRefUserDO;
import com.cmcorg.engine.web.auth.model.vo.ApiResultVO;
import com.cmcorg.engine.web.auth.util.MyEntityUtil;
import com.cmcorg.engine.web.model.model.dto.NotEmptyIdSet;
import com.cmcorg.engine.web.model.model.dto.NotNullId;
import com.cmcorg.service.engine.web.role.exception.BizCodeEnum;
import com.cmcorg.service.engine.web.role.model.dto.SysRoleInsertOrUpdateDTO;
import com.cmcorg.service.engine.web.role.model.dto.SysRolePageDTO;
import com.cmcorg.service.engine.web.role.model.vo.SysRoleInfoByIdVO;
import com.cmcorg.service.engine.web.role.service.SysRoleRefMenuService;
import com.cmcorg.service.engine.web.role.service.SysRoleRefUserService;
import com.cmcorg.service.engine.web.role.service.SysRoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRoleDO> implements SysRoleService {

    @Resource
    SysRoleRefMenuService sysRoleRefMenuService;
    @Resource
    SysRoleRefUserService sysRoleRefUserService;

    /**
     * 新增/修改
     */
    @Override
    @Transactional
    public String insertOrUpdate(SysRoleInsertOrUpdateDTO dto) {

        // 角色名，不能重复
        boolean exists =
            lambdaQuery().eq(SysRoleDO::getName, dto.getName()).ne(dto.getId() != null, BaseEntity::getId, dto.getId())
                .exists();
        if (exists) {
            ApiResultVO.error(BizCodeEnum.THE_SAME_ROLE_NAME_EXIST);
        }

        // 如果是默认角色，则取消之前的默认角色
        if (BooleanUtil.isTrue(dto.getDefaultFlag())) {
            lambdaUpdate().set(SysRoleDO::getDefaultFlag, false).eq(SysRoleDO::getDefaultFlag, true)
                .ne(dto.getId() != null, BaseEntity::getId, dto.getId()).update();
        }

        SysRoleDO sysRoleDO = new SysRoleDO();
        sysRoleDO.setName(dto.getName());
        sysRoleDO.setDefaultFlag(BooleanUtil.isTrue(dto.getDefaultFlag()));
        sysRoleDO.setEnableFlag(BooleanUtil.isTrue(dto.getEnableFlag()));
        sysRoleDO.setRemark(MyEntityUtil.getNotNullStr(dto.getRemark()));
        sysRoleDO.setDelFlag(false);
        sysRoleDO.setId(dto.getId());

        if (dto.getId() == null) { // 新增
            baseMapper.insert(sysRoleDO);
        } else { // 修改
            baseMapper.updateById(sysRoleDO);
            // 先删除子表数据
            deleteByIdSetSub(CollUtil.newHashSet(dto.getId()));
        }

        // 再插入子表数据
        if (CollUtil.isNotEmpty(dto.getMenuIdSet())) {
            List<SysRoleRefMenuDO> insertList = new ArrayList<>();
            for (Long menuId : dto.getMenuIdSet()) {
                SysRoleRefMenuDO sysRoleRefMenuDO = new SysRoleRefMenuDO();
                sysRoleRefMenuDO.setRoleId(sysRoleDO.getId());
                sysRoleRefMenuDO.setMenuId(menuId);
                insertList.add(sysRoleRefMenuDO);
            }
            sysRoleRefMenuService.saveBatch(insertList);
        }

        if (CollUtil.isNotEmpty(dto.getUserIdSet())) {
            List<SysRoleRefUserDO> insertList = new ArrayList<>();
            for (Long userId : dto.getUserIdSet()) {
                SysRoleRefUserDO sysRoleRefUserDO = new SysRoleRefUserDO();
                sysRoleRefUserDO.setRoleId(sysRoleDO.getId());
                sysRoleRefUserDO.setUserId(userId);
                insertList.add(sysRoleRefUserDO);
            }
            sysRoleRefUserService.saveBatch(insertList);
        }

        return BaseBizCodeEnum.OK;
    }

    /**
     * 分页排序查询
     */
    @Override
    public Page<SysRoleDO> myPage(SysRolePageDTO dto) {

        return lambdaQuery().like(StrUtil.isNotBlank(dto.getName()), SysRoleDO::getName, dto.getName())
            .like(StrUtil.isNotBlank(dto.getRemark()), BaseEntity::getRemark, dto.getRemark())
            .eq(dto.getEnableFlag() != null, BaseEntity::getEnableFlag, dto.getEnableFlag())
            .eq(dto.getDefaultFlag() != null, SysRoleDO::getDefaultFlag, dto.getDefaultFlag())
            .orderByDesc(BaseEntity::getUpdateTime).page(dto.getPage(true));

    }

    /**
     * 通过主键id，查看详情
     */
    @Override
    public SysRoleInfoByIdVO infoById(NotNullId notNullId) {

        SysRoleInfoByIdVO sysRoleInfoByIdVO =
            BeanUtil.copyProperties(getById(notNullId.getId()), SysRoleInfoByIdVO.class);

        if (sysRoleInfoByIdVO == null) {
            return null;
        }

        // 完善子表的数据
        List<SysRoleRefMenuDO> menuList =
            sysRoleRefMenuService.lambdaQuery().eq(SysRoleRefMenuDO::getRoleId, sysRoleInfoByIdVO.getId())
                .select(SysRoleRefMenuDO::getMenuId).list();

        List<SysRoleRefUserDO> userList =
            sysRoleRefUserService.lambdaQuery().eq(SysRoleRefUserDO::getRoleId, sysRoleInfoByIdVO.getId())
                .select(SysRoleRefUserDO::getUserId).list();

        sysRoleInfoByIdVO.setMenuIdSet(menuList.stream().map(SysRoleRefMenuDO::getMenuId).collect(Collectors.toSet()));
        sysRoleInfoByIdVO.setUserIdSet(userList.stream().map(SysRoleRefUserDO::getUserId).collect(Collectors.toSet()));

        return sysRoleInfoByIdVO;
    }

    /**
     * 批量删除
     */
    @Override
    @Transactional
    public String deleteByIdSet(NotEmptyIdSet notEmptyIdSet) {

        deleteByIdSetSub(notEmptyIdSet.getIdSet()); // 删除子表数据

        baseMapper.deleteBatchIds(notEmptyIdSet.getIdSet());

        return BaseBizCodeEnum.OK;
    }

    /**
     * 删除子表数据
     */
    private void deleteByIdSetSub(Set<Long> idSet) {

        // 删除 角色菜单关联表
        sysRoleRefMenuService.removeByIds(idSet);
        // 删除 角色用户关联表
        sysRoleRefUserService.removeByIds(idSet);

    }

}




