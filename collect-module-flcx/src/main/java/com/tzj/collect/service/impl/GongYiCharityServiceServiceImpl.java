package com.tzj.collect.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaCharityCharitytimeQueryRequest;
import com.taobao.api.request.AlibabaCharityUseractionSyncRequest;
import com.taobao.api.response.AlibabaCharityCharitytimeQueryResponse;
import com.taobao.api.response.AlibabaCharityUseractionSyncResponse;
import com.tzj.collect.common.util.TaobaoUtil;
import com.tzj.collect.core.service.GongYiCharityService;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

@Component
@Service(version = "${flcx.service.version}")
public class GongYiCharityServiceServiceImpl implements GongYiCharityService {


    @Override
    public  AlibabaCharityUseractionSyncResponse.UserActionSyncResult gongYiCharityUser(String aliUserId){
        TaobaoClient client = new DefaultTaobaoClient(TaobaoUtil.GONGYI_APP_URL, TaobaoUtil.GONGYI_APP_KEY, TaobaoUtil.GONGYI_APP_SECRET);
        AlibabaCharityUseractionSyncRequest req = new AlibabaCharityUseractionSyncRequest();
        AlibabaCharityUseractionSyncRequest.ChannelUserActionDto obj1 = new AlibabaCharityUseractionSyncRequest.ChannelUserActionDto();
        obj1.setActivityId(TaobaoUtil.GONGYI_ACTIVITY_ID);
        obj1.setEventId(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + (new Random().nextInt(899999) + 100000));
        obj1.setTbUserId(Long.parseLong(aliUserId));
        obj1.setForeRegActivity(false);
        obj1.setCharityTimestamp(new Date());
        req.setChannelUserActionDto(obj1);
        AlibabaCharityUseractionSyncResponse rsp = null;
        try{
            rsp = client.execute(req);
        }catch (Exception e){
            e.printStackTrace();
        }
        return  rsp.getResult().getData();
    }
    @Override
    public  Long gongYiCharityCharityTime(String aliUserId){
        TaobaoClient client = new DefaultTaobaoClient(TaobaoUtil.GONGYI_APP_URL, TaobaoUtil.GONGYI_APP_KEY, TaobaoUtil.GONGYI_APP_SECRET);
        AlibabaCharityCharitytimeQueryRequest req = new AlibabaCharityCharitytimeQueryRequest();
        req.setTbUid(Long.parseLong(aliUserId));
        req.setActivityId(TaobaoUtil.GONGYI_ACTIVITY_ID);
        AlibabaCharityCharitytimeQueryResponse rsp = null;
        try{
            rsp = client.execute(req);
        }catch (Exception e){
            e.printStackTrace();
        }
        return  rsp.getData();
    }


}
