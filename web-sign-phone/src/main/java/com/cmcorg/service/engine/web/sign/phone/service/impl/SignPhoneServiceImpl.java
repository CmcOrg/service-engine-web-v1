package com.cmcorg.service.engine.web.sign.phone.service.impl;

import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import com.cmcorg.engine.web.auth.mapper.SysUserMapper;
import com.cmcorg.engine.web.auth.model.entity.SysUserDO;
import com.cmcorg.engine.web.auth.util.AuthUserUtil;
import com.cmcorg.engine.web.model.model.dto.NotBlankCodeDTO;
import com.cmcorg.engine.web.redisson.enums.RedisKeyEnum;
import com.cmcorg.engine.web.tencent.util.SmsTencentUtil;
import com.cmcorg.service.engine.web.sign.helper.exception.BizCodeEnum;
import com.cmcorg.service.engine.web.sign.helper.util.SignUtil;
import com.cmcorg.service.engine.web.sign.phone.configuration.SignPhoneSecurityPermitAllConfiguration;
import com.cmcorg.service.engine.web.sign.phone.dto.*;
import com.cmcorg.service.engine.web.sign.phone.service.SignPhoneService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class SignPhoneServiceImpl implements SignPhoneService {

    private static RedisKeyEnum PRE_REDIS_KEY_ENUM = RedisKeyEnum.PRE_PHONE;

    @Resource
    SysUserMapper sysUserMapper;

    /**
     * 注册-发送验证码
     */
    @Override
    public String signUpSendCode(PhoneNotBlankDTO dto) {

        String key = PRE_REDIS_KEY_ENUM + dto.getPhone();

        return SignUtil
            .sendCode(key, ChainWrappers.lambdaQueryChain(sysUserMapper).eq(SysUserDO::getPhone, dto.getPhone()), false,
                BizCodeEnum.PHONE_HAS_BEEN_REGISTERED, (code) -> SmsTencentUtil.sendSignUp(dto.getPhone(), code));

    }

    /**
     * 注册
     */
    @Override
    @Transactional
    public String signUp(SignPhoneSignUpDTO dto) {

        return SignUtil
            .signUp(dto.getPassword(), dto.getOrigPassword(), dto.getCode(), PRE_REDIS_KEY_ENUM, dto.getPhone());

    }

    /**
     * 手机账号密码登录
     */
    @Override
    public String signInPassword(SignPhoneSignInPasswordDTO dto) {

        return SignUtil
            .signInPassword(ChainWrappers.lambdaQueryChain(sysUserMapper).eq(SysUserDO::getPhone, dto.getPhone()),
                dto.getPassword(), dto.getPhone());

    }

    /**
     * 修改密码-发送验证码
     */
    @Override
    public String updatePasswordSendCode() {

        return SignUtil.getAccountAndSendCode(PRE_REDIS_KEY_ENUM,
            (code, account) -> SmsTencentUtil.sendResetPassword(account, code));

    }

    /**
     * 修改密码
     */
    @Override
    public String updatePassword(SignPhoneUpdatePasswordDTO dto) {

        return SignUtil
            .updatePassword(dto.getNewPassword(), dto.getOrigNewPassword(), PRE_REDIS_KEY_ENUM, dto.getCode(), null);

    }

    /**
     * 修改手机-发送验证码
     */
    @Override
    public String updateAccountSendCode() {

        String currentUserPhoneNotAdmin = AuthUserUtil.getCurrentUserPhoneNotAdmin();

        String key = PRE_REDIS_KEY_ENUM + currentUserPhoneNotAdmin;

        return SignUtil.sendCode(key, null, true,
            com.cmcorg.engine.web.tencent.exception.BizCodeEnum.PHONE_DOES_NOT_EXIST_PLEASE_RE_ENTER,
            (code) -> SmsTencentUtil.sendUpdate(currentUserPhoneNotAdmin, code));

    }

    /**
     * 修改手机
     */
    @Override
    public String updateAccount(SignPhoneUpdateAccountDTO dto) {

        return SignUtil
            .updateAccount(dto.getOldPhoneCode(), dto.getNewPhoneCode(), PRE_REDIS_KEY_ENUM, dto.getNewPhone(), null);

    }

    /**
     * 忘记密码-发送验证码
     */
    @Override
    public String forgotPasswordSendCode(PhoneNotBlankDTO dto) {

        String key = PRE_REDIS_KEY_ENUM + dto.getPhone();

        return SignUtil
            .sendCode(key, ChainWrappers.lambdaQueryChain(sysUserMapper).eq(SysUserDO::getPhone, dto.getPhone()), true,
                com.cmcorg.engine.web.tencent.exception.BizCodeEnum.PHONE_NOT_REGISTERED,
                (code) -> SmsTencentUtil.sendResetPassword(dto.getPhone(), code));

    }

    /**
     * 忘记密码
     */
    @Override
    public String forgotPassword(SignPhoneForgotPasswordDTO dto) {

        return SignUtil
            .forgotPassword(dto.getNewPassword(), dto.getOrigNewPassword(), dto.getCode(), PRE_REDIS_KEY_ENUM,
                dto.getPhone(), ChainWrappers.lambdaQueryChain(sysUserMapper).eq(SysUserDO::getPhone, dto.getPhone()));

    }

    /**
     * 账号注销-发送验证码
     */
    @Override
    public String signDeleteSendCode() {

        // 如果有更高级的账号注销-发送验证码，则禁用低级的账号注销-发送验证码
        SignUtil.checkSignLevel(SignPhoneSecurityPermitAllConfiguration.SIGN_LEVEL);

        return SignUtil
            .getAccountAndSendCode(PRE_REDIS_KEY_ENUM, (code, account) -> SmsTencentUtil.sendDelete(account, code));

    }

    /**
     * 账号注销
     */
    @Override
    @Transactional
    public String signDelete(NotBlankCodeDTO dto) {

        // 如果有更高级的账号注销，则禁用低级的账号注销
        SignUtil.checkSignLevel(SignPhoneSecurityPermitAllConfiguration.SIGN_LEVEL);

        return SignUtil.signDelete(dto.getCode(), PRE_REDIS_KEY_ENUM, null);

    }

    /**
     * 绑定手机-发送验证码
     */
    @Override
    public String bindAccountSendCode(PhoneNotBlankDTO dto) {

        String key = PRE_REDIS_KEY_ENUM + dto.getPhone();

        return SignUtil
            .sendCode(key, ChainWrappers.lambdaQueryChain(sysUserMapper).eq(SysUserDO::getPhone, dto.getPhone()), false,
                BizCodeEnum.PHONE_HAS_BEEN_REGISTERED, (code) -> SmsTencentUtil.sendBind(dto.getPhone(), code));

    }

    /**
     * 绑定手机
     */
    @Override
    public String bindAccount(SignPhoneBindAccountDTO dto) {

        return SignUtil.bindAccount(dto.getCode(), PRE_REDIS_KEY_ENUM, dto.getPhone());

    }

    /**
     * 手机验证码登录-发送验证码
     */
    @Override
    public String signInSendCode(PhoneNotBlankDTO dto) {

        String key = PRE_REDIS_KEY_ENUM + dto.getPhone();

        return SignUtil
            .sendCode(key, ChainWrappers.lambdaQueryChain(sysUserMapper).eq(SysUserDO::getPhone, dto.getPhone()), true,
                com.cmcorg.engine.web.tencent.exception.BizCodeEnum.PHONE_NOT_REGISTERED,
                (code) -> SmsTencentUtil.sendSignIn(dto.getPhone(), code));

    }

    /**
     * 手机验证码登录
     */
    @Override
    public String signInCode(SignPhoneSignInCodeDTO dto) {

        return SignUtil
            .signInCode(ChainWrappers.lambdaQueryChain(sysUserMapper).eq(SysUserDO::getPhone, dto.getPhone()),
                dto.getCode(), PRE_REDIS_KEY_ENUM, dto.getPhone());

    }

}
