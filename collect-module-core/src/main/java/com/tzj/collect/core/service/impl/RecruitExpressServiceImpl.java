package com.tzj.collect.core.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.core.mapper.RecruitExpressMapper;
import com.tzj.collect.core.param.ali.RecruitExpressBean;
import com.tzj.collect.core.result.admin.RecruitExpressResult;
import com.tzj.collect.core.service.AsyncService;
import com.tzj.collect.core.service.RecruitExpressService;
import com.tzj.collect.entity.RecruitExpress;
import com.tzj.module.easyopen.exception.ApiException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
    @Override
    public Object getRecruitList(RecruitExpressBean recruitExpressBean) {
        Integer startPage = (recruitExpressBean.getPageBean().getPageNumber()-1)*recruitExpressBean.getPageBean().getPageSize();
        Integer pageSize = recruitExpressBean.getPageBean().getPageSize();
        List<RecruitExpressResult> recruitList = recruitExpressMapper.getRecruitList(recruitExpressBean.getType(), recruitExpressBean.getCooperationType(), recruitExpressBean.getEnterprise(), recruitExpressBean.getCity(), recruitExpressBean.getMobile(), recruitExpressBean.getStartTime(), recruitExpressBean.getEndTime(), startPage, pageSize);
        Integer recruitCount = recruitExpressMapper.getRecruitCount(recruitExpressBean.getType(), recruitExpressBean.getCooperationType(), recruitExpressBean.getEnterprise(), recruitExpressBean.getCity(), recruitExpressBean.getMobile(), recruitExpressBean.getStartTime(), recruitExpressBean.getEndTime());
        Map<String,Object> resultMap = new HashMap<>();
        Map<String,Object> pagination = new HashMap<>();
        pagination.put("current",recruitExpressBean.getPageBean().getPageNumber());
        pagination.put("pageSize",recruitExpressBean.getPageBean().getPageSize());
        pagination.put("total",recruitCount);
        resultMap.put("pagination",pagination);
        resultMap.put("orderList",recruitList);
        return resultMap;
    }

    @Override
    public List<RecruitExpressResult> getRecruitListOutExcel(RecruitExpressBean recruitExpressBean) {
        List<RecruitExpressResult> recruitList = recruitExpressMapper.getRecruitListOutExcel(recruitExpressBean.getType(), recruitExpressBean.getCooperationType(), recruitExpressBean.getEnterprise(), recruitExpressBean.getCity(), recruitExpressBean.getMobile(), recruitExpressBean.getStartTime(), recruitExpressBean.getEndTime());
        return recruitList;
    }
}
