package com.cmcorg.service.engine.web.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.*;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import com.cmcorg.engine.web.auth.exception.BaseBizCodeEnum;
import com.cmcorg.engine.web.auth.mapper.SysUserInfoMapper;
import com.cmcorg.engine.web.auth.mapper.SysUserMapper;
import com.cmcorg.engine.web.auth.model.entity.*;
import com.cmcorg.engine.web.auth.model.vo.ApiResultVO;
import com.cmcorg.engine.web.auth.properties.AuthProperties;
import com.cmcorg.engine.web.auth.util.MyEntityUtil;
import com.cmcorg.engine.web.auth.util.PasswordConvertUtil;
import com.cmcorg.engine.web.model.model.constant.BaseConstant;
import com.cmcorg.engine.web.model.model.constant.BaseRegexConstant;
import com.cmcorg.engine.web.model.model.constant.ParamConstant;
import com.cmcorg.engine.web.model.model.dto.NotEmptyIdSet;
import com.cmcorg.engine.web.model.model.dto.NotNullId;
import com.cmcorg.engine.web.model.model.vo.DictResultVO;
import com.cmcorg.engine.web.redisson.enums.RedisKeyEnum;
import com.cmcorg.engine.web.redisson.util.RedissonUtil;
import com.cmcorg.engine.web.util.util.MyMapUtil;
import com.cmcorg.service.engine.web.param.util.MyRsaUtil;
import com.cmcorg.service.engine.web.param.util.SysParamUtil;
import com.cmcorg.service.engine.web.role.service.SysRoleRefUserService;
import com.cmcorg.service.engine.web.role.service.SysRoleService;
import com.cmcorg.service.engine.web.sign.helper.util.SignUtil;
import com.cmcorg.service.engine.web.user.exception.BizCodeEnum;
import com.cmcorg.service.engine.web.user.mapper.SysUserProMapper;
import com.cmcorg.service.engine.web.user.model.dto.SysUserDictListDTO;
import com.cmcorg.service.engine.web.user.model.dto.SysUserInsertOrUpdateDTO;
import com.cmcorg.service.engine.web.user.model.dto.SysUserPageDTO;
import com.cmcorg.service.engine.web.user.model.dto.SysUserUpdatePasswordDTO;
import com.cmcorg.service.engine.web.user.model.vo.SysUserInfoByIdVO;
import com.cmcorg.service.engine.web.user.model.vo.SysUserPageVO;
import com.cmcorg.service.engine.web.user.service.SysUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserProMapper, SysUserDO> implements SysUserService {

    @Resource
    SysRoleRefUserService sysRoleRefUserService;
    @Resource
    SysUserInfoMapper sysUserInfoMapper;
    @Resource
    AuthProperties authProperties;
    @Resource
    SysUserMapper sysUserMapper;
    @Resource
    SysRoleService sysRoleService;

    /**
     * 分页排序查询
     */
    @Override
    public Page<SysUserPageVO> myPage(SysUserPageDTO dto) {

        Page<SysUserPageVO> page = baseMapper.myPage(dto.getCreateTimeDescDefaultOrderPage(), dto);

        Set<Long> userIdSet = new HashSet<>(MyMapUtil.getInitialCapacity(page.getRecords().size()));

        for (SysUserPageVO item : page.getRecords()) {
            item.setEmail(DesensitizedUtil.email(item.getEmail())); // 脱敏
            item.setSignInName(DesensitizedUtil.chineseName(item.getSignInName())); // 脱敏
            userIdSet.add(item.getId());
        }

        if (userIdSet.size() != 0) {

            List<SysRoleRefUserDO> sysRoleRefUserDOList =
                sysRoleRefUserService.lambdaQuery().in(SysRoleRefUserDO::getUserId, userIdSet)
                    .select(SysRoleRefUserDO::getUserId, SysRoleRefUserDO::getRoleId).list();

            Map<Long, Set<Long>> roleUserGroupMap = sysRoleRefUserDOList.stream().collect(Collectors
                .groupingBy(SysRoleRefUserDO::getUserId,
                    Collectors.mapping(SysRoleRefUserDO::getRoleId, Collectors.toSet())));

            page.getRecords().forEach(it -> it.setRoleIdSet(roleUserGroupMap.get(it.getId())));
        }

        return page;
    }

    /**
     * 下拉列表
     */
    @Override
    public Page<DictResultVO> dictList(SysUserDictListDTO dto) {

        List<SysUserInfoDO> sysUserInfoDOList =
            ChainWrappers.lambdaQueryChain(sysUserInfoMapper).select(SysUserInfoDO::getId, SysUserInfoDO::getNickname)
                .list();

        List<DictResultVO> dictListVOList =
            sysUserInfoDOList.stream().map(it -> new DictResultVO(it.getId(), it.getNickname()))
                .collect(Collectors.toList());

        // 增加 admin账号
        if (BooleanUtil.isTrue(dto.getAddAdminFlag())) {
            dictListVOList.add(new DictResultVO(BaseConstant.ADMIN_ID, authProperties.getAdminNickname()));
        }

        return new Page<DictResultVO>().setTotal(sysUserInfoDOList.size()).setRecords(dictListVOList);
    }

    /**
     * 新增/修改
     */
    @Override
    @Transactional
    public String insertOrUpdate(SysUserInsertOrUpdateDTO dto) {

        boolean emailBlank = StrUtil.isBlank(dto.getEmail());
        boolean signInNameBlank = StrUtil.isBlank(dto.getSignInName());

        if (emailBlank && signInNameBlank) {
            ApiResultVO.error(BizCodeEnum.ACCOUNT_CANNOT_BE_EMPTY);
        }

        boolean passwordFlag = StrUtil.isNotBlank(dto.getPassword()) && StrUtil.isNotBlank(dto.getOrigPassword());

        if (dto.getId() == null && passwordFlag) { // 只有新增时，才可以设置密码
            String paramValue = SysParamUtil.getValueById(ParamConstant.RSA_PRIVATE_KEY_ID); // 获取非对称 私钥
            dto.setOrigPassword(MyRsaUtil.rsaDecrypt(dto.getOrigPassword(), paramValue));
            dto.setPassword(MyRsaUtil.rsaDecrypt(dto.getPassword(), paramValue));

            if (!ReUtil.isMatch(BaseRegexConstant.PASSWORD_REGEXP, dto.getOrigPassword())) {
                ApiResultVO.error(
                    com.cmcorg.service.engine.web.sign.helper.exception.BizCodeEnum.PASSWORD_RESTRICTIONS); // 不合法直接抛出异常
            }
        }

        Set<RedisKeyEnum> redisKeyEnumSet = CollUtil.newHashSet();

        if (!emailBlank) {
            redisKeyEnumSet.add(RedisKeyEnum.PRE_EMAIL);
        }
        if (!signInNameBlank) {
            redisKeyEnumSet.add(RedisKeyEnum.PRE_SIGN_IN_NAME);
        }

        return RedissonUtil.doMultiLock("", redisKeyEnumSet, () -> {

            Map<RedisKeyEnum, String> map = MapUtil.newHashMap();

            // 检查：账号是否存在
            for (RedisKeyEnum item : redisKeyEnumSet) {
                if (accountIsExist(dto, item, map)) {
                    SignUtil.accountIsExistError();
                }
            }

            if (dto.getId() == null) { // 新增：用户

                SysUserInfoDO sysUserInfoDO = new SysUserInfoDO();
                sysUserInfoDO.setNickname(dto.getNickname());
                sysUserInfoDO.setBio(dto.getBio());
                sysUserInfoDO.setAvatarUri(dto.getAvatarUri());
                SysUserDO sysUserDO =
                    SignUtil.insertUser(dto.getPassword(), map, false, sysUserInfoDO, dto.getEnableFlag());

                insertOrUpdateSub(sysUserDO, dto); // 新增数据到子表

            } else { // 修改：用户

                // 删除子表数据
                SignUtil.doSignDeleteSub(CollUtil.newHashSet(dto.getId()), false);

                SysUserDO sysUserDO = new SysUserDO();
                sysUserDO.setId(dto.getId());
                sysUserDO.setEnableFlag(BooleanUtil.isTrue(dto.getEnableFlag()));
                sysUserDO.setEmail(MyEntityUtil.getNotNullStr(dto.getEmail()));
                sysUserDO.setSignInName(MyEntityUtil.getNotNullStr(dto.getSignInName()));
                sysUserMapper.updateById(sysUserDO);

                // 新增数据到子表
                insertOrUpdateSub(sysUserDO, dto);

                SysUserInfoDO sysUserInfoDO = new SysUserInfoDO();
                sysUserInfoDO.setId(dto.getId());
                sysUserInfoDO.setNickname(MyEntityUtil.getNotNullStr(dto.getNickname(), SignUtil.getRandomNickname()));
                sysUserInfoDO.setBio(MyEntityUtil.getNotNullStr(dto.getBio()));
                sysUserInfoDO.setAvatarUri(MyEntityUtil.getNotNullStr(dto.getAvatarUri()));
                sysUserInfoMapper.updateById(sysUserInfoDO);

            }

            return BaseBizCodeEnum.OK;
        });
    }

    /**
     * 判断：账号是否重复
     */
    private boolean accountIsExist(SysUserInsertOrUpdateDTO dto, RedisKeyEnum item, Map<RedisKeyEnum, String> map) {
        boolean exist = false;
        if (RedisKeyEnum.PRE_EMAIL.equals(item)) {
            exist = SignUtil.accountIsExist(RedisKeyEnum.PRE_EMAIL, dto.getEmail(), dto.getId());
            map.put(item, dto.getEmail());
        } else if (RedisKeyEnum.PRE_SIGN_IN_NAME.equals(item)) {
            exist = SignUtil.accountIsExist(RedisKeyEnum.PRE_SIGN_IN_NAME, dto.getSignInName(), dto.getId());
            map.put(item, dto.getSignInName());
        }
        return exist;
    }

    /**
     * 新增/修改：新增数据到子表
     */
    private void insertOrUpdateSub(SysUserDO sysUserDO, SysUserInsertOrUpdateDTO dto) {

        // 如果禁用了，则子表不进行新增操作
        if (BooleanUtil.isFalse(sysUserDO.getEnableFlag())) {
            return;
        }

        // 新增数据到：角色用户关联表
        if (CollUtil.isNotEmpty(dto.getRoleIdSet())) {

            // 获取：没有被禁用的角色 idSet
            List<SysRoleDO> sysRoleDOList = sysRoleService.lambdaQuery().in(BaseEntity::getId, dto.getRoleIdSet())
                .eq(BaseEntity::getEnableFlag, true).select(BaseEntity::getId).list();

            Set<Long> roleIdSet = sysRoleDOList.stream().map(BaseEntity::getId).collect(Collectors.toSet());

            List<SysRoleRefUserDO> insertList = new ArrayList<>();
            for (Long item : roleIdSet) {
                SysRoleRefUserDO sysRoleRefUserDO = new SysRoleRefUserDO();
                sysRoleRefUserDO.setRoleId(item);
                sysRoleRefUserDO.setUserId(sysUserDO.getId());
                insertList.add(sysRoleRefUserDO);
            }
            sysRoleRefUserService.saveBatch(insertList);
        }

    }

    /**
     * 批量注销用户
     */
    @Override
    @Transactional
    public String deleteByIdSet(NotEmptyIdSet notEmptyIdSet) {

        SignUtil.doSignDelete(notEmptyIdSet.getIdSet());

        return BaseBizCodeEnum.OK;
    }

    /**
     * 通过主键id，查看详情
     */
    @Override
    public SysUserInfoByIdVO infoById(NotNullId notNullId) {

        SysUserDO sysUserDO = lambdaQuery()
            .select(SysUserDO::getSignInName, SysUserDO::getEmail, SysUserDO::getEnableFlag, SysUserDO::getPhone,
                BaseEntity::getId, BaseEntity::getUpdateTime, BaseEntity::getCreateTime)
            .eq(BaseEntity::getId, notNullId.getId()).one();

        SysUserInfoByIdVO sysUserInfoByIdVO = BeanUtil.copyProperties(sysUserDO, SysUserInfoByIdVO.class);

        if (sysUserInfoByIdVO == null) {
            return null;
        }

        SysUserInfoDO sysUserInfoDO =
            ChainWrappers.lambdaQueryChain(sysUserInfoMapper).eq(SysUserInfoDO::getId, notNullId.getId())
                .select(SysUserInfoDO::getNickname, SysUserInfoDO::getAvatarUri, SysUserInfoDO::getBio).one();

        sysUserInfoByIdVO.setNickname(sysUserInfoDO.getNickname());
        sysUserInfoByIdVO.setAvatarUri(sysUserInfoDO.getAvatarUri());
        sysUserInfoByIdVO.setBio(sysUserInfoDO.getBio());

        // 获取：用户绑定的角色 idSet
        List<SysRoleRefUserDO> refUserDOList =
            sysRoleRefUserService.lambdaQuery().eq(SysRoleRefUserDO::getUserId, notNullId.getId())
                .select(SysRoleRefUserDO::getRoleId).list();
        Set<Long> roleIdSet = refUserDOList.stream().map(SysRoleRefUserDO::getRoleId).collect(Collectors.toSet());

        sysUserInfoByIdVO.setRoleIdSet(roleIdSet);

        return sysUserInfoByIdVO;
    }

    /**
     * 刷新用户 jwt私钥后缀
     */
    @Override
    @Transactional
    public String refreshJwtSecretSuf(NotEmptyIdSet notEmptyIdSet, String password) {

        List<SysUserDO> updateList = new ArrayList<>();

        for (Long item : notEmptyIdSet.getIdSet()) {
            SysUserDO sysUserDO = new SysUserDO();
            sysUserDO.setId(item);
            sysUserDO.setJwtSecretSuf(IdUtil.simpleUUID());
            if (password != null) {
                sysUserDO.setPassword(password);
            }
            updateList.add(sysUserDO);
        }

        updateBatchById(updateList);

        return BaseBizCodeEnum.OK;
    }

    /**
     * 批量重置头像
     */
    @Override
    @Transactional
    public String resetAvatar(NotEmptyIdSet notEmptyIdSet) {

        ChainWrappers.lambdaUpdateChain(sysUserInfoMapper).in(SysUserInfoDO::getId, notEmptyIdSet.getIdSet())
            .set(SysUserInfoDO::getAvatarUri, "").update();

        return BaseBizCodeEnum.OK;
    }

    /**
     * 批量修改密码
     */
    @Override
    @Transactional
    public String updatePassword(SysUserUpdatePasswordDTO dto) {

        boolean passwordFlag = StrUtil.isNotBlank(dto.getNewPassword()) && StrUtil.isNotBlank(dto.getNewOrigPassword());

        String password = "";

        if (passwordFlag) {

            String paramValue = SysParamUtil.getValueById(ParamConstant.RSA_PRIVATE_KEY_ID); // 获取非对称 私钥
            dto.setNewOrigPassword(MyRsaUtil.rsaDecrypt(dto.getNewOrigPassword(), paramValue));
            dto.setNewPassword(MyRsaUtil.rsaDecrypt(dto.getNewPassword(), paramValue));

            if (!ReUtil.isMatch(BaseRegexConstant.PASSWORD_REGEXP, dto.getNewOrigPassword())) {
                ApiResultVO.error(
                    com.cmcorg.service.engine.web.sign.helper.exception.BizCodeEnum.PASSWORD_RESTRICTIONS); // 不合法直接抛出异常
            }

            password = PasswordConvertUtil.convert(dto.getNewPassword(), true);
        }

        refreshJwtSecretSuf(new NotEmptyIdSet(dto.getIdSet()), password); // 刷新：jwt私钥后缀，并重新设置密码

        return BaseBizCodeEnum.OK;
    }

}
