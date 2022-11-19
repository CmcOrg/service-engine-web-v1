package com.cmcorg.service.engine.web.sign.wx.service.impl;

import com.cmcorg.engine.web.wx.model.vo.WxGetPhoneByCodeVO;
import com.cmcorg.engine.web.wx.util.WxUtil;
import com.cmcorg.service.engine.web.sign.wx.model.dto.SignInPhoneCodeDTO;
import com.cmcorg.service.engine.web.sign.wx.service.SignWxService;
import org.springframework.stereotype.Service;

@Service
public class SignWxServiceImpl implements SignWxService {

    /**
     * 手机号 code登录
     */
    @Override
    public String signInPhoneCode(SignInPhoneCodeDTO dto) {

        // 获取：用户手机号
        WxGetPhoneByCodeVO.WxPhoneInfoVO wxPhoneInfoVO = WxUtil.getWxPhoneInfoVOByCode(dto.getPhoneCode());

        String phone = wxPhoneInfoVO.getPhoneNumber();

        // 存在则，直接登录，不存在则，注册之后，直接登录

        return null;
    }

}
