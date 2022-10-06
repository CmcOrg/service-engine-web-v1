package com.cmcorg.service.engine.web.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cmcorg.engine.web.auth.model.entity.SysUserDO;
import com.cmcorg.service.engine.web.user.model.dto.SysUserPageDTO;
import com.cmcorg.service.engine.web.user.model.vo.SysUserPageVO;
import org.apache.ibatis.annotations.Param;

public interface SysUserProMapper extends BaseMapper<SysUserDO> {

    // 分页排序查询
    Page<SysUserPageVO> myPage(@Param("page") Page<SysUserPageVO> page, @Param("dto") SysUserPageDTO dto);

}
