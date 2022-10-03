package com.cmcorg.service.engine.web.param.model.dto;

import com.cmcorg.engine.web.auth.model.dto.MyPageDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SysParamPageDTO extends MyPageDTO {

    @Schema(description = "配置名，以 id为不变值进行使用，不要用此属性")
    private String name;

    @Schema(description = "是否启用")
    private Boolean enableFlag;

    @Schema(description = "备注")
    private String remark;

}
