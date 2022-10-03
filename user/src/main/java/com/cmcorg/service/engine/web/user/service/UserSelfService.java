package com.cmcorg.service.engine.web.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cmcorg.engine.web.auth.model.entity.SysUserDO;
import com.cmcorg.service.engine.web.user.model.dto.UserSelfUpdateBaseInfoDTO;
import com.cmcorg.service.engine.web.user.model.vo.UserSelfBaseInfoVO;

public interface UserSelfService extends IService<SysUserDO> {

    UserSelfBaseInfoVO userSelfBaseInfo();

    String userSelfUpdateBaseInfo(UserSelfUpdateBaseInfoDTO dto);

    String userSelfRefreshJwtSecretSuf();

}
