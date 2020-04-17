package com.tzj.iot.api.equipment.app;

import com.alibaba.fastjson.JSONObject;
import com.tzj.collect.common.util.CompanyEquipmentUtils;
import com.tzj.collect.common.util.MemberUtils;
import com.tzj.collect.common.utils.ToolUtils;
import com.tzj.collect.core.param.ali.MemberBean;
import com.tzj.collect.core.param.iot.EquipmentParamBean;
import com.tzj.collect.core.param.iot.IotCompanyResult;
import com.tzj.collect.core.param.iot.IotErrorParamBean;
import com.tzj.collect.core.service.*;
import com.tzj.collect.core.service.impl.FileUploadServiceImpl;
import com.tzj.collect.entity.CompanyEquipment;
import com.tzj.collect.entity.Member;
import com.tzj.module.api.annotation.*;
import com.tzj.module.api.utils.JwtUtils;
import com.tzj.module.easyopen.exception.ApiException;
import com.tzj.module.easyopen.file.FileBase64Param;
import com.tzj.module.easyopen.file.FileBean;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.tzj.collect.common.constant.TokenConst.*;

/**
 * iot 设备 api
 * @author: sgmark@aliyun.com
 * @Date: 2019/11/13 0013
 * @Param: 
 * @return: 
 */
@ApiService
public class EquipmentAppApi {

    @Resource
    private EquipmentAdvertService advertService;
    @Resource
    private EquipmentErrorListService equipmentErrorListService;
    @Resource
    private EquipmentErrorCodeService equipmentErrorCodeService;
    @Resource
    private FileUploadServiceImpl fileUploadServiceImpl;
    @Resource
    private CompanyService companyService;
    @Resource
    private EquipmentMessageService equipmentMessageService;
    @Resource
    private MqttClient mqttClient;
    @Resource
    private MemberService memberService;

    /**
     * 广告位
     * @author: sgmark@aliyun.com
     * @Date: 2019/11/13 0013
     * @Param: 
     * @return: 
     */
    @Api(name = "equipment.advert", version = "1.0")
    @RequiresPermissions(values = EQUIPMENT_APP_API_COMMON_AUTHORITY)
    public List<Map<String, Object>> advertList() {
        return advertService.iotEquipmentAdvertList();
    }

    /**
     * 验证码验证开门
     * @author: sgmark@aliyun.com
     * @Date: 2019/12/24 0024
     * @Param: 
     * @return: 
     */
    @Api(name = "equipment.code.open", version = "1.0")
    @RequiresPermissions(values = EQUIPMENT_APP_API_COMMON_AUTHORITY)
    public Map<String, Object> equipmentCodeOpen(EquipmentParamBean equipmentParamBean) {
        if (StringUtils.isEmpty(equipmentParamBean.getHardwareCode())|| StringUtils.isEmpty(equipmentParamBean.getCaptcha())){
            throw new ApiException("参数错误");
        }
        return equipmentMessageService.equipmentCodeOpen(equipmentParamBean, mqttClient);
    }


    /**
     *  获取动态二维码
     * @author: sgmark@aliyun.com
     * @Date: 2019/12/10 0010
     * @Param: 
     * @return: 
     */
    @Api(name = "equipment.qrcode", version = "1.0")
    @RequiresPermissions(values = EQUIPMENT_APP_API_COMMON_AUTHORITY)
    public Map<String, Object> qrCode() {
        Map<String, Object> returnMap = new HashMap<>();
        CompanyEquipment equipment = CompanyEquipmentUtils.getCompanyEquipment();
        IotCompanyResult iotCompanyResult = companyService.selectIotUrlByEquipmentCode(equipment.getEquipmentCode());
        String aliUrl = "alipays://platformapi/startapp?appId=2018060660292753&page=pages/view/index/index&query=";
        String encodeString = URLEncoder.encode("tranTime="+ System.currentTimeMillis()+ "&ecUuid="+UUID.randomUUID().toString().substring(0,15)+ "&cabinetNo="+equipment.getEquipmentCode());
        returnMap.put("qrCode", aliUrl+URLEncoder.encode("qrCode=Y&type=appliance&sourceId="+equipment.getEquipmentCode()+"&qrUrl="+iotCompanyResult.getIotUrl()+"?"+encodeString));
        return returnMap;
    }

    public static void main(String[] args) {
        String aliUrl = "alipays://platformapi/startapp?appId=2018060660292753&page=pages/view/index/index&query=";
        String encodeString = URLEncoder.encode("tranTime="+ System.currentTimeMillis()+ "&ecUuid="+UUID.randomUUID().toString().substring(0,15)+ "&cabinetNo="+"SH0024");
        System.out.println(aliUrl+URLEncoder.encode("qrCode=Y&type=appliance&sourceId="+"SH0024"+"&qrUrl="+"http://apibox.weee-ec.cn/service/Api/Alipay.src"+"?"+encodeString));
    }


    /**
     * iot错误信息列表
     * @author: sgmark@aliyun.com
     * @Date: 2019/10/16 0016
     * @Param:
     * @return:
     */
    @Api(name = "equipment.error.code", version = "1.0")
    @RequiresPermissions(values = ALI_API_COMMON_AUTHORITY)
    public List<Map<String, Object>> iotErrorCode(IotErrorParamBean iotErrorParamBean){
        return equipmentErrorCodeService.iotErrorCode(iotErrorParamBean);
    }
    /**
     * 用户上传iot错误信息
     * @author: sgmark@aliyun.com
     * @Date: 2019/10/16 0016
     * @Param:
     * @return:
     */
    @Api(name = "equipment.error.list.member", version = "1.0")
    @RequiresPermissions(values = ALI_API_COMMON_AUTHORITY)
    public Map<String, Object>  iotErrorListByMember(IotErrorParamBean iotErrorParamBean){
        iotErrorParamBean.setMember(MemberUtils.getMember());
        return equipmentErrorListService.iotErrorListByMember(iotErrorParamBean);
    }
    /**
     * 上传文件0
     */
    @Api(name = "equipment.upload.image", version = "1.0")
    @RequiresPermissions(values = EQUIPMENT_APP_API_COMMON_AUTHORITY)
    public List<FileBean> uploadImage(List<FileBase64Param> fileBase64ParamLists){
        return fileUploadServiceImpl.uploadImageForIot(fileBase64ParamLists);
    }
    @Api(name = "equipment.open.door", version = "1.0")
    @SignIgnore
    @AuthIgnore
    public void openDoor(){
        Map<String, String> messageMap = new HashMap<>();
        messageMap.put("code", CompanyEquipment.EquipmentAction.EquipmentActionCode.RECYCLE_OPEN.getKey());
        messageMap.put("msg", CompanyEquipment.EquipmentAction.EquipmentActionCode.RECYCLE_OPEN.getValue());
        try {
            equipmentMessageService.sendMessageToMQ4IoTUseSignatureMode(JSONObject.toJSONString(messageMap), "869012040190428", mqttClient);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
    @Api(name = "equipment.getMemberByCardNo", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = EQUIPMENT_APP_API_COMMON_AUTHORITY)
    public Object getMemberByCardNo(MemberBean memberBean) throws MqttException {
        CompanyEquipment companyEquipment = CompanyEquipmentUtils.getCompanyEquipment();
        Map<String,Object> resultMap = new HashMap<String,Object>();
        String aliUserId = ToolUtils.getAliUserIdByOrderNo(memberBean.getCardNo());
        Member member = memberService.selectMemberByAliUserId(aliUserId);
        if(member==null) {
            resultMap.put("code", "500");
            resultMap.put("msg", "该卡号不存在");
            return resultMap;
        }
        //发送开启箱门指令使用过后更新动态二维码
        Map<String, String> messageMap = new HashMap<>();
        messageMap.put("code", CompanyEquipment.EquipmentAction.EquipmentActionCode.EQUIPMENT_OPEN.getKey());
        messageMap.put("msg", CompanyEquipment.EquipmentAction.EquipmentActionCode.EQUIPMENT_OPEN.getValue());
        String token = JwtUtils.generateToken(member.getAliUserId(), ALI_API_EXPRIRE, ALI_API_TOKEN_SECRET_KEY);
        String securityToken = JwtUtils.generateEncryptToken(token, ALI_API_TOKEN_CYPTO_KEY);
        messageMap.put("token", securityToken);
        equipmentMessageService.sendMessageToMQ4IoTUseSignatureMode(JSONObject.toJSONString(messageMap),companyEquipment.getHardwareCode(), mqttClient);
        resultMap.put("code", "200");
        resultMap.put("msg", "操作成功");
        return resultMap;
    }

}
