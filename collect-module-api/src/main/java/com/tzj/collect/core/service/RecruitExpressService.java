package com.tzj.collect.core.service;

import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.core.param.ali.RecruitExpressBean;
import com.tzj.collect.core.result.admin.RecruitExpressResult;
import com.tzj.collect.entity.RecruitExpress;

import java.util.List;

public interface RecruitExpressService extends IService<RecruitExpress> {


    Object recruitExpressSave(RecruitExpressBean recruitExpressBean);

    Object getRecruitList(RecruitExpressBean recruitExpressBean);

    List<RecruitExpressResult> getRecruitListOutExcel(RecruitExpressBean recruitExpressBean);

}
