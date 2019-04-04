package com.tzj.collect.service;

import com.baomidou.mybatisplus.service.IService;
import com.taobao.api.ApiException;
import com.tzj.collect.api.enterprise.param.EnterpriseTerminalBean;
import com.tzj.collect.entity.EnterpriseTerminal;

public interface EnterpriseTerminalService extends IService<EnterpriseTerminal> {


    /**
     * 更改/新增以旧换新的销售终端
     * 王灿
     * @param enterpriseTerminalBean
     * @return
     */
   Object updateEnterpriseTerminal(EnterpriseTerminalBean enterpriseTerminalBean, Integer enterpriseId)throws ApiException;
}
