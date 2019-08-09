package com.tzj.collect.core.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.core.mapper.RecruitExpressMapper;
import com.tzj.collect.core.param.ali.RecruitExpressBean;
import com.tzj.collect.core.service.AsyncService;
import com.tzj.collect.core.service.RecruitExpressService;
import com.tzj.collect.entity.RecruitExpress;
import com.tzj.module.easyopen.exception.ApiException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(readOnly=true)
public class RecruitExpressServiceImpl extends ServiceImpl<RecruitExpressMapper, RecruitExpress> implements RecruitExpressService {

    //招募钉钉通知
    public static final String recruitDingDing ="https://oapi.dingtalk.com/robot/send?access_token=78cd495bcf1bd047590f5b67ad1d1b9a7fef9a0eeaf5d65b322ecf36125b8895";

    @Autowired
    private RecruitExpressMapper recruitExpressMapper;
    @Autowired
    private AsyncService asyncService;

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
        if(this.insert(recruitExpress)){
            //发送钉钉消息
            StringBuffer message = new StringBuffer();
            message.append("您有新的招募信息了！\r\n");
            asyncService.notifyDingDingOrderCreate(message.toString(), true, recruitDingDing);
        }
        return "操作成功";
    }
}
