package com.tzj.collect.api.iot;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tzj.collect.api.ali.param.MemberBean;
import com.tzj.collect.api.iot.localmap.LatchMap;
import com.tzj.collect.api.iot.messagecode.MessageCode;
import com.tzj.collect.api.iot.param.IotParamBean;
import com.tzj.collect.api.iot.param.IotPostParamBean;
import com.tzj.collect.common.redis.RedisUtil;
import com.tzj.collect.common.util.MemberUtils;
import com.tzj.collect.entity.Member;
import com.tzj.collect.service.CompanyService;
import com.tzj.collect.service.MemberService;
import com.tzj.collect.service.OrderService;
import com.tzj.module.api.annotation.*;
import com.tzj.module.easyopen.exception.ApiException;
import com.tzj.module.easyopen.util.ApiUtil;
import io.itit.itf.okhttp.FastHttpClient;
import io.itit.itf.okhttp.Response;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static com.tzj.collect.common.constant.TokenConst.ALI_API_COMMON_AUTHORITY;

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
    private RedisUtil redisUtil;

    private Boolean flag = true;//保证当前线程能执行
    private LatchMap latchMapResult = null;
    private Map<String, LatchMap> latMapConcurrent = new ConcurrentHashMap<>();//本地
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

//    @Api(name = "iot.order.update", version = "1.0")
//    @SignIgnore
//    @AuthIgnore
//    public Map<String, Object> iotUpdateOrderItemAch(IotParamBean iotParamBean){
//        return orderService.iotUpdateOrderItemAch(iotParamBean);
//    }

    @Api(name = "iot.scan", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ALI_API_COMMON_AUTHORITY)
    public Map<String, Object> iotScan(IotPostParamBean iotPostParamBean)throws Exception{
        Map map = null;
        MemberBean memberBean = new MemberBean();
        if (StringUtils.isEmpty(iotPostParamBean.getCabinetNo())){
            throw new ApiException("cabinetNo不存在", "-9");
        }
        Member member = MemberUtils.getMember();
        memberBean.setCardNo(member.getCardNo());
        if (member != null){
            String iotUrl = null;
            if (iotPostParamBean.getQrUrl() != null && !"".equals(iotPostParamBean.getQrUrl().trim())){//http://xxx.com?cabinetNo=xxxa
                iotUrl = iotPostParamBean.getQrUrl();
            }else {
                //根据设备编号在company表中找到访问地址
                String equipmentCode = iotPostParamBean.getCabinetNo();
                if (equipmentCode.contains("@")){
                    equipmentCode = equipmentCode.substring(0, equipmentCode.indexOf("@"));
                }
                iotUrl = companyService.selectIotUrlByEquipmentCode(equipmentCode);
            }
            //发送post请求开箱
            iotPostParamBean.setMobile(member.getMobile());
            iotPostParamBean.setAPIName("OpnBox");
            String jsonStr= JSON.toJSONString(iotPostParamBean);
            String sign= this.buildSign(JSON.parseObject(jsonStr));
            iotPostParamBean.setSign(sign);
            if (iotUrl == null || "".equals(iotUrl.trim())){
                throw new ApiException("cabinetNo不存在", "-9");
            }
            iotUrl = iotUrl +"?APIName="+iotPostParamBean.getAPIName()
                    +"&cabinetNo="+iotPostParamBean.getCabinetNo()+ "&memberId="+ member.getCardNo()
                    +"&mobile="+iotPostParamBean.getMobile()+ "&sign="+ iotPostParamBean.getSign()
                    + "&tranTime="+iotPostParamBean.getTranTime().toString();
            System.out.println(iotUrl);
            Response response= FastHttpClient.get().url(iotUrl).build().execute();
            String resultJson=response.body().string();
            Object object = JSON.parseObject(resultJson);
            map = new HashMap();
            map.put("status", ((JSONObject) object).get("errorcode"));
            if (MessageCode.SUCCESS_OPEN.getKey().equals(((JSONObject) object).get("errorcode"))){
                map.put("msg", MessageCode.SUCCESS_OPEN.getValue());
            }else if (MessageCode.EMPLOY_ERROR.getKey().equals(((JSONObject) object).get("errorcode"))){
                map.put("msg", MessageCode.EMPLOY_ERROR.getValue());
            }else if (MessageCode.STOPPAGE_ERROR.getKey().equals(((JSONObject) object).get("errorcode"))){
                map.put("msg", MessageCode.STOPPAGE_ERROR.getValue());
            }else {
                map.put("msg", MessageCode.OTHERS_ERROR.getValue());
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
    @SignIgnore
    @RequiresPermissions(values = ALI_API_COMMON_AUTHORITY)
    public Map<String, Object> longPulling(){
        Member member = MemberUtils.getMember();
        String uuId = "iot_member_id_"+ member.getId();
        Long date = null;
        HashMap<String, Object> result = new HashMap<>();
        //如果uuid时间已过期，获取新的uuId
        date = System.currentTimeMillis();
        try {
            LatchMap latchMap = null;
            if (!latMapConcurrent.containsKey(uuId)) {
                latchMap = new LatchMap();
                latMapConcurrent.put(uuId, latchMap);
            } else {
                latchMap = latMapConcurrent.get(uuId);
            }

            if (null != latchMap.orderId) {
                result.put("success", true);
            }

            if (null == latchMap.latch) {
                latchMap.latch = new CountDownLatch(1);
            }
            try {
                // 线程等待
                //开启异步线程，如果redis有当前用户订单，当前线程重新启动
                this.getTokenByCache(date, uuId);
                latchMap.latch.await(5, TimeUnit.MINUTES);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (latchMapResult.getOrderId() != null){
                result.put("id", latchMapResult.getOrderId());
                result.put("code", 0);
                result.put("msg", "操作成功");
                return result;
            }

        }catch (Exception e){
           throw new ApiException(e.getMessage());
        }
        return result;
    }

    public void getTokenByCache(Long startTime, String uuId){
        flag = true;//每次进来设值为真
        Hashtable<String, String> iotMapCache;
        do {
            try {
                iotMapCache  = (Hashtable<String, String>)redisUtil.get("iotMap");
                if (iotMapCache != null && iotMapCache.containsKey(uuId)){
                    latchMapResult = latMapConcurrent.get(uuId);
                    if (latchMapResult != null){
                        latchMapResult.orderId = iotMapCache.get(uuId);
                        latchMapResult.latch.countDown();
                        break;
                    }else{
                        continue;
                    }
                }else{
                    //每秒执行一次
                    try{
                        Thread.sleep(1000);
                    }catch(Exception e){
                        System.exit(0);//退出程序
                    }
                }
            }catch (NullPointerException e){
                System.exit(0);//退出程序
            }
        }while (System.currentTimeMillis() - startTime <= 300*1000 && flag);
        //5分钟内还没被扫码成功，关闭长连接
        if (null != latchMapResult){
            latchMapResult.latch.countDown();
        }
    }


}
