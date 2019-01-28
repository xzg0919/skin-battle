package com.tzj.collect.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.taobao.api.ApiException;
import com.tzj.collect.api.picc.param.PiccInsurancePolicyAgreementBean;
import com.tzj.collect.api.picc.param.PiccInsurancePolicyBean;
import com.tzj.collect.api.picc.param.PiccInsurancePolicyContentBean;
import com.tzj.collect.entity.*;
import com.tzj.collect.mapper.PiccInsurancePolicyMapper;
import com.tzj.collect.service.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class PiccInsurancePolicyServiceImpl extends ServiceImpl<PiccInsurancePolicyMapper,PiccInsurancePolicy> implements PiccInsurancePolicyService {

    @Autowired
    private PiccInsurancePolicyMapper piccInsurancePolicyMapper;
    @Autowired
    private PiccInsurancePolicyContentService piccInsurancePolicyContentService;
    @Autowired
    private PiccInsurancePolicyAgreementService piccInsurancePolicyAgreementService;
    @Autowired
    private PiccOrderService piccOrderService;
    @Autowired
    private PiccWaterService piccWaterService;


    @Transactional
    @Override
    public Object updateInsurance(Long piccCompanyId, PiccInsurancePolicyBean piccInsurancePolicyBean) throws ApiException {
        PiccInsurancePolicy piccInsurancePolicy = null;
        //判断产品是新增还是修改
        if (StringUtils.isBlank(piccInsurancePolicyBean.getId())){
            piccInsurancePolicy = new PiccInsurancePolicy();
        }else{
            piccInsurancePolicy = this.selectById(piccInsurancePolicyBean.getId());
            if(piccInsurancePolicy ==null){
               throw  new  ApiException("传入的保险Id有误");
            }
            //将所有相关的内容删除
            piccInsurancePolicyContentService.delete(new EntityWrapper<PiccInsurancePolicyContent>().eq("insurance_id", piccInsurancePolicy.getId()).eq("del_flag", 0));
            //删除所有相关协议
            piccInsurancePolicyAgreementService.delete(new EntityWrapper<PiccInsurancePolicyAgreement>().eq("insurance_id", piccInsurancePolicy.getId()).eq("del_flag", 0));
        }
        piccInsurancePolicy.setPiccCompanyId(piccCompanyId.intValue());
        piccInsurancePolicy.setTitle(piccInsurancePolicyBean.getTitle());
        piccInsurancePolicy.setInitPrice(piccInsurancePolicyBean.getInitPrice());
        piccInsurancePolicy.setUnderwritingPrice(piccInsurancePolicyBean.getUnderwritingPrice());
        try {
            this.insertOrUpdate(piccInsurancePolicy);
        }catch (Exception e){
            throw new ApiException("储存保险失败");
        }
        //取出保障内容并储存
        List<PiccInsurancePolicyContentBean> piccInsurancePolicyContentBeanList = piccInsurancePolicyBean.getPiccInsurancePolicyContentBeanList();
        if (null!=piccInsurancePolicyContentBeanList){
            for (PiccInsurancePolicyContentBean piccInsurancePolicyContentBean:piccInsurancePolicyContentBeanList) {
                if(null!=piccInsurancePolicyContentBean){
                    PiccInsurancePolicyContent piccInsurancePolicyContent = new PiccInsurancePolicyContent();
                    piccInsurancePolicyContent.setInsuranceId(piccInsurancePolicy.getId().intValue());
                    piccInsurancePolicyContent.setContent(piccInsurancePolicyContentBean.getContent());
                    piccInsurancePolicyContent.setInsurancePrice(piccInsurancePolicyContentBean.getInsurancePrice());
                    try {
                        piccInsurancePolicyContentService.insertOrUpdate(piccInsurancePolicyContent);
                    }catch (Exception e){
                        throw new ApiException("储存保险内容失败");
                    }
                }
            }
        }
        //取出保障协议并储存
        List<PiccInsurancePolicyAgreementBean> piccInsurancePolicyAgreementBeanList = piccInsurancePolicyBean.getPiccInsurancePolicyAgreementBeanList();
        if(null!=piccInsurancePolicyAgreementBeanList){
            for (PiccInsurancePolicyAgreementBean piccInsurancePolicyAgreementBean:piccInsurancePolicyAgreementBeanList) {
                if(null!=piccInsurancePolicyAgreementBean){
                    PiccInsurancePolicyAgreement piccInsurancePolicyAgreement = new PiccInsurancePolicyAgreement();
                    piccInsurancePolicyAgreement.setInsuranceId(piccInsurancePolicy.getId().intValue());
                    piccInsurancePolicyAgreement.setAgreementName(piccInsurancePolicyAgreementBean.getAgreementName());
                    piccInsurancePolicyAgreement.setAgreementUrl(piccInsurancePolicyAgreementBean.getAgreementUrl());
                    try {
                        piccInsurancePolicyAgreementService.insertOrUpdate(piccInsurancePolicyAgreement);
                    }catch (Exception e){
                        throw new ApiException("储存保险协议失败");
                    }
                }
            }
        }
        return "操作成功";
    }

    @Override
    public Object getInsurancePolicy(long piccCompanyId) {
        Map<String,Object> map = new HashMap<>();
        PiccInsurancePolicy piccInsurancePolicy = this.selectOne(new EntityWrapper<PiccInsurancePolicy>().eq("picc_company_id", piccCompanyId).eq("del_flag", 0));
        if(null!=piccInsurancePolicy){
            List<PiccInsurancePolicyContent> piccInsurancePolicyContents = piccInsurancePolicyContentService.selectList(new EntityWrapper<PiccInsurancePolicyContent>().eq("insurance_id", piccInsurancePolicy.getId()).eq("del_flag", 0));
            List<PiccInsurancePolicyAgreement> piccInsurancePolicyAgreements = piccInsurancePolicyAgreementService.selectList(new EntityWrapper<PiccInsurancePolicyAgreement>().eq("insurance_id", piccInsurancePolicy.getId()).eq("del_flag", 0));
            map.put("InsurancePolicy",piccInsurancePolicy);
            map.put("InsuranceContentsList",piccInsurancePolicyContents);
            map.put("InsuranceAgreementsList",piccInsurancePolicyAgreements);
        }
        return  map;

    }

    @Override
    public Object insuranceDetal(Integer memberId, Integer insuranceId) {
        //根据保单ID查询保单信息
        PiccInsurancePolicy piccInsurancePolicy = this.selectById(insuranceId);
        List<PiccInsurancePolicyContent> contentList = piccInsurancePolicyContentService.selectList(new EntityWrapper<PiccInsurancePolicyContent>().eq("insurance_id", insuranceId).eq("del_flag", 0));
        List<PiccInsurancePolicyAgreement> agreementList = piccInsurancePolicyAgreementService.selectList(new EntityWrapper<PiccInsurancePolicyAgreement>().eq("insurance_id", insuranceId).eq("del_flag", 0));
        //查询用户保单对应的信息
        PiccOrder piccOrder = piccOrderService.selectOne(new EntityWrapper<PiccOrder>().eq("insurance_id", insuranceId).eq("member_id", memberId).eq("status_", 2).eq("del_flag", 0));
        //查询用户存在未领取的环保能量
        List<PiccWater> piccWaterList = piccWaterService.selectList(new EntityWrapper<PiccWater>().eq("member_id", memberId).eq("del_flag", 0).eq("status_", 0).ge("point_count", 1).last(" LIMIT 0,6"));
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("piccInsurancePolicy",piccInsurancePolicy);
        resultMap.put("contentList",contentList);
        resultMap.put("agreementList",agreementList);
        resultMap.put("piccOrder",piccOrder);
        resultMap.put("piccWaterList",piccWaterList);
        return resultMap;
    }

}
