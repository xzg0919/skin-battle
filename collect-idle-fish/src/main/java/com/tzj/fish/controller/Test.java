package com.tzj.fish.controller;

import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaIdleRecycleOrderQueryRequest;
import com.taobao.api.request.AlibabaIdleRecycleSpuTemplateModifyRequest;
import com.taobao.api.response.AlibabaIdleRecycleOrderQueryResponse;
import com.taobao.api.response.AlibabaIdleRecycleSpuTemplateModifyResponse;

import static com.tzj.collect.common.utils.ToolUtils.appkey;

public class Test {

    public static void main(String[] args) throws ApiException {
        TaobaoClient client = new DefaultTaobaoClient("http://gw.api.taobao.com/router/rest", "28326416", "6b8fb4524e18b154851fa46c67768ef3");
        AlibabaIdleRecycleOrderQueryRequest req = new AlibabaIdleRecycleOrderQueryRequest();
        req.setBizOrderId(872657249920884234L);
        AlibabaIdleRecycleOrderQueryResponse rsp = client.execute(req);
        System.out.println(rsp.getBody());
    }

}
