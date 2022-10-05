package com.cmcorg.service.engine.web.sign.helper.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.*;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import com.cmcorg.engine.web.auth.exception.BaseBizCodeEnum;
import com.cmcorg.engine.web.auth.mapper.SysRoleRefUserMapper;
import com.cmcorg.engine.web.auth.mapper.SysUserInfoMapper;
import com.cmcorg.engine.web.auth.mapper.SysUserMapper;
import com.cmcorg.engine.web.auth.model.entity.BaseEntity;
import com.cmcorg.engine.web.auth.model.entity.SysRoleRefUserDO;
import com.cmcorg.engine.web.auth.model.entity.SysUserDO;
import com.cmcorg.engine.web.auth.model.entity.SysUserInfoDO;
import com.cmcorg.engine.web.auth.model.vo.ApiResultVO;
import com.cmcorg.engine.web.auth.properties.AuthProperties;
import com.cmcorg.engine.web.auth.util.*;
import com.cmcorg.engine.web.model.exception.IBizCode;
import com.cmcorg.engine.web.model.model.constant.BaseConstant;
import com.cmcorg.engine.web.model.model.constant.BaseRegexConstant;
import com.cmcorg.engine.web.model.model.constant.LogTopicConstant;
import com.cmcorg.engine.web.model.model.constant.ParamConstant;
import com.cmcorg.engine.web.redisson.enums.RedisKeyEnum;
import com.cmcorg.engine.web.redisson.util.RedissonUtil;
import com.cmcorg.service.engine.web.param.util.MyRsaUtil;
import com.cmcorg.service.engine.web.param.util.SysParamUtil;
import com.cmcorg.service.engine.web.sign.helper.configuration.AbstractSignHelperSecurityPermitAllConfiguration;
import com.cmcorg.service.engine.web.sign.helper.exception.BizCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RBatch;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j(topic = LogTopicConstant.USER)
public class SignUtil {

    private static SysUserInfoMapper sysUserInfoMapper;
    private static SysUserMapper sysUserMapper;
    private static SysRoleRefUserMapper sysRoleRefUserMapper;
    private static RedissonClient redissonClient;
    private static AuthProperties authProperties;
    private static List<AbstractSignHelperSecurityPermitAllConfiguration>
        abstractSignHelperSecurityPermitAllConfigurationList;

    public SignUtil(SysUserInfoMapper sysUserInfoMapper, RedissonClient redissonClient, SysUserMapper sysUserMapper,
        AuthProperties authProperties,
        List<AbstractSignHelperSecurityPermitAllConfiguration> abstractSignHelperSecurityPermitAllConfigurationList,
        SysRoleRefUserMapper sysRoleRefUserMapper) {
        SignUtil.sysUserInfoMapper = sysUserInfoMapper;
        SignUtil.sysUserMapper = sysUserMapper;
        SignUtil.redissonClient = redissonClient;
        SignUtil.authProperties = authProperties;
        SignUtil.abstractSignHelperSecurityPermitAllConfigurationList =
            abstractSignHelperSecurityPermitAllConfigurationList;
        SignUtil.sysRoleRefUserMapper = sysRoleRefUserMapper;
    }

    public interface SignSendCodeInterface {
        /**
         * 回调函数
         */
        void doAfter(String code);
    }

    public interface SignGetAccountAndSendCodeInterface {
        /**
         * 回调函数
         */
        void doAfter(String code, SysUserDO sysUserDO);
    }

    /**
     * 发送验证码
     */
    public static String sendCode(String key, LambdaQueryChainWrapper<SysUserDO> lambdaQueryChainWrapper,
        boolean mustExist, IBizCode iBizCode, SignSendCodeInterface signSendCodeInterface) {

        return RedissonUtil.doLock(key, () -> {

            // 判断是否存在
            boolean exists = lambdaQueryChainWrapper.exists();

            if (mustExist) {
                if (!exists) {
                    ApiResultVO.error(iBizCode);
                }
            } else {
                if (exists) {
                    ApiResultVO.error(iBizCode);
                }
            }

            String code = CodeUtil.getCode();

            // 保存到 redis中，设置10分钟过期
            redissonClient.getBucket(key).set(code, BaseConstant.MINUTE_10_EXPIRE_TIME, TimeUnit.MILLISECONDS);

            signSendCodeInterface.doAfter(code);

            return BaseBizCodeEnum.SEND_OK;
        });
    }

    /**
     * 获取账户信息，并执行发送验证码操作
     */
    public static String getAccountAndSendCode(RedisKeyEnum redisKeyEnum,
        SignGetAccountAndSendCodeInterface signGetAccountAndSendCodeInterface) {

        SysUserDO sysUserDO = getSysUserDOByIdAndRedisKeyEnum(redisKeyEnum, AuthUserUtil.getCurrentUserIdNotAdmin());

        String code = CodeUtil.getCode();

        // 保存到 redis中，设置10分钟过期
        redissonClient.getBucket(redisKeyEnum + code)
            .set(code, BaseConstant.MINUTE_10_EXPIRE_TIME, TimeUnit.MILLISECONDS);

        // 执行：发送验证码操作
        signGetAccountAndSendCodeInterface.doAfter(code, sysUserDO);

        return BaseBizCodeEnum.SEND_OK;
    }

    /**
     * 注册，注意：调用此方法，必须加 事务
     */
    public static String signUp(String password, String origPassword, String code, RedisKeyEnum redisKeyEnum,
        String account) {

        if (BaseConstant.ADMIN_ACCOUNT.equals(account)) {
            ApiResultVO.error(BaseBizCodeEnum.THE_ADMIN_ACCOUNT_DOES_NOT_SUPPORT_THIS_OPERATION);
        }

        String paramValue = SysParamUtil.getValueById(ParamConstant.RSA_PRIVATE_KEY_ID); // 获取非对称 私钥
        password = MyRsaUtil.rsaDecrypt(password, paramValue);
        origPassword = MyRsaUtil.rsaDecrypt(origPassword, paramValue);

        if (!ReUtil.isMatch(BaseRegexConstant.PASSWORD_REGEXP, origPassword)) {
            ApiResultVO.error(BizCodeEnum.PASSWORD_RESTRICTIONS); // 不合法直接抛出异常
        }

        String key = redisKeyEnum + account;

        String finalPassword = password;
        return RedissonUtil.doLock(key, () -> {

            RBucket<String> bucket = redissonClient.getBucket(key);

            if (!RedisKeyEnum.PRE_SIGN_IN_NAME.equals(redisKeyEnum)) {
                CodeUtil.checkCode(code, bucket.get()); // 检查 code是否正确
            }

            // 检查：注册的登录账号是否存在
            boolean exist = accountIsExist(redisKeyEnum, account, null);
            if (exist) {
                if (!RedisKeyEnum.PRE_SIGN_IN_NAME.equals(redisKeyEnum)) {
                    bucket.delete(); // 删除：验证码
                }
                accountIsExistError();
            }

            Map<RedisKeyEnum, String> map = MapUtil.newHashMap();
            map.put(redisKeyEnum, account);

            SignUtil.insertUser(finalPassword, map, true, null, null); // 新增：用户

            if (!RedisKeyEnum.PRE_SIGN_IN_NAME.equals(redisKeyEnum)) {
                bucket.delete(); // 删除：验证码
            }

            return "注册成功";
        });
    }

    /**
     * 新增：用户
     */
    public static SysUserDO insertUser(String password, Map<RedisKeyEnum, String> accountMap,
        boolean checkPasswordBlank, SysUserInfoDO tempSysUserInfoDO, Boolean enableFlag) {

        SysUserDO sysUserDO = new SysUserDO();
        if (enableFlag == null) {
            sysUserDO.setEnableFlag(true);
        } else {
            sysUserDO.setEnableFlag(enableFlag);
        }
        sysUserDO.setDelFlag(false);
        sysUserDO.setRemark("");

        sysUserDO.setEmail("");
        sysUserDO.setSignInName("");
        sysUserDO.setPhone("");

        for (Map.Entry<RedisKeyEnum, String> item : accountMap.entrySet()) {
            if (RedisKeyEnum.PRE_EMAIL.equals(item.getKey())) {
                sysUserDO.setEmail(item.getValue());
            } else if (RedisKeyEnum.PRE_SIGN_IN_NAME.equals(item.getKey())) {
                sysUserDO.setSignInName(item.getValue());
            }
        }

        sysUserDO.setJwtSecretSuf(IdUtil.simpleUUID());
        sysUserDO.setPassword(PasswordConvertUtil.convert(password, checkPasswordBlank));
        sysUserMapper.insert(sysUserDO); // 保存：用户

        SysUserInfoDO sysUserInfoDO = new SysUserInfoDO();
        sysUserInfoDO.setId(sysUserDO.getId());
        sysUserInfoDO.setUuid(IdUtil.simpleUUID());

        if (tempSysUserInfoDO == null) {
            sysUserInfoDO.setNickname(getRandomNickname());
            sysUserInfoDO.setBio("");
            sysUserInfoDO.setAvatarUri("");
        } else {
            sysUserInfoDO.setNickname(MyEntityUtil.getNotNullStr(tempSysUserInfoDO.getNickname(), getRandomNickname()));
            sysUserInfoDO.setBio(MyEntityUtil.getNotNullStr(tempSysUserInfoDO.getBio()));
            sysUserInfoDO.setAvatarUri(MyEntityUtil.getNotNullStr(tempSysUserInfoDO.getAvatarUri()));
        }

        sysUserInfoMapper.insert(sysUserInfoDO); // 保存：用户基本信息

        return sysUserDO;
    }

    /**
     * 账号密码登录
     */
    public static String signInPassword(LambdaQueryChainWrapper<SysUserDO> lambdaQueryChainWrapper, String password,
        String account) {

        password = MyRsaUtil.rsaDecrypt(password);

        if (BaseConstant.ADMIN_ACCOUNT.equals(account)) { // 如果是 admin账户
            if (BooleanUtil.isTrue(authProperties.getAdminEnable())) { // 并且配置文件中允许 admin登录
                // 判断：密码错误次数过多，是否被冻结
                checkTooManyPasswordErrors(BaseConstant.ADMIN_ID);
                if (!authProperties.getAdminPassword().equals(password)) {
                    passwordErrorHandler(BaseConstant.ADMIN_ID);
                    ApiResultVO.error(BizCodeEnum.ACCOUNT_OR_PASSWORD_NOT_VALID);
                }
                return MyJwtUtil.generateJwt(BaseConstant.ADMIN_ID, null);
            } else {
                ApiResultVO.error(BizCodeEnum.ACCOUNT_OR_PASSWORD_NOT_VALID);
            }
        }

        SysUserDO sysUserDO = lambdaQueryChainWrapper
            .select(SysUserDO::getPassword, BaseEntity::getEnableFlag, SysUserDO::getJwtSecretSuf, BaseEntity::getId)
            .one();

        // 账户是否存在
        if (sysUserDO == null) {
            ApiResultVO.error(BizCodeEnum.ACCOUNT_OR_PASSWORD_NOT_VALID);
        }

        // 判断：密码错误次数过多，是否被冻结
        checkTooManyPasswordErrors(sysUserDO.getId());

        if (StrUtil.isBlank(sysUserDO.getPassword())) {
            ApiResultVO.error(BizCodeEnum.NO_PASSWORD_SET); // 未设置密码，请点击【忘记密码】，进行密码设置
        }

        if (!PasswordConvertUtil.match(sysUserDO.getPassword(), password)) {
            passwordErrorHandler(sysUserDO.getId());
            ApiResultVO.error(BizCodeEnum.ACCOUNT_OR_PASSWORD_NOT_VALID);
        }

        // 校验密码，成功之后，再判断是否被冻结，免得透露用户被封号的信息
        if (!sysUserDO.getEnableFlag()) {
            ApiResultVO.error(BizCodeEnum.ACCOUNT_IS_DISABLED);
        }

        // 颁发，并返回 jwt
        return MyJwtUtil.generateJwt(sysUserDO.getId(), sysUserDO.getJwtSecretSuf());
    }

    /**
     * 判断：密码错误次数过多，是否被冻结
     */
    private static void checkTooManyPasswordErrors(Long id) {

        String lockMessage =
            redissonClient.<Long, String>getMap(RedisKeyEnum.PRE_TOO_MANY_PASSWORD_ERRORS.name()).get(id);
        if (StrUtil.isNotBlank(lockMessage)) {
            ApiResultVO.error(BizCodeEnum.TOO_MANY_PASSWORD_ERRORS);
        }
    }

    /**
     * 密码错误次数过多，直接锁定账号，可以进行【忘记密码】操作，解除锁定
     */
    private static void passwordErrorHandler(Long userId) {

        RAtomicLong atomicLong = redissonClient.getAtomicLong(RedisKeyEnum.PRE_PASSWORD_ERROR_COUNT.name() + userId);

        long count = atomicLong.incrementAndGet(); // 次数 + 1

        if (count == 1) {
            atomicLong.expire(Duration.ofMillis(BaseConstant.DAY_30_EXPIRE_TIME)); // 等于 1表示，是第一次访问，则设置过期时间
        }
        if (count > 10) {
            // 超过十次密码错误，则封禁账号，下次再错误，则才会提示
            redissonClient.<Long, String>getMap(RedisKeyEnum.PRE_TOO_MANY_PASSWORD_ERRORS.name())
                .put(userId, "密码错误次数过多，被锁定的账号");
            atomicLong.delete(); // 清空错误次数
        }
    }

    /**
     * 修改密码
     */
    public static String updatePassword(String newPassword, String origNewPassword, RedisKeyEnum redisKeyEnum,
        String code, String oldPassword) {

        Long currentUserIdNotAdmin = AuthUserUtil.getCurrentUserIdNotAdmin();

        if (StrUtil.isNotBlank(oldPassword)) {
            checkCurrentPassword(oldPassword, currentUserIdNotAdmin);
        }

        String paramValue = SysParamUtil.getValueById(ParamConstant.RSA_PRIVATE_KEY_ID); // 获取非对称 私钥
        newPassword = MyRsaUtil.rsaDecrypt(newPassword, paramValue);
        origNewPassword = MyRsaUtil.rsaDecrypt(origNewPassword, paramValue);

        if (!ReUtil.isMatch(BaseRegexConstant.PASSWORD_REGEXP, origNewPassword)) {
            ApiResultVO.error(BizCodeEnum.PASSWORD_RESTRICTIONS); // 不合法直接抛出异常
        }

        String account = getAccountByIdAndRedisKeyEnum(redisKeyEnum, currentUserIdNotAdmin);

        String key = redisKeyEnum + account;

        String finalNewPassword = newPassword;
        return RedissonUtil.doLock(key, () -> {

            RBucket<String> bucket = redissonClient.getBucket(key);

            if (!RedisKeyEnum.PRE_SIGN_IN_NAME.equals(redisKeyEnum)) {
                CodeUtil.checkCode(code, bucket.get()); // 检查 code是否正确
            }

            SysUserDO sysUserDO = new SysUserDO();
            sysUserDO.setId(currentUserIdNotAdmin);
            sysUserDO.setJwtSecretSuf(IdUtil.simpleUUID());
            sysUserDO.setPassword(PasswordConvertUtil.convert(finalNewPassword, true));
            sysUserMapper.updateById(sysUserDO); // 保存：用户

            if (!RedisKeyEnum.PRE_SIGN_IN_NAME.equals(redisKeyEnum)) {
                bucket.delete(); // 删除：验证码
            }

            return BaseBizCodeEnum.OK;
        });
    }

    /**
     * 检查：当前密码是否正确
     */
    private static void checkCurrentPassword(String currentPassword, Long currentUserIdNotAdmin) {

        SysUserDO sysUserDO = ChainWrappers.lambdaQueryChain(sysUserMapper).eq(BaseEntity::getId, currentUserIdNotAdmin)
            .select(SysUserDO::getPassword).one();

        if (sysUserDO == null) {
            ApiResultVO.error(BizCodeEnum.USER_DOES_NOT_EXIST);
        }

        if (!PasswordConvertUtil.match(sysUserDO.getPassword(), currentPassword)) {
            passwordErrorHandler(sysUserDO.getId());
            ApiResultVO.error(BizCodeEnum.PASSWORD_NOT_VALID);
        }

    }

    @NotNull
    private static String getAccountByIdAndRedisKeyEnum(RedisKeyEnum redisKeyEnum, Long currentUserIdNotAdmin) {

        SysUserDO sysUserDO = getSysUserDOByIdAndRedisKeyEnum(redisKeyEnum, currentUserIdNotAdmin);

        if (RedisKeyEnum.PRE_EMAIL.equals(redisKeyEnum)) {
            return sysUserDO.getEmail();
        } else if (RedisKeyEnum.PRE_SIGN_IN_NAME.equals(redisKeyEnum)) {
            return sysUserDO.getSignInName();
        } else {
            ApiResultVO.sysError();
            return null; // 这里不会执行，只是为了通过语法检查
        }
    }

    @NotNull
    private static SysUserDO getSysUserDOByIdAndRedisKeyEnum(RedisKeyEnum redisKeyEnum, Long currentUserIdNotAdmin) {

        LambdaQueryChainWrapper<SysUserDO> lambdaQueryChainWrapper =
            ChainWrappers.lambdaQueryChain(sysUserMapper).eq(BaseEntity::getId, currentUserIdNotAdmin);

        if (RedisKeyEnum.PRE_EMAIL.equals(redisKeyEnum)) {
            lambdaQueryChainWrapper.select(SysUserDO::getEmail);
        } else if (RedisKeyEnum.PRE_SIGN_IN_NAME.equals(redisKeyEnum)) {
            lambdaQueryChainWrapper.select(SysUserDO::getSignInName);
        } else {
            ApiResultVO.sysError();
        }

        SysUserDO sysUserDO = lambdaQueryChainWrapper.one();

        if (sysUserDO == null) {
            ApiResultVO.error(BizCodeEnum.USER_DOES_NOT_EXIST);
        }

        return sysUserDO;
    }

    /**
     * 修改登录账号
     */
    public static String updateAccount(String oldCode, String newCode, RedisKeyEnum redisKeyEnum, String newAccount,
        String currentPassword) {

        Long currentUserIdNotAdmin = AuthUserUtil.getCurrentUserIdNotAdmin();

        if (RedisKeyEnum.PRE_SIGN_IN_NAME.equals(redisKeyEnum)) {
            checkTooManyPasswordErrors(currentUserIdNotAdmin);
            checkCurrentPassword(currentPassword, currentUserIdNotAdmin);
        }

        String oldAccount = getAccountByIdAndRedisKeyEnum(redisKeyEnum, currentUserIdNotAdmin);

        String oldKey = redisKeyEnum + oldAccount;
        String newKey = redisKeyEnum + newAccount;

        return RedissonUtil.doMultiLock("", CollUtil.newHashSet(oldKey, newKey), () -> {

            RBucket<String> oldBucket = redissonClient.getBucket(oldKey);
            if (!RedisKeyEnum.PRE_SIGN_IN_NAME.equals(redisKeyEnum)) {
                CodeUtil.checkCode(oldCode, oldBucket.get(), "操作失败：请先获取原账号的验证码", "原账号验证码有误，请重新输入"); // 检查 code是否正确
            }

            RBucket<String> newBucket = redissonClient.getBucket(newKey);
            if (!RedisKeyEnum.PRE_SIGN_IN_NAME.equals(redisKeyEnum)) {
                CodeUtil.checkCode(newCode, newBucket.get(), "操作失败：请先获取新账号的验证码", "新账号验证码有误，请重新输入"); // 检查 code是否正确
            }

            // 检查：新的登录账号是否存在
            boolean exist = accountIsExist(redisKeyEnum, newAccount, currentUserIdNotAdmin);
            if (exist) {
                newBucket.delete();
                ApiResultVO.error("操作失败：已被其他人绑定，请重试");
            }

            SysUserDO sysUserDO = new SysUserDO();
            sysUserDO.setId(currentUserIdNotAdmin);

            // 通过：RedisKeyEnum，设置：账号
            setSysUserDOAccountByRedisKeyEnum(redisKeyEnum, newAccount, sysUserDO);

            sysUserDO.setJwtSecretSuf(IdUtil.simpleUUID());
            sysUserMapper.updateById(sysUserDO); // 更新：用户

            if (!RedisKeyEnum.PRE_SIGN_IN_NAME.equals(redisKeyEnum)) {
                // 删除：验证码
                oldBucket.delete();
                newBucket.delete();
            }

            return BaseBizCodeEnum.OK;
        });
    }

    /**
     * 通过：RedisKeyEnum，设置：账号
     */
    private static void setSysUserDOAccountByRedisKeyEnum(RedisKeyEnum redisKeyEnum, String newAccount,
        SysUserDO sysUserDO) {

        if (RedisKeyEnum.PRE_EMAIL.equals(redisKeyEnum)) {
            sysUserDO.setEmail(newAccount);
        } else if (RedisKeyEnum.PRE_SIGN_IN_NAME.equals(redisKeyEnum)) {
            sysUserDO.setSignInName(newAccount);
        } else {
            ApiResultVO.sysError();
        }
    }

    /**
     * 检查登录账号是否存在
     */
    public static boolean accountIsExist(RedisKeyEnum redisKeyEnum, String newAccount, Long id) {

        LambdaQueryChainWrapper<SysUserDO> lambdaQueryChainWrapper =
            ChainWrappers.lambdaQueryChain(sysUserMapper).ne(id != null, BaseEntity::getId, id);

        if (RedisKeyEnum.PRE_EMAIL.equals(redisKeyEnum)) {
            lambdaQueryChainWrapper.eq(SysUserDO::getEmail, newAccount);
        } else if (RedisKeyEnum.PRE_SIGN_IN_NAME.equals(redisKeyEnum)) {
            lambdaQueryChainWrapper.eq(SysUserDO::getSignInName, newAccount);
        } else {
            ApiResultVO.sysError();
        }

        return lambdaQueryChainWrapper.exists();
    }

    public static void accountIsExistError() {
        ApiResultVO.error(BizCodeEnum.THE_ACCOUNT_HAS_ALREADY_BEEN_REGISTERED);
    }

    /**
     * 忘记密码
     */
    public static String forgotPassword(String newPassword, String origNewPassword, String code,
        RedisKeyEnum redisKeyEnum, String account, LambdaQueryChainWrapper<SysUserDO> lambdaQueryChainWrapper) {

        String paramValue = SysParamUtil.getValueById(ParamConstant.RSA_PRIVATE_KEY_ID); // 获取非对称 私钥
        newPassword = MyRsaUtil.rsaDecrypt(newPassword, paramValue);
        origNewPassword = MyRsaUtil.rsaDecrypt(origNewPassword, paramValue);

        if (!ReUtil.isMatch(BaseRegexConstant.PASSWORD_REGEXP, origNewPassword)) {
            ApiResultVO.error(BizCodeEnum.PASSWORD_RESTRICTIONS); // 不合法直接抛出异常
        }

        String key = redisKeyEnum.name() + account;

        String finalNewPassword = newPassword;
        return RedissonUtil.doLock(key, () -> {

            RBucket<String> bucket = redissonClient.getBucket(key);

            CodeUtil.checkCode(code, bucket.get()); // 检查 code是否正确

            // 获取：用户 id
            SysUserDO sysUserDO = lambdaQueryChainWrapper.select(BaseEntity::getId).one();
            if (sysUserDO == null) {
                bucket.delete(); // 删除：验证码
                ApiResultVO.error(BizCodeEnum.USER_DOES_NOT_EXIST);
            }

            sysUserDO.setJwtSecretSuf(IdUtil.simpleUUID());
            sysUserDO.setPassword(PasswordConvertUtil.convert(finalNewPassword, true));
            sysUserMapper.updateById(sysUserDO); // 保存：用户

            RBatch batch = redissonClient.createBatch();

            // 移除密码错误次数相关
            batch.getBucket(RedisKeyEnum.PRE_PASSWORD_ERROR_COUNT.name() + sysUserDO.getId()).deleteAsync();
            batch.getBucket(RedisKeyEnum.PRE_TOO_MANY_PASSWORD_ERRORS.name() + sysUserDO.getId()).deleteAsync();

            // 删除：验证码
            batch.getBucket(key).deleteAsync();

            batch.execute(); // 执行批量操作

            return BaseBizCodeEnum.OK;
        });
    }

    /**
     * 账号注销
     */
    public static String signDelete(String code, RedisKeyEnum redisKeyEnum, String currentPassword) {

        Long currentUserIdNotAdmin = AuthUserUtil.getCurrentUserIdNotAdmin();

        if (StrUtil.isNotBlank(currentPassword)) {
            checkCurrentPassword(currentPassword, currentUserIdNotAdmin);
        }

        String account = getAccountByIdAndRedisKeyEnum(redisKeyEnum, currentUserIdNotAdmin);

        String key = redisKeyEnum + account;

        return RedissonUtil.doLock(key, () -> {

            RBucket<String> bucket = redissonClient.getBucket(key);

            if (!RedisKeyEnum.PRE_SIGN_IN_NAME.equals(redisKeyEnum)) {
                CodeUtil.checkCode(code, bucket.get()); // 检查 code是否正确
            }

            // 执行：账号注销
            doSignDelete(CollUtil.newHashSet(currentUserIdNotAdmin));

            if (!RedisKeyEnum.PRE_SIGN_IN_NAME.equals(redisKeyEnum)) {
                bucket.delete(); // 删除：验证码
            }

            return BaseBizCodeEnum.OK;
        });
    }

    /**
     * 执行：账号注销
     */
    public static void doSignDelete(Set<Long> idSet) {

        sysUserMapper.deleteBatchIds(idSet); // 直接：删除用户

        doSignDeleteSub(idSet, true); // 删除子表数据

    }

    /**
     * 执行：账号注销，删除子表数据
     */
    public static void doSignDeleteSub(Set<Long> idSet, boolean removeUserInfoFlag) {

        if (removeUserInfoFlag) {
            // 直接：删除用户基本信息
            ChainWrappers.lambdaUpdateChain(sysUserInfoMapper).in(SysUserInfoDO::getId, idSet).remove();
        }

        // 直接：删除用户绑定的角色
        ChainWrappers.lambdaUpdateChain(sysRoleRefUserMapper).in(SysRoleRefUserDO::getUserId, idSet).remove();

    }

    /**
     * 绑定登录账号
     */
    public static String bindAccount(String code, RedisKeyEnum redisKeyEnum, String account) {

        Long currentUserIdNotAdmin = AuthUserUtil.getCurrentUserIdNotAdmin();

        String key = redisKeyEnum + account;

        return RedissonUtil.doLock(key, () -> {

            RBucket<String> bucket = redissonClient.getBucket(key);

            // 检查：绑定的登录账号是否存在
            boolean exist = accountIsExist(redisKeyEnum, account, null);
            if (exist) {
                bucket.delete();
                ApiResultVO.error("操作失败：账号已被绑定，请重试");
            }

            CodeUtil.checkCode(code, bucket.get()); // 检查 code是否正确

            SysUserDO sysUserDO = new SysUserDO();

            // 通过：RedisKeyEnum，设置：账号
            setSysUserDOAccountByRedisKeyEnum(redisKeyEnum, account, sysUserDO);

            sysUserDO.setId(currentUserIdNotAdmin);
            sysUserDO.setJwtSecretSuf(IdUtil.simpleUUID());

            sysUserMapper.updateById(sysUserDO); // 保存：用户

            bucket.delete(); // 删除：验证码

            return BaseBizCodeEnum.OK;
        });
    }

    /**
     * 获取默认的用户名
     * 备注：不使用邮箱的原因，因为邮箱不符合 用户昵称的规则：只能包含中文，数字，字母，下划线，长度2-20
     */
    public static String getRandomNickname() {
        return "用户昵称" + RandomUtil.randomStringUpper(6);
    }

    /**
     * 检查等级
     */
    public static void checkSignLevel(int signLevel) {
        boolean anyMatch =
            abstractSignHelperSecurityPermitAllConfigurationList.stream().anyMatch(it -> it.getSignLevel() > signLevel);
        if (anyMatch) {
            ApiResultVO.error(BaseBizCodeEnum.ILLEGAL_REQUEST);
        }
    }

}
