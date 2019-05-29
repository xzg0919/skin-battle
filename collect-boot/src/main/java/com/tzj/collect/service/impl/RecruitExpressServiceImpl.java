package com.tzj.collect.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.api.ali.param.RecruitExpressBean;
import com.tzj.collect.entity.RecruitExpress;
import com.tzj.collect.mapper.RecruitExpressMapper;
import com.tzj.collect.service.RecruitExpressService;
import com.tzj.module.easyopen.exception.ApiException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(readOnly=true)
public class RecruitExpressServiceImpl extends ServiceImpl<RecruitExpressMapper, RecruitExpress> implements RecruitExpressService {

    @Autowired
    private RecruitExpressMapper recruitExpressMapper;

    @Transactional
    @Override
    public Object recruitExpressSave(RecruitExpressBean recruitExpressBean){
        if("0".equals(recruitExpressBean.getCooperationType())&& StringUtils.isBlank(recruitExpressBean.getEnterprise())){
            throw  new ApiException("当选中企业时，企业名不能为空");
        }
        RecruitExpress recruitExpress = new RecruitExpress();
        recruitExpress.setType(recruitExpressBean.getType());
        recruitExpress.setCooperationType(recruitExpressBean.getCooperationType());
        recruitExpress.setEnterprise(recruitExpressBean.getEnterprise());
        recruitExpress.setName(recruitExpressBean.getName());
        recruitExpress.setMobile(recruitExpressBean.getMobile());
        recruitExpress.setCity(recruitExpressBean.getCity());
        recruitExpress.setCategoryType(recruitExpressBean.getCategoryType());
        this.insert(recruitExpress);
        return "操作成功";
    }
}
