package com.cmcorg.service.engine.web.user.service.impl;

import cn.hutool.core.util.DesensitizedUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import com.cmcorg.engine.web.auth.exception.BaseBizCodeEnum;
import com.cmcorg.engine.web.auth.mapper.SysUserInfoMapper;
import com.cmcorg.engine.web.auth.mapper.SysUserMapper;
import com.cmcorg.engine.web.auth.model.entity.BaseEntity;
import com.cmcorg.engine.web.auth.model.entity.SysUserDO;
import com.cmcorg.engine.web.auth.model.entity.SysUserInfoDO;
import com.cmcorg.engine.web.auth.properties.AuthProperties;
import com.cmcorg.engine.web.auth.util.AuthUserUtil;
import com.cmcorg.engine.web.auth.util.MyEntityUtil;
import com.cmcorg.engine.web.model.model.constant.BaseConstant;
import com.cmcorg.service.engine.web.user.model.dto.UserSelfUpdateBaseInfoDTO;
import com.cmcorg.service.engine.web.user.model.vo.UserSelfBaseInfoVO;
import com.cmcorg.service.engine.web.user.service.UserSelfService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class UserSelfServiceImpl extends ServiceImpl<SysUserMapper, SysUserDO> implements UserSelfService {

    @Resource
    AuthProperties authProperties;
    @Resource
    SysUserInfoMapper sysUserInfoMapper;

    /**
     * 获取：当前用户，基本信息
     */
    @Override
    public UserSelfBaseInfoVO userSelfBaseInfo() {

        Long currentUserId = AuthUserUtil.getCurrentUserId();

        UserSelfBaseInfoVO sysUserSelfBaseInfoVO = new UserSelfBaseInfoVO();

        if (BaseConstant.ADMIN_ID.equals(currentUserId)) {
            sysUserSelfBaseInfoVO.setAvatarUri("");
            sysUserSelfBaseInfoVO.setNickname(authProperties.getAdminNickname());
            sysUserSelfBaseInfoVO.setBio("");
            sysUserSelfBaseInfoVO.setEmail("");
            sysUserSelfBaseInfoVO.setPasswordFlag(true);
            return sysUserSelfBaseInfoVO;
        }

        SysUserInfoDO sysUserInfoDO =
            ChainWrappers.lambdaQueryChain(sysUserInfoMapper).eq(SysUserInfoDO::getId, currentUserId)
                .select(SysUserInfoDO::getAvatarUri, SysUserInfoDO::getNickname, SysUserInfoDO::getBio).one();

        SysUserDO sysUserDO = lambdaQuery().eq(BaseEntity::getId, currentUserId)
            .select(SysUserDO::getEmail, SysUserDO::getPassword, SysUserDO::getSignInName).one();

        if (sysUserInfoDO != null && sysUserDO != null) {
            sysUserSelfBaseInfoVO.setAvatarUri(sysUserInfoDO.getAvatarUri());
            sysUserSelfBaseInfoVO.setNickname(sysUserInfoDO.getNickname());
            sysUserSelfBaseInfoVO.setBio(sysUserInfoDO.getBio());
            sysUserSelfBaseInfoVO.setEmail(DesensitizedUtil.email(sysUserDO.getEmail())); // 脱敏
            sysUserSelfBaseInfoVO.setSignInName(DesensitizedUtil.chineseName(sysUserDO.getSignInName())); // 脱敏
            sysUserSelfBaseInfoVO.setPasswordFlag(StrUtil.isNotBlank(sysUserDO.getPassword()));
        }

        return sysUserSelfBaseInfoVO;
    }

    /**
     * 当前用户：基本信息：修改
     */
    @Override
    @Transactional
    public String userSelfUpdateBaseInfo(UserSelfUpdateBaseInfoDTO dto) {

        Long currentUserIdNotAdmin = AuthUserUtil.getCurrentUserIdNotAdmin();

        SysUserInfoDO sysUserInfoDO = new SysUserInfoDO();
        sysUserInfoDO.setId(currentUserIdNotAdmin);
        sysUserInfoDO.setNickname(dto.getNickname());
        sysUserInfoDO.setBio(MyEntityUtil.getNotNullStr(dto.getBio()));
        sysUserInfoDO.setAvatarUri(MyEntityUtil.getNotNullStr(dto.getAvatarUri()));

        sysUserInfoMapper.updateById(sysUserInfoDO);

        return BaseBizCodeEnum.OK;
    }

    /**
     * 当前用户：刷新jwt私钥后缀
     */
    @Override
    @Transactional
    public String userSelfRefreshJwtSecretSuf() {

        SysUserDO sysUserDO = new SysUserDO();
        sysUserDO.setId(AuthUserUtil.getCurrentUserIdNotAdmin());
        sysUserDO.setJwtSecretSuf(IdUtil.simpleUUID());

        updateById(sysUserDO);

        return BaseBizCodeEnum.OK;
    }

}
