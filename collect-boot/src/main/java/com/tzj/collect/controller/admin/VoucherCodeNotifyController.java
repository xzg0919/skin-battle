package com.tzj.collect.controller.admin;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

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
        voucherNofity.setAlipayBizNo(request.getParameter("alipay_biz_no"));
        voucherNofity.setBizTime(request.getParameter("biz_time"));
        voucherNofity.setCreateBy("yh");
        voucherNofity.setCreateDate(new Date());
        voucherNofity.setDelFlag("0");
        voucherNofity.setEntityNum(request.getParameter("entity_num"));
        voucherNofity.setFluxAmount(request.getParameter("flux_amount"));
        voucherNofity.setNofityType(request.getParameter("nofity_type"));
        voucherNofity.setTemplateId(request.getParameter("template_id"));
        voucherNofity.setUid(request.getParameter("uid"));
        voucherNofity.setVoucherId(request.getParameter("voucher_id"));
        voucherNofity.setNotifyStatus("ok");
        try
        {
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
}
