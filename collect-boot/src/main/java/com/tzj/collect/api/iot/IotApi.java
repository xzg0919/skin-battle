package com.tzj.collect.api.iot;

import com.tzj.collect.api.ali.param.MemberBean;
import com.tzj.collect.api.iot.param.IotParamBean;
import com.tzj.collect.service.MemberService;
import com.tzj.collect.service.OrderService;
import com.tzj.module.api.annotation.Api;
import com.tzj.module.api.annotation.ApiService;
import com.tzj.module.api.annotation.AuthIgnore;
import com.tzj.module.api.annotation.SignIgnore;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

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
    public Map<String,Object> memberIsExist(MemberBean memberBean){
        Map<String,Object> resultMap = new HashMap<String,Object>();
        resultMap.put("isExist", memberService.memberIsExist(memberBean));
        return resultMap;
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

}
