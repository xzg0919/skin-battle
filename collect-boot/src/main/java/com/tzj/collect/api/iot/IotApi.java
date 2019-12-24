package com.tzj.collect.api.iot;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tzj.collect.api.common.async.AsyncRedis;
import com.tzj.collect.api.iot.localmap.LatchMap;
import com.tzj.collect.api.iot.messagecode.MessageCode;
import com.tzj.collect.common.util.MemberUtils;
import com.tzj.collect.core.param.ali.MemberBean;
import com.tzj.collect.core.param.iot.IotCompanyResult;
import com.tzj.collect.core.param.iot.IotErrorParamBean;
import com.tzj.collect.core.param.iot.IotParamBean;
import com.tzj.collect.core.param.iot.IotPostParamBean;
import com.tzj.collect.core.service.*;
import com.tzj.collect.core.service.impl.FileUploadServiceImpl;
import com.tzj.collect.entity.CompanyEquipment;
import com.tzj.collect.entity.Member;
import com.tzj.module.api.annotation.*;
import com.tzj.module.easyopen.exception.ApiException;
import com.tzj.module.easyopen.file.FileBase64Param;
import com.tzj.module.easyopen.file.FileBean;
import com.tzj.module.easyopen.file.FileUploadService;
import com.tzj.module.easyopen.util.ApiUtil;
import io.itit.itf.okhttp.FastHttpClient;
import io.itit.itf.okhttp.Response;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.tzj.collect.common.constant.TokenConst.ALI_API_COMMON_AUTHORITY;
import static com.tzj.collect.common.constant.TokenConst.BUSINESS_API_COMMON_AUTHORITY;

/**
 * iot设备接口
 *
 * @author sgmark
 * @create 2019-03-30 12:17
 **/
@ApiService
public class IotApi {

    @Autowired
    private MemberService memberService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private AsyncRedis asyncRedis;
    @Resource
    private EquipmentErrorCodeService equipmentErrorCodeService;
    @Resource
    private EquipmentErrorListService equipmentErrorListService;
    @Resource
    private FileUploadServiceImpl fileUploadServiceImpl;
    @Resource
    private EquipmentMessageService equipmentMessageService;
    @Resource
    private MqttClient mqttClient;


    public static AtomicBoolean flag = new AtomicBoolean(true);//保证当前线程能执行
//    private LatchMap latchMapResult = null;
    public static Map<String, LatchMap> latMapConcurrent = new ConcurrentHashMap<>();//本地
    /**
     * 会员存在与否
      * @author sgmark@aliyun.com
      * @date 2019/3/30 0030
      * @param
      * @return
      */
    @Api(name = "member.isexist", version = "1.0")
    @SignIgnore
    @AuthIgnore
    public Map memberIsExist(MemberBean memberBean){
        return memberService.memberIsExist(memberBean);
    }

    @Api(name = "iot.order.create", version = "1.0")
    @SignIgnore
    @AuthIgnore
    public Map<String, Object> iotCreatOrder(IotParamBean iotParamBean){
        return orderService.iotCreatOrder(iotParamBean);
    }
    @Api(name = "iot.order.create", version = "2.0")
    @RequiresPermissions(values = BUSINESS_API_COMMON_AUTHORITY)
    public Map<String, Object> iotCreatOrder2(IotParamBean iotParamBean){
        return orderService.iotCreatOrder(iotParamBean);
    }

//    @Api(name = "iot.order.update", version = "1.0")
//    @SignIgnore
//    @AuthIgnore
//    public Map<String, Object> iotUpdateOrderItemAch(IotParamBean iotParamBean){
//        return orderService.iotUpdateOrderItemAch(iotParamBean);
//    }

    @Api(name = "iot.scan", version = "1.0")
    @RequiresPermissions(values = ALI_API_COMMON_AUTHORITY)
    public Map<String, Object> iotScan(IotPostParamBean iotPostParamBean)throws Exception{
        Map map = new HashMap();
        MemberBean memberBean = new MemberBean();
        if (StringUtils.isEmpty(iotPostParamBean.getCabinetNo())){
//            throw new ApiException("cabinetNo不存在", "-9");
            map.put("msg", MessageCode.ERROR_QRCODE.getValue());
            map.put("status", MessageCode.ERROR_QRCODE.getKey());
            return map;
        }
        Member member = MemberUtils.getMember();
        memberBean.setCardNo(member.getCardNo());
        if (member != null){
            String iotUrl = null;
            String equipmentCode = "";
            IotCompanyResult iotCompanyResult = null;
            //根据设备编号在company表中找到访问地址
            equipmentCode = iotPostParamBean.getCabinetNo();
            if (equipmentCode.contains("@")){
                equipmentCode = equipmentCode.substring(0, equipmentCode.indexOf("@"));
            }
            iotCompanyResult = companyService.selectIotUrlByEquipmentCode(equipmentCode);
            if (null == iotCompanyResult){
                map.put("msg", MessageCode.ERROR_QRCODE.getValue());
                map.put("status", MessageCode.ERROR_QRCODE.getKey());
                return map;
            }
            iotPostParamBean.setQrUrl(iotCompanyResult.getIotUrl());
            iotPostParamBean.setAppId(iotCompanyResult.getUserName());
            iotPostParamBean.setAppKey(iotCompanyResult.getPassword());
            iotPostParamBean.setMemberId(member.getCardNo());
            //发送get请求开箱
            iotPostParamBean.setMobile(member.getMobile());
            iotPostParamBean.setTranTime(System.currentTimeMillis());
            String encryptData = encrypt(iotPostParamBean);
            iotPostParamBean.setAPIName("OpnBox");
            String jsonStr= JSON.toJSONString(iotPostParamBean);
            String sign= buildSign(JSON.parseObject(jsonStr));
            iotPostParamBean.setSign(sign);
            // encryptData 加密数据：方式--- md5(md5(app_id&app_key&cabinetNo&mobile&memberId&tranTime)&tranTime)
            if (StringUtils.isEmpty(iotCompanyResult.getIotUrl()) || StringUtils.isEmpty(iotPostParamBean.getCabinetNo())){
//              throw new ApiException("cabinetNo不存在", "-9");
                map.put("msg", MessageCode.ERROR_QRCODE.getValue());
                map.put("status", MessageCode.ERROR_QRCODE.getKey());
                return map;
            }
            iotUrl = iotCompanyResult.getIotUrl() +"?APIName="+iotPostParamBean.getAPIName()
                    +"&cabinetNo="+iotPostParamBean.getCabinetNo()+ "&memberId="+ member.getCardNo()
                    +"&mobile="+iotPostParamBean.getMobile()+ "&sign="+ iotPostParamBean.getSign()
                    + "&tranTime="+iotPostParamBean.getTranTime().toString()
                    + "&encryptData=" + encryptData + "&appId="+ iotPostParamBean.getAppId();
            System.out.println(iotUrl);
            Response response = null;
            long codeTime = System.currentTimeMillis() - Long.parseLong(String.valueOf(iotPostParamBean.getTranTime()));
            //自己的设备增加检测
            if (iotUrl.contains("mayishoubei.com")){
                //动态二维码检测
                if(equipmentMessageService.redisSetCheck("IoT:Equipment:Uid", iotPostParamBean.getEcUuid())){
                    throw new ApiException("二维码已被使用");
                }if (Long.parseLong("300000") < codeTime || codeTime <= 0) {
                    throw new ApiException("二维码已过期");
                }
                else {
                    equipmentMessageService.redisSetAdd("IoT:Equipment:Uid", iotPostParamBean.getEcUuid());
                }
                //发送开启箱门指令使用过后更新动态二维码
                Map<String, String> messageMap = new HashMap<>();
                messageMap.put("code", CompanyEquipment.EquipmentAction.EquipmentActionCode.EQUIPMENT_OPEN.getKey());
                messageMap.put("msg", CompanyEquipment.EquipmentAction.EquipmentActionCode.EQUIPMENT_OPEN.getValue());
                equipmentMessageService.sendMessageToMQ4IoTUseSignatureMode(JSONObject.toJSONString(messageMap),iotCompanyResult.getHardwareCode(), mqttClient);
                map.put("msg", MessageCode.SUCCESS_OPEN.getValue());
                map.put("status", MessageCode.SUCCESS_OPEN.getKey());
                return map;
            }
            try {
                response =  FastHttpClient.get().url(iotUrl).build().execute();
            }catch (Exception e){
                map.put("msg", "连接超时");
                map.put("status", MessageCode.STOPPAGE_ERROR.getKey());
                return map;
            }
            String resultJson=response.body().string();
            Object object = JSON.parseObject(resultJson);
//            map.put("status", ((JSONObject) object).get("errorcode"));
            if (MessageCode.SUCCESS_OPEN.getKey().equals(((JSONObject) object).get("errorcode"))){
                map.put("msg", MessageCode.SUCCESS_OPEN.getValue());
                map.put("status", MessageCode.SUCCESS_OPEN.getKey());
            }else if (MessageCode.EMPLOY_ERROR.getKey().equals(((JSONObject) object).get("errorcode"))){
                map.put("msg", MessageCode.EMPLOY_ERROR.getValue());
                map.put("status", MessageCode.EMPLOY_ERROR.getKey());
            }else if (MessageCode.STOPPAGE_ERROR.getKey().equals(((JSONObject) object).get("errorcode"))){
                map.put("msg", MessageCode.STOPPAGE_ERROR.getValue());
                map.put("status", MessageCode.STOPPAGE_ERROR.getKey());
            }else {
                map.put("msg", MessageCode.OTHERS_ERROR.getValue());
                map.put("status", MessageCode.OTHERS_ERROR.getKey());
            }
            map.put("errorMsg", ((JSONObject) object).get("errormsg"));
            map.put("APIName", ((JSONObject) object).get("APIName"));
            map.put("remark", ((JSONObject) object).get("remark"));
            System.out.println(resultJson);
        }
        return map;
    }
    private static String buildSign(Map<String, ?> paramsMap){
        Set<String> keySet = paramsMap.keySet();
        List<String> paramNames = new ArrayList(keySet);
        Collections.sort(paramNames);
        List<String> list = new ArrayList();
        Iterator var5 = paramNames.iterator();

        String paramName;
        while(var5.hasNext()) {
            paramName = (String)var5.next();
            String value = paramsMap.get(paramName).toString();
            if (StringUtils.isNotEmpty(paramName) && StringUtils.isNotEmpty(value)) {
                list.add(paramName + "=" + (value != null ? value : ""));
            }
        }
        String source = StringUtils.join(list, "&");
        paramName = ApiUtil.md5(source);
        return paramName;
    }

    /**
     * 等待生成订单的长连接（跳转至订单详情页面，返回订单id）
      * @author sgmark@aliyun.com
      * @date 2019/4/22 0022
      * @param
      * @return
      */
    @Api(name = "iot.long.pulling", version = "1.0")
    @RequiresPermissions(values = ALI_API_COMMON_AUTHORITY)
    public Map<String, Object> longPulling(){
        Member member = MemberUtils.getMember();
        String iotMemId = member.getAliUserId();
        Long date = null;
        HashMap<String, Object> result = new HashMap<>();
        date = System.currentTimeMillis();
        try {
            LatchMap latchMap = null;
            if (!latMapConcurrent.containsKey(iotMemId)) {
                latchMap = new LatchMap();
                latMapConcurrent.put(iotMemId, latchMap);
            } else {
                latchMap = latMapConcurrent.get(iotMemId);
            }

            if (null != latchMap.orderId) {
                result.put("success", true);//表示二次扫码
            }
            //每次进来初始化线程
            latchMap.latch = new CountDownLatch(1);

            try {
                // 线程等待
                //开启异步线程，如果redis有当前用户订单，当前线程重新启动
                asyncRedis.getTokenByCache(date, iotMemId);
                latchMap.latch.await(9, TimeUnit.SECONDS);
            } catch (Exception e) {
                e.printStackTrace();
            }
            latchMap = latMapConcurrent.get(iotMemId);
            if (latchMap.getOrderId() != null){
                result.put("id", latchMap.getOrderId());
                result.put("code", 0);
                result.put("msg", "操作成功");
                result.put("tryAgain", "N");
                result.put("status", "IOTORDER");
                //使用后清空
                latchMap.orderId = null;
                latMapConcurrent.put(iotMemId, latchMap);
                return result;
            }

        }catch (Exception e){
           throw new ApiException(e.getMessage());
        }
        result.put("tryAgain", "Y");
        return result;
    }
    /**
     * iot错误信息列表
     * @author: sgmark@aliyun.com
     * @Date: 2019/10/16 0016
     * @Param: 
     * @return: 
     */
    @Api(name = "iot.error.code", version = "1.0")
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
    @Api(name = "iot.error.list.member", version = "1.0")
    @RequiresPermissions(values = ALI_API_COMMON_AUTHORITY)
    public Map<String, Object>  iotErrorListByMember(IotErrorParamBean iotErrorParamBean){
        iotErrorParamBean.setMember(MemberUtils.getMember());
        return equipmentErrorListService.iotErrorListByMember(iotErrorParamBean);
    }


    /**
     * 上传文件0
     */
    @Api(name = "iot.util.uploadImage", version = "1.0")
    @SignIgnore
    @AuthIgnore
    public List<FileBean> uploadImage(List<FileBase64Param> fileBase64ParamLists){
        return fileUploadServiceImpl.uploadImageForIot(fileBase64ParamLists);
    }

    /** 加密方式md5(md5(app_id&app_key&cabinetNo&mobile&memberId&tranTime)&tranTime)
     * @author sgmark@aliyun.com
     * @date 2019/4/25 0025
     * @param
     * @return
     */
    private static String encrypt(IotPostParamBean iotPostParamBean) {
        Object []  strings = {iotPostParamBean.getAppId(), iotPostParamBean.getAppKey(), iotPostParamBean.getCabinetNo(),
                iotPostParamBean.getMobile(), iotPostParamBean.getMemberId(), iotPostParamBean.getTranTime()+""};
//        Arrays.sort(strings);//排序
        List<Object> list = Arrays.asList(strings);
        strings = new Object[]{ApiUtil.md5(StringUtils.join(list, "&")), iotPostParamBean.getTranTime()};
        list = Arrays.asList(strings);
        return ApiUtil.md5(StringUtils.join(list, "&"));
    }

}
