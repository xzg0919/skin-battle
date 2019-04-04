package com.tzj.collect.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.taobao.api.ApiException;
import com.tzj.collect.api.enterprise.param.EnterpriseTerminalBean;
import com.tzj.collect.entity.EnterpriseTerminal;
import com.tzj.collect.mapper.EnterpriseTerminalMapper;
import com.tzj.collect.service.EnterpriseTerminalService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class EnterpriseTerminalServiceImpl extends ServiceImpl<EnterpriseTerminalMapper,EnterpriseTerminal> implements EnterpriseTerminalService{

    @Autowired
    private EnterpriseTerminalMapper enterpriseTerminalMapper;

    /**
     * 更改/新增以旧换新的销售终端
     * 王灿
     * @param enterpriseTerminalBean
     * @return
     */
    @Override
    @Transactional
    public Object updateEnterpriseTerminal(EnterpriseTerminalBean enterpriseTerminalBean, Integer enterpriseId) throws ApiException{

        EnterpriseTerminal enterpriseTerminal;
        if(StringUtils.isBlank(enterpriseTerminalBean.getId())){
            //新增
            enterpriseTerminal = new EnterpriseTerminal();
        }else{
            //修改
            enterpriseTerminal = this.selectById(enterpriseTerminalBean.getId());
        }
        EnterpriseTerminal enterpriseTerminal1 = this.selectOne(new EntityWrapper<EnterpriseTerminal>().eq("tel", enterpriseTerminalBean.getTel()).eq("del_flag", 0));
        if (null != enterpriseTerminal1&&!(enterpriseTerminal1.getId()+"").equals(enterpriseTerminalBean.getId())) {
            throw new ApiException("该手机号已被注册");
        }
        enterpriseTerminal.setAddress(enterpriseTerminalBean.getAddress());
        enterpriseTerminal.setContacts(enterpriseTerminalBean.getContacts());
        enterpriseTerminal.setEnterpriseId(enterpriseId);
        enterpriseTerminal.setName(enterpriseTerminalBean.getName());
        enterpriseTerminal.setTel(enterpriseTerminalBean.getTel());
        enterpriseTerminal.setPassword(enterpriseTerminalBean.getPassword());
        try{
            if(StringUtils.isBlank(enterpriseTerminalBean.getId())){
               this.insert(enterpriseTerminal);
            }else{
                //修改
               this.updateById(enterpriseTerminal);
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new ApiException("操作失败");
        }
        return  "操作成功";
    }

    public static void main(String[] args) {

        System.out.println((1+"").equals("1"));
    }
}
