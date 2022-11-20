package com.cmcorg.service.engine.web.sign.signinname.service.impl;

import cn.hutool.core.util.BooleanUtil;
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import com.cmcorg.engine.web.auth.exception.BaseBizCodeEnum;
import com.cmcorg.engine.web.auth.mapper.SysUserMapper;
import com.cmcorg.engine.web.auth.model.entity.BaseEntity;
import com.cmcorg.engine.web.auth.model.entity.SysUserDO;
import com.cmcorg.engine.web.auth.model.vo.ApiResultVO;
import com.cmcorg.engine.web.auth.properties.AuthProperties;
import com.cmcorg.engine.web.auth.util.AuthUserUtil;
import com.cmcorg.engine.web.redisson.enums.RedisKeyEnum;
import com.cmcorg.service.engine.web.sign.helper.util.SignUtil;
import com.cmcorg.service.engine.web.sign.signinname.mode.dto.*;
import com.cmcorg.service.engine.web.sign.signinname.service.SignSignInNameService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class SignSignInNameServiceImpl implements SignSignInNameService {

    private static RedisKeyEnum PRE_REDIS_KEY_ENUM = RedisKeyEnum.PRE_SIGN_IN_NAME;

    @Resource
    SysUserMapper sysUserMapper;

    @Resource
    AuthProperties authProperties;

    /**
     * 注册
     */
    @Override
    @Transactional
    public String signUp(SignSignInNameSignUpDTO dto) {

        if (BooleanUtil.isFalse(authProperties.getSignInNameSignUpEnable())) {
            ApiResultVO.error("操作失败：不允许注册，请联系管理员");
        }

        return SignUtil.signUp(dto.getPassword(), dto.getOrigPassword(), null, PRE_REDIS_KEY_ENUM, dto.getSignInName());

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

        // 检查：登录名，是否不可以执行操作
        checkSignNameCanBeExecutedAndError();

        return SignUtil.updatePassword(dto.getNewPassword(), dto.getOrigNewPassword(), PRE_REDIS_KEY_ENUM, null,
            dto.getOldPassword());

    }

    /**
     * 修改账号
     */
    @Override
    public String updateAccount(SignSignInNameUpdateAccountDTO dto) {

        return SignUtil.updateAccount(null, null, PRE_REDIS_KEY_ENUM, dto.getNewSignInName(), dto.getCurrentPassword());

    }

    /**
     * 账号注销
     */
    @Override
    @Transactional
    public String signDelete(SignSignInNameSignDeleteDTO dto) {

        // 检查：登录名，是否不可以执行操作
        checkSignNameCanBeExecutedAndError();

        return SignUtil.signDelete(null, PRE_REDIS_KEY_ENUM, dto.getCurrentPassword());

    }

    /**
     * 检查：登录名，是否不可以执行操作，如果不可以，则抛出异常
     */
    private void checkSignNameCanBeExecutedAndError() {

        if (checkSignNameNotCanBeExecuted()) {
            ApiResultVO.error(BaseBizCodeEnum.ILLEGAL_REQUEST);
        }

    }

    /**
     * 检查：登录名，是否不可以执行操作
     */
    private boolean checkSignNameNotCanBeExecuted() {

        // 判断是否有：邮箱或者手机号，或者密码等于空，即：密码不能为空，并且不能有手机或者邮箱
        return ChainWrappers.lambdaQueryChain(sysUserMapper)
            .eq(BaseEntity::getId, AuthUserUtil.getCurrentUserIdNotAdmin()).and(
                i -> i.eq(SysUserDO::getPassword, "").or().ne(SysUserDO::getEmail, "").or().ne(SysUserDO::getPhone, ""))
            .exists();

    }

}
