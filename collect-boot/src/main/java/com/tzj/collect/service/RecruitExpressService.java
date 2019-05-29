package com.tzj.collect.service;

import com.baomidou.mybatisplus.service.IService;
import com.taobao.api.ApiException;
import com.tzj.collect.api.ali.param.RecruitExpressBean;
import com.tzj.collect.entity.CompanyStreetBig;
import com.tzj.collect.entity.RecruitExpress;

public interface RecruitExpressService extends IService<RecruitExpress> {


    Object recruitExpressSave(RecruitExpressBean recruitExpressBean);

}
