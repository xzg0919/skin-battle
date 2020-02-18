package com.tzj.iot.api.equipment.app;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.tzj.collect.core.param.iot.EquipmentParamBean;
import com.tzj.collect.core.param.token.TokenBean;
import com.tzj.collect.core.service.CompanyEquipmentService;
import com.tzj.collect.core.service.MessageService;
import com.tzj.collect.entity.CompanyEquipment;
import com.tzj.module.api.annotation.*;
import com.tzj.module.api.entity.Subject;
import com.tzj.module.api.utils.JwtUtils;
import com.tzj.module.easyopen.ApiContext;

import com.tzj.module.easyopen.exception.ApiException;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Resource;
import java.util.Date;

import static com.tzj.collect.common.constant.TokenConst.*;

/**
 * iot 设备token api
 * @author: sgmark@aliyun.com
 * @Date: 2019/11/13 0013
 * @Param: 
 * @return: 
 */
@ApiService

public class EquipmentAppTokenApi {

    @Resource
    private CompanyEquipmentService companyEquipmentService;
    @Resource
    private MessageService messageService;
    /**
     * 得到code
     * @return
     */
    @Api(name = "equipment.message.getcode", version = "1.0")
    @AuthIgnore
    public String getMessageCode(EquipmentParamBean equipmentParamBean) {
        CompanyEquipment companyEquipment = companyEquipmentService.selectOne(new EntityWrapper<CompanyEquipment>().eq("hardware_code", equipmentParamBean.getHardwareCode()).eq("del_flag", 0));
        if (null != companyEquipment){
            return messageService.getMessageCode(companyEquipment.getActivateTel());
        }else {
            throw new ApiException("设备不可用");
        }
    }
    /**
     * 回收人员手机登录验证后获取token
     * 忽略token验证，需要sign签名验证
     *
     * @param equipmentParamBean
     * @return
     */
    @Api(name = "equipment.token.get", version = "1.0")
    @SignIgnore
    @AuthIgnore //这个api忽略token验证
    public TokenBean getToken(EquipmentParamBean equipmentParamBean) {
        CompanyEquipment companyEquipment = null;
        if(StringUtils.isBlank(equipmentParamBean.getCaptcha())){
            //根据设备硬件号进行登录
            companyEquipment = companyEquipmentService.selectOne(new EntityWrapper<CompanyEquipment>().eq("hardware_code", equipmentParamBean.getHardwareCode()).eq("is_activated", "1").eq("del_flag", 0));
            if(companyEquipment==null){
                throw new ApiException("设备未激活");
            }else{
                String token = JwtUtils.generateToken(companyEquipment.getId().toString(), EQUIPMENT_APP_API_EXPRIRE, EQUIPMENT_APP_API_TOKEN_SECRET_KEY);
                String securityToken = JwtUtils.generateEncryptToken(token, EQUIPMENT_APP_API_TOKEN_CYPTO_KEY);
                System.out.println("返回回收人员的token是 ："+securityToken);
                TokenBean tokenBean = new TokenBean();
                tokenBean.setExpire(EQUIPMENT_APP_API_EXPRIRE);
                tokenBean.setToken(securityToken);
                return tokenBean;
            }

        }else{
            companyEquipment = companyEquipmentService.selectOne(new EntityWrapper<CompanyEquipment>().eq("hardware_code", equipmentParamBean.getHardwareCode()).eq("del_flag", 0));
            //先去验证手机与验证码！！！
            if(messageService.validMessage(companyEquipment.getActivateTel(),equipmentParamBean.getCaptcha())){
                //更改设备激活状态
                companyEquipment.setIsActivated("1");
                companyEquipment.setEnablingTime(new Date());
                companyEquipmentService.updateById(companyEquipment);
                String token = JwtUtils.generateToken(companyEquipment.getId().toString(), EQUIPMENT_APP_API_EXPRIRE, EQUIPMENT_APP_API_TOKEN_SECRET_KEY);
                String securityToken = JwtUtils.generateEncryptToken(token, EQUIPMENT_APP_API_TOKEN_CYPTO_KEY);
                System.out.println("返回回收人员的token是 ："+securityToken);
                TokenBean tokenBean = new TokenBean();
                tokenBean.setExpire(EQUIPMENT_APP_API_EXPRIRE);
                tokenBean.setToken(securityToken);
                return tokenBean;
            }else{
                throw new ApiException("验证码错误");
            }
        }
    }
    /**
     * 刷新token
     * 需要token验证，忽略sign签名验证
     * 需要 ALI_API_COMMON_AUTHORITY 权限
     *
     * @return
     */
    @Api(name = "equipment.token.flush", version = "1.0")
    @RequiresPermissions(values = EQUIPMENT_APP_API_COMMON_AUTHORITY)
    public TokenBean flushToken() {

    	Subject subject=ApiContext.getSubject();

        String token = JwtUtils.generateToken(subject.getId(), EQUIPMENT_APP_API_EXPRIRE, EQUIPMENT_APP_API_TOKEN_SECRET_KEY);
        String securityToken = JwtUtils.generateEncryptToken(token, EQUIPMENT_APP_API_TOKEN_CYPTO_KEY);

        TokenBean tokenBean = new TokenBean();
        tokenBean.setExpire(EQUIPMENT_APP_API_EXPRIRE);
        tokenBean.setToken(securityToken);
        return tokenBean;
    }

}
