package com.tzj.collect.api.iot;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tzj.collect.api.ali.param.MemberBean;
import com.tzj.collect.api.iot.param.IotParamBean;
import com.tzj.collect.api.iot.param.IotPostParamBean;
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
                iotUrl = companyService.selectIotUrlByEquipmentCode(iotPostParamBean.getCabinetNo());
            }
            //发送post请求开箱
            iotPostParamBean.setMobile(member.getMobile());
            iotPostParamBean.setAPIName("OpnBox");
            String jsonStr= JSON.toJSONString(iotPostParamBean);
            String sign= this.buildSign(JSON.parseObject(jsonStr));
            iotPostParamBean.setSign(sign);
            if (iotUrl == null && "".equals(iotUrl.trim())){
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
            map.put("status", ((JSONObject) object).get("status"));
            map.put("APIName", ((JSONObject) object).get("APIName"));
            map.put("errormsg", ((JSONObject) object).get("errormsg"));
            map.put("errorcode", ((JSONObject) object).get("errorcode"));
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
}
