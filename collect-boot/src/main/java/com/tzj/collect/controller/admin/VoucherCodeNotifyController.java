package com.tzj.collect.controller.admin;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tzj.collect.common.constant.VoucherConst;
import com.tzj.collect.core.service.VoucherMemberService;
import com.tzj.collect.core.service.VoucherNofityService;
import com.tzj.collect.entity.VoucherNofity;

@Controller
@RequestMapping("/ali")
public class VoucherCodeNotifyController
{
    @Autowired
    private VoucherNofityService voucherNofityService;
    @Autowired
    private VoucherMemberService voucherMemberService;
    /**
     * 
     * <p>Created on 2019年10月25日</p>
     * <p>Description:[支付宝发券通知]</p>
     * @author:[杨欢][yanghuan1937@aliyun.com] 
     * @update:[日期YYYY-MM-DD] [更改人姓名]
     * @return String
     */
    @RequestMapping("/aliNotity")
    public void aliNotity(HttpServletRequest request,HttpServletResponse response)
    {
        VoucherNofity voucherNofity = new VoucherNofity();
        
        voucherNofity.setCreateBy("yh");
        voucherNofity.setCreateDate(new Date());
        voucherNofity.setDelFlag("0");
        voucherNofity.setBizContent(request.getParameter("biz_content"));
        /*voucherNofity.setEntityNum(request.getParameter("entity_num"));
        voucherNofity.setFluxAmount(request.getParameter("flux_amount"));
        voucherNofity.setNofityType(request.getParameter("nofity_type"));
        voucherNofity.setTemplateId(request.getParameter("template_id"));
        voucherNofity.setUid(request.getParameter("uid"));
        voucherNofity.setVoucherId(request.getParameter("voucher_id"));
        voucherNofity.setAlipayBizNo(request.getParameter("alipay_biz_no"));
        voucherNofity.setBizTime(request.getParameter("biz_time"));*/
        voucherNofity.setNotifyStatus(VoucherConst.VOUCHER_NOTIFY_OK);
        try
        {
            Map<String, String[]> paramMap = request.getParameterMap();
            if(null != paramMap && !paramMap.isEmpty())
            {
                String param = "";
                String value[] = null;
                for(String key : paramMap.keySet())
                {
                    param = param.concat("key：" + key);
                    param = param.concat("--------");
                    param = param.concat("value：");
                    value = paramMap.get(key);
                    if(null != value && value.length > 0)
                    {
                        for(int i=0,j=value.length;i<j;i++)
                        {
                            param = param.concat(value[i]);
                            if(i + 1 < j)
                            {
                                param = param.concat(".");
                            }
                        }
                    }
                    param = param.concat("\r");
                }
                System.out.println(param);
            }     
            // 解析通知
            voucherNofity = makeVoucherNofity(voucherNofity);
            // 存通知
            voucherNofityService.insert(voucherNofity);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        try
        {
            // 发券
            voucherNofity = voucherMemberService.send(voucherNofity);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        try
        {
            response.getWriter().write("success");
            response.getWriter().flush();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        
    }
    /**
     * <p>Created on 2019年10月27日</p>
     * <p>Description:[解析通知]</p>
     * @author:[杨欢][yanghuan1937@aliyun.com] 
     * @update:[日期YYYY-MM-DD] [更改人姓名]
     * @return VoucherNofity
     */
    private VoucherNofity makeVoucherNofity(VoucherNofity voucherNofity)
    {
        if(StringUtils.isBlank(voucherNofity.getBizContent()))
        {
            voucherNofity.setNotifyStatus("error");
            voucherNofity.setNotifyRemark("通知内容为空");
        }
        else
        {
            JSONObject jobj = JSON.parseObject(voucherNofity.getBizContent());
            if(null != jobj.get("entity_num"))
            {
                voucherNofity.setEntityNum(jobj.get("entity_num").toString());
            }
            if(null != jobj.get("flux_amount"))
            {
                voucherNofity.setFluxAmount(jobj.get("flux_amount").toString());
            }
            if(null != jobj.get("nofity_type"))
            {
                voucherNofity.setNofityType(jobj.get("nofity_type").toString());
            }
            if(null != jobj.get("template_id"))
            {
                voucherNofity.setTemplateId(jobj.get("template_id").toString());
            }
            if(null != jobj.get("user_id"))
            {
                voucherNofity.setUid(jobj.get("user_id").toString());
            }
            if(null != jobj.get("voucher_id"))
            {
                voucherNofity.setVoucherId(jobj.get("voucher_id").toString());
            }
            if(null != jobj.get("alipay_biz_no"))
            {
                voucherNofity.setAlipayBizNo(jobj.get("alipay_biz_no").toString());
            }
            if(null != jobj.get("biz_time"))
            {
                voucherNofity.setBizTime(jobj.get("biz_time").toString());
            }
        }
        
        return voucherNofity;
    }
}
