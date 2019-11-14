package com.tzj.point.service.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayMarketingCardUpdateRequest;
import com.alipay.api.response.AlipayMarketingCardUpdateResponse;
import com.tzj.collect.common.mq.RocketMqConst;
import com.tzj.collect.common.notify.DingTalkNotify;
import com.tzj.point.common.constant.AlipayConst;
import com.tzj.point.service.AliPayService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Service
public class AliPayServiceImpl  implements AliPayService {

    /**
     * <p>Discription:[更改会员积分]</p>
     * @author:[王灿]
     * @update:[日期YYYY-MM-DD] [更改人姓名]
     */
    @Override
    public String updatePoint(String targetCardNo, Date openDate, String point, String vip)
    {
        String bizContent = "{";
        String usefullDate[] = getUsefullDate(openDate);
        if(null != point && point.contains("-"))
        {
            point = "0";
        }
        bizContent = bizContent
                + "\"target_card_no\":\""
                + targetCardNo
                + "\","
                + "\"target_card_no_type\":\"BIZ_CARD\","
                + "\"occur_time\":\""
                + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())
                + "\","
                + "\"card_info\":{"
                + "\"open_date\":\""
                + usefullDate[0]
                + "\","
                + "\"valid_date\":\""
                + usefullDate[1]
                + "\",";
        if(!StringUtils.isBlank(vip))
        {
            bizContent = bizContent
                    + "\"level\":\""
                    + vip
                    + "\",";
        }
        bizContent = bizContent
                + "\"point\":\""
                + point
                + "\","
                + "\"balance\":\"0\""
                + "},"
                + "\"ext_info\":\"\\\"\\\"\""
                + "}";
        try
        {
            AlipayMarketingCardUpdateRequest request = new AlipayMarketingCardUpdateRequest();
            request.setBizContent(bizContent);
            AlipayClient alipayClient = new DefaultAlipayClient(AlipayConst.serverUrl,AlipayConst.XappId, AlipayConst.private_key, AlipayConst.format, AlipayConst.input_charset, AlipayConst.ali_public_key, AlipayConst.sign_type);
            AlipayMarketingCardUpdateResponse response = alipayClient.execute(request);
            if(!response.isSuccess())
            {
                System.out.println("更新会员卡积分失败");
                DingTalkNotify.sendAliErrorMessage(Thread.currentThread().getStackTrace()[1].getClassName()
                        ,Thread.currentThread().getStackTrace()[1].getMethodName(),"app更新会员卡积分失败",
                        RocketMqConst.DINGDING_ERROR,response.getBody());
            }else {
                System.out.println("更新会员卡积分成功");
            }
        }
        catch (AlipayApiException e)
        {
            e.printStackTrace();
        }
        return vip;
    }
    /**
     * <p>Discription:[会员卡有效期，十年]</p>
     * @author:[王灿][yanghuan1937@aliyun.com]
     * @udate:[日期YYYY-MM-DD] [更改人姓名]
     * @return String []
     */
    private  String[] getUsefullDate(Date startDate)
    {
        Calendar now = Calendar.getInstance();
        if(null != startDate)
        {
            now.setTime(startDate);
        }
        String usefullDate[] = new String[2];
        usefullDate[0] = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(now.getTime());
        now.set(Calendar.YEAR,now.get(Calendar.YEAR) + 10);
        usefullDate[1] = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(now.getTime());
        return usefullDate;
    }
}
