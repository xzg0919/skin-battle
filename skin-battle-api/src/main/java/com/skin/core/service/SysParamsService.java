package com.skin.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.skin.entity.SysParams;

import java.util.List;

public interface SysParamsService extends IService<SysParams> {


    SysParams getSysParams(String param);


    List<SysParams> getSysParamsList(String param);


    SysParams getSysParams(String param, String val);

}
