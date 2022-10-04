package com.cmcorg.service.engine.web.user.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cmcorg.engine.web.auth.model.entity.SysUserDO;
import com.cmcorg.engine.web.model.model.dto.NotEmptyIdSet;
import com.cmcorg.engine.web.model.model.dto.NotNullId;
import com.cmcorg.engine.web.model.model.vo.DictLongListVO;
import com.cmcorg.service.engine.web.user.model.dto.SysUserDictListDTO;
import com.cmcorg.service.engine.web.user.model.dto.SysUserInsertOrUpdateDTO;
import com.cmcorg.service.engine.web.user.model.dto.SysUserPageDTO;
import com.cmcorg.service.engine.web.user.model.dto.SysUserUpdatePasswordDTO;
import com.cmcorg.service.engine.web.user.model.vo.SysUserInfoByIdVO;
import com.cmcorg.service.engine.web.user.model.vo.SysUserPageVO;

public interface SysUserService extends IService<SysUserDO> {

    Page<SysUserPageVO> myPage(SysUserPageDTO dto);

    Page<DictLongListVO> dictList(SysUserDictListDTO dto);

    String insertOrUpdate(SysUserInsertOrUpdateDTO dto);

    String deleteByIdSet(NotEmptyIdSet notEmptyIdSet);

    SysUserInfoByIdVO infoById(NotNullId notNullId);

    String refreshJwtSecretSuf(NotEmptyIdSet notEmptyIdSet, String password);

    String resetAvatar(NotEmptyIdSet notEmptyIdSet);

    String updatePassword(SysUserUpdatePasswordDTO dto);

}
