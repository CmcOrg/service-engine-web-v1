package com.cmcorg.service.engine.web.sign.wx.service;

import com.cmcorg.service.engine.web.sign.wx.model.dto.SignInCodeDTO;
import com.cmcorg.service.engine.web.sign.wx.model.dto.SignInPhoneCodeDTO;

public interface SignWxService {

    String signInPhoneCode(SignInPhoneCodeDTO dto);

    String signInCode(SignInCodeDTO dto);

}
