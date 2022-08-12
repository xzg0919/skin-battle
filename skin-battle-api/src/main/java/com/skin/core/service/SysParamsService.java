package com.skin.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.skin.entity.SysParams;

public interface SysParamsService extends IService<SysParams> {


    SysParams getSysParams(String param);
}
