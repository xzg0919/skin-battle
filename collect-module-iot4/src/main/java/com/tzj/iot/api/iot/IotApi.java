package com.tzj.iot.api.iot;


import com.alibaba.fastjson.JSONObject;
import com.tzj.collect.core.param.iot.IOT4Bean;
import com.tzj.module.api.annotation.*;

import java.util.HashMap;
import java.util.Map;

import static com.tzj.collect.common.constant.TokenConst.EQUIPMENT_APP_API_COMMON_AUTHORITY;

@ApiService
public class IotApi {


    /**
     * 扫码开箱动态二维码
     * @return
     */
    @Api(name = "get.qrCode", version = "1.0")
    @RequiresPermissions(values = EQUIPMENT_APP_API_COMMON_AUTHORITY)
    @SignIgnore
    @AuthIgnore
    public Map<String, Object> qrCode(IOT4Bean iot4Bean) {
        Map<String, Object> returnMap = new HashMap<>();
        Long timeStamp =System.currentTimeMillis();
        String deviceCode = iot4Bean.getDeviceCode();
        String aliUrl = "alipays://platformapi/startapp?appId=2018060660292753&page=pages/view/index/index&query=";
        returnMap.put("aliUrl", aliUrl);
        JSONObject params =new JSONObject();
        params.put("timeStamp",timeStamp);
        params.put("deviceCode",deviceCode);
        params.put("from","iot4");
        returnMap.put("params",params);
        return returnMap;
    }




}
