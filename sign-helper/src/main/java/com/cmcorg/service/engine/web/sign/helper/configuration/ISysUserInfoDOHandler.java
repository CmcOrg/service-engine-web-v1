package com.cmcorg.service.engine.web.sign.helper.configuration;

import com.cmcorg.service.engine.web.sign.helper.model.dto.UserSelfUpdateInfoDTO;

public interface ISysUserInfoDOHandler {

    /**
     * 当前用户：基本信息：修改
     */
    void userSelfUpdateInfo(UserSelfUpdateInfoDTO dto);

    /**
     * 新增用户信息
     */
    void insertUserInfo(Long id, String nickname, String bio, String avatarUri);

    /**
     * 修改用户信息
     */
    void updateUserInfo(Long id, String nickname, String bio, String avatarUri);

}
