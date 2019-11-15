package com.tzj.collect.core.service;

import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.entity.BindAxn;

public interface BindAxnService extends IService<BindAxn> {

    public void updateBySubsId(String subId,String secretNo);

    String getAxnMobile(String mobile,String tel);
}
