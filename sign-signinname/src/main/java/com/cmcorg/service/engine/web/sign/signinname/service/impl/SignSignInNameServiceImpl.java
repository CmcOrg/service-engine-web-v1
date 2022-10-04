package com.cmcorg.service.engine.web.sign.signinname.service.impl;

import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import com.cmcorg.engine.web.auth.mapper.SysUserMapper;
import com.cmcorg.engine.web.auth.model.entity.SysUserDO;
import com.cmcorg.engine.web.redisson.enums.RedisKeyEnum;
import com.cmcorg.service.engine.web.sign.helper.util.SignUtil;
import com.cmcorg.service.engine.web.sign.signinname.configuration.SignSignInNameSecurityPermitAllConfiguration;
import com.cmcorg.service.engine.web.sign.signinname.mode.dto.*;
import com.cmcorg.service.engine.web.sign.signinname.service.SignSignInNameService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class SignSignInNameServiceImpl implements SignSignInNameService {

    @Resource
    SysUserMapper sysUserMapper;

    /**
     * 注册
     */
    @Override
    @Transactional
    public String signUp(SignSignInNameSignUpDTO dto) {

        return SignUtil
            .signUp(dto.getPassword(), dto.getOrigPassword(), null, RedisKeyEnum.PRE_SIGN_IN_NAME, dto.getSignInName());
    }

    /**
     * 账号密码登录
     */
    @Override
    public String signInPassword(SignSignInNameSignInPasswordDTO dto) {

        return SignUtil.signInPassword(
            ChainWrappers.lambdaQueryChain(sysUserMapper).eq(SysUserDO::getSignInName, dto.getSignInName()),
            dto.getPassword(), dto.getSignInName());
    }

    /**
     * 修改密码
     */
    @Override
    public String updatePassword(SignSignInNameUpdatePasswordDTO dto) {

        return SignUtil
            .updatePassword(dto.getNewPassword(), dto.getOrigNewPassword(), RedisKeyEnum.PRE_SIGN_IN_NAME, null,
                dto.getOldPassword());
    }

    /**
     * 修改账号
     */
    @Override
    public String updateAccount(SignSignInNameUpdateAccountDTO dto) {

        return SignUtil
            .updateAccount(null, null, RedisKeyEnum.PRE_SIGN_IN_NAME, dto.getNewSignInName(), dto.getCurrentPassword());
    }

    /**
     * 账号注销
     */
    @Override
    public String signDelete(SignSignInNameSignDeleteDTO dto) {

        // 如果有更高级的注销，则禁用低级的注销
        SignUtil.checkSignLevel(SignSignInNameSecurityPermitAllConfiguration.SIGN_LEVEL);

        return SignUtil.signDelete(null, RedisKeyEnum.PRE_SIGN_IN_NAME, dto.getCurrentPassword());
    }

}
