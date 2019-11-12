package com.tzj.collect.core.service.impl;

import com.aliyuncs.dyplsapi.model.v20170525.BindAxnResponse;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.core.mapper.BindAxnMapper;
import com.tzj.collect.core.service.AliBindAxnService;
import com.tzj.collect.core.service.BindAxnService;
import com.tzj.collect.entity.BindAxn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BindAxnServiceImpl extends ServiceImpl<BindAxnMapper, BindAxn> implements BindAxnService {

    @Autowired
    private AliBindAxnService aliBindAxnService;

    @Transactional
    @Override
    public void updateBySubsId(String subId,String secretNo){

        BindAxn bindAxn  = this.selectOne(new EntityWrapper<BindAxn>().eq("subs_id", subId).eq("secret_no", secretNo));
             bindAxn.setStatus("1");
        this.updateById(bindAxn);
    }

    @Override
    @Transactional
    public String getAxnMobile(String mobile, String tel) {
        BindAxnResponse response = aliBindAxnService.getAxnPhone(mobile, tel);
        if ("OK".equals(response.getCode())){
            BindAxnResponse.SecretBindDTO secretBindDTO = response.getSecretBindDTO();
            BindAxn bindAxn = new BindAxn();
            bindAxn.setStatus("0");
            bindAxn.setPhoneNoA(mobile);
            bindAxn.setPhoneNoB(tel);
            bindAxn.setSecretNo(secretBindDTO.getSecretNo());
            bindAxn.setSubsId(secretBindDTO.getSubsId());
            this.insert(bindAxn);
            return secretBindDTO.getSecretNo();
        }
        return "获取号码异常";
    }


}
