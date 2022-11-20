package com.cmcorg.service.engine.web.sign.wx.service.impl;

import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import com.cmcorg.engine.web.auth.mapper.SysUserMapper;
import com.cmcorg.engine.web.auth.model.entity.SysUserDO;
import com.cmcorg.engine.web.redisson.enums.RedisKeyEnum;
import com.cmcorg.engine.web.wx.model.vo.WxOpenIdVO;
import com.cmcorg.engine.web.wx.model.vo.WxPhoneByCodeVO;
import com.cmcorg.engine.web.wx.util.WxUtil;
import com.cmcorg.service.engine.web.sign.helper.util.SignUtil;
import com.cmcorg.service.engine.web.sign.wx.model.dto.SignInCodeDTO;
import com.cmcorg.service.engine.web.sign.wx.model.dto.SignInPhoneCodeDTO;
import com.cmcorg.service.engine.web.sign.wx.service.SignWxService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class SignWxServiceImpl implements SignWxService {

    @Resource
    SysUserMapper sysUserMapper;

    /**
     * 手机号 code登录
     */
    @Override
    public String signInPhoneCode(SignInPhoneCodeDTO dto) {

        // 获取：用户手机号
        WxPhoneByCodeVO.WxPhoneInfoVO wxPhoneInfoVO = WxUtil.getWxPhoneInfoVOByCode(dto.getPhoneCode());

        // 直接通过：手机号登录
        return SignUtil.signInAccount(
            ChainWrappers.lambdaQueryChain(sysUserMapper).eq(SysUserDO::getPhone, wxPhoneInfoVO.getPhoneNumber()),
            RedisKeyEnum.PRE_PHONE, wxPhoneInfoVO.getPhoneNumber());

    }

    /**
     * 微信 code登录
     */
    @Override
    public String signInCode(SignInCodeDTO dto) {

        WxOpenIdVO wxOpenIdVO = WxUtil.getWxOpenIdVOByCode(dto.getCode());

        // 直接通过：微信 openId登录
        return SignUtil.signInAccount(
            ChainWrappers.lambdaQueryChain(sysUserMapper).eq(SysUserDO::getWxOpenId, wxOpenIdVO.getOpenid()),
            RedisKeyEnum.PRE_WX_OPEN_ID, wxOpenIdVO.getOpenid());

    }

}
