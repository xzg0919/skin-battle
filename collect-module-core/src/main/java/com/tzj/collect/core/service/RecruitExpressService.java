package com.tzj.collect.core.service;

import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.core.param.ali.RecruitExpressBean;
import com.tzj.collect.entity.RecruitExpress;

public interface RecruitExpressService extends IService<RecruitExpress> {


    Object recruitExpressSave(RecruitExpressBean recruitExpressBean);

}
