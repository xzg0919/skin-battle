package com.tzj.collect.core.service;

import com.baomidou.mybatisplus.service.IService;

import com.tzj.collect.core.param.enterprise.EnterpriseTerminalBean;
import com.tzj.collect.entity.EnterpriseTerminal;

public interface EnterpriseTerminalService extends IService<EnterpriseTerminal> {


    /**
     * 更改/新增以旧换新的销售终端
     * 王灿
     * @param enterpriseTerminalBean
     * @return
     */
   Object updateEnterpriseTerminal(EnterpriseTerminalBean enterpriseTerminalBean, Integer enterpriseId)throws Exception;
}
