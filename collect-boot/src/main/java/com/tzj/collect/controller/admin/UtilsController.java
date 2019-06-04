package com.tzj.collect.controller.admin;


import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayOpenAppQrcodeCreateModel;
import com.alipay.api.request.AlipayOpenAppQrcodeCreateRequest;
import com.alipay.api.response.AlipayOpenAppQrcodeCreateResponse;
import com.tzj.collect.common.constant.AlipayConst;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("utils")
public class UtilsController {

    @RequestMapping("/get/xcxUrl")
    public String getXcxUrl(String urlParam, String id, String type){
        System.out.println("---------"+urlParam+"-------"+id+"-------"+type);
        return    UtilsController.getXcxUri(urlParam,id,type);
    }
    public static String getXcxUri(String urlParam, String id, String type){
        System.out.println("---------"+urlParam+"-------"+id+"-------"+type);
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConst.serverUrl, AlipayConst.XappId, AlipayConst.private_key, AlipayConst.format, AlipayConst.input_charset, AlipayConst.ali_public_key, AlipayConst.sign_type);
        AlipayOpenAppQrcodeCreateRequest request = new AlipayOpenAppQrcodeCreateRequest();
        AlipayOpenAppQrcodeCreateModel model = new AlipayOpenAppQrcodeCreateModel();
        model.setUrlParam(urlParam);
        model.setQueryParam("id="+id+"&type="+type);
        model.setDescribe("二维码的链接");
        request.setBizModel(model);
        AlipayOpenAppQrcodeCreateResponse response = null;
        try {
            response = alipayClient.execute(request);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        if(response.isSuccess()){
            System.out.println("调用成功");
        } else {
            System.out.println("调用失败");
        }
        return response.getQrCodeUrl();
    }
}
