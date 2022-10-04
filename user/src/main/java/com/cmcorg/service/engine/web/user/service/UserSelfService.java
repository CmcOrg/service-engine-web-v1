package com.cmcorg.service.engine.web.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cmcorg.engine.web.auth.model.entity.SysUserDO;
import com.cmcorg.service.engine.web.user.model.dto.UserSelfUpdateInfoDTO;
import com.cmcorg.service.engine.web.user.model.vo.UserSelfInfoVO;

public interface UserSelfService extends IService<SysUserDO> {

    UserSelfInfoVO userSelfInfo();

    String userSelfUpdateInfo(UserSelfUpdateInfoDTO dto);

    String userSelfRefreshJwtSecretSuf();

}
