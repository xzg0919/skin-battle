package com.tzj.collect.controller.admin;

import com.tzj.collect.core.service.GasLeakageCheckService;
import com.tzj.collect.entity.GasLeakageCheck;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * @Auther: xiangzhongguo
 * @Date: 2021/12/7 09:31
 * @Description:
 */
@RestController
@RequestMapping("/gasLeakageCheck")
public class GasLeakageCheckController {

    @Autowired
    GasLeakageCheckService gasLeakageCheckService;


    @PostMapping("saveInfo")
    public String saveInfo(String tel,String address,String contactName){

        if(StringUtils.isBlank(tel)){
            return "请填写手机号码！";
        }

        if(StringUtils.isBlank(address)){
            return "请填写地址！";
        }

        if(StringUtils.isBlank(contactName)){
            return "请填写姓名！";
        }

        GasLeakageCheck gasLeakageCheck=new GasLeakageCheck();
        gasLeakageCheck.setAddress(address);
        gasLeakageCheck.setContactName(contactName);
        gasLeakageCheck.setTel(tel);
        gasLeakageCheckService.insert(gasLeakageCheck);

        return "提交成功";
    }
}
