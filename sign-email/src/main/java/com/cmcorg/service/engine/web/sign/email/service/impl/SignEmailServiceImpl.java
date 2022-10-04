package com.cmcorg.service.engine.web.sign.email.service.impl;

import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import com.cmcorg.engine.web.auth.mapper.SysUserMapper;
import com.cmcorg.engine.web.auth.model.entity.SysUserDO;
import com.cmcorg.engine.web.email.model.enums.EmailMessageEnum;
import com.cmcorg.engine.web.email.util.MyEmailUtil;
import com.cmcorg.engine.web.model.model.dto.NotBlankCodeDTO;
import com.cmcorg.engine.web.redisson.enums.RedisKeyEnum;
import com.cmcorg.service.engine.web.sign.email.model.dto.*;
import com.cmcorg.service.engine.web.sign.email.service.SignEmailService;
import com.cmcorg.service.engine.web.sign.helper.exception.BizCodeEnum;
import com.cmcorg.service.engine.web.sign.helper.util.SignUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class SignEmailServiceImpl implements SignEmailService {

    @Resource
    SysUserMapper sysUserMapper;

    /**
     * 发送验证码
     */
    @Override
    public String signUpSendCode(EmailNotBlankDTO dto) {

        String key = RedisKeyEnum.PRE_EMAIL + dto.getEmail();

        return SignUtil
            .sendCode(key, ChainWrappers.lambdaQueryChain(sysUserMapper).eq(SysUserDO::getEmail, dto.getEmail()), false,
                BizCodeEnum.EMAIL_HAS_BEEN_REGISTERED,
                (code) -> MyEmailUtil.send(dto.getEmail(), EmailMessageEnum.SIGN_UP, code, false));
    }

    /**
     * 注册
     */
    @Override
    @Transactional
    public String signUp(SignEmailSignUpDTO dto) {

        return SignUtil
            .signUp(dto.getPassword(), dto.getOrigPassword(), dto.getCode(), RedisKeyEnum.PRE_EMAIL, dto.getEmail());
    }

    /**
     * 邮箱账号密码登录
     */
    @Override
    public String signInPassword(SignEmailSignInPasswordDTO dto) {

        return SignUtil
            .signInPassword(ChainWrappers.lambdaQueryChain(sysUserMapper).eq(SysUserDO::getEmail, dto.getEmail()),
                dto.getPassword(), dto.getEmail());
    }

    /**
     * 修改密码-发送验证码
     */
    @Override
    public String updatePasswordSendCode() {

        return SignUtil.getAccountAndSendCode(RedisKeyEnum.PRE_EMAIL,
            (code, sysUserDO) -> MyEmailUtil.send(sysUserDO.getEmail(), EmailMessageEnum.UPDATE_PASSWORD, code, false));
    }

    /**
     * 修改密码
     */
    @Override
    public String updatePassword(SignEmailUpdatePasswordDTO dto) {

        return SignUtil
            .updatePassword(dto.getNewPassword(), dto.getOrigNewPassword(), RedisKeyEnum.PRE_EMAIL, dto.getCode(),
                null);
    }

    /**
     * 修改邮箱-发送验证码
     */
    @Override
    public String updateAccountSendCode(EmailNotBlankDTO dto) {

        String key = RedisKeyEnum.PRE_EMAIL + dto.getEmail();

        return SignUtil
            .sendCode(key, ChainWrappers.lambdaQueryChain(sysUserMapper).eq(SysUserDO::getEmail, dto.getEmail()), true,
                com.cmcorg.engine.web.email.exception.BizCodeEnum.EMAIL_DOES_NOT_EXIST_PLEASE_RE_ENTER,
                (code) -> MyEmailUtil.send(dto.getEmail(), EmailMessageEnum.UPDATE_EMAIL, code, false));
    }

    /**
     * 修改邮箱
     */
    @Override
    public String updateAccount(SignEmailUpdateAccountDTO dto) {

        return SignUtil
            .updateAccount(dto.getOldEmailCode(), dto.getNewEmailCode(), RedisKeyEnum.PRE_EMAIL, dto.getNewEmail(),
                null);
    }

    /**
     * 忘记密码-发送验证码
     */
    @Override
    public String forgotPasswordSendCode(EmailNotBlankDTO dto) {

        String key = RedisKeyEnum.PRE_EMAIL + dto.getEmail();

        return SignUtil
            .sendCode(key, ChainWrappers.lambdaQueryChain(sysUserMapper).eq(SysUserDO::getEmail, dto.getEmail()), true,
                com.cmcorg.engine.web.email.exception.BizCodeEnum.EMAIL_DOES_NOT_EXIST_PLEASE_RE_ENTER,
                (code) -> MyEmailUtil.send(dto.getEmail(), EmailMessageEnum.FORGOT_PASSWORD, code, false));
    }

    /**
     * 忘记密码
     */
    @Override
    public String forgotPassword(SignEmailForgotPasswordDTO dto) {

        return SignUtil
            .forgotPassword(dto.getNewPassword(), dto.getOrigNewPassword(), dto.getCode(), RedisKeyEnum.PRE_EMAIL,
                dto.getEmail(), ChainWrappers.lambdaQueryChain(sysUserMapper).eq(SysUserDO::getEmail, dto.getEmail()));
    }

    /**
     * 账号注销-发送验证码
     */
    @Override
    public String signDeleteSendCode() {

        return SignUtil.getAccountAndSendCode(RedisKeyEnum.PRE_EMAIL,
            (code, sysUserDO) -> MyEmailUtil.send(sysUserDO.getEmail(), EmailMessageEnum.SIGN_DELETE, code, false));
    }

    /**
     * 账号注销
     */
    @Override
    public String signDelete(NotBlankCodeDTO dto) {

        return SignUtil.signDelete(dto.getCode(), RedisKeyEnum.PRE_EMAIL, null);
    }

    /**
     * 绑定邮箱-发送验证码
     */
    @Override
    public String bindAccountSendCode(EmailNotBlankDTO dto) {

        String key = RedisKeyEnum.PRE_EMAIL + dto.getEmail();

        return SignUtil
            .sendCode(key, ChainWrappers.lambdaQueryChain(sysUserMapper).eq(SysUserDO::getEmail, dto.getEmail()), false,
                BizCodeEnum.EMAIL_HAS_BEEN_REGISTERED,
                (code) -> MyEmailUtil.send(dto.getEmail(), EmailMessageEnum.BIND_EMAIL, code, false));
    }

    /**
     * 绑定邮箱
     */
    @Override
    public String bindAccount(SignEmailBindAccountDTO dto) {

        return SignUtil.bindAccount(dto.getCode(), RedisKeyEnum.PRE_EMAIL, dto.getEmail());
    }

}
