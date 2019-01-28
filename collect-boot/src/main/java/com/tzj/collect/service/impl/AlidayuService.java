package com.tzj.collect.service.impl;

import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;
import com.tzj.collect.common.util.ToolUtils;

/**
 * 
 * <p>Created on 2017年8月14日</p>
 * <p>Title:       [支付宝绿账系统]_[]_[]</p>
 * <p>Description: []</p>
 * <p>Copyright:   Copyright (c) 2017</p>
 * <p>Company:     上海挺之军信息科技有限公司 </p>
 * <p>Department:  研发部</p>
 * @author         [杨欢][yanghuan1937@aliyun.com]
 * @version        1.0
 */
public final class AlidayuService
{
    /**
     * 
     * <p>Description:[构造器方法描述]</p>
     * @coustructor 方法.
     */
    private AlidayuService()
    {
        
    }
    
    /**
     * 
     * <p>Created on 2017年8月14日</p>
     * <p>Description:[发送验证码]</p>
     * @author:[杨欢][yanghuan1937@aliyun.com] 
     * @update:[日期YYYY-MM-DD] [更改人姓名]
     */
    public static void sendMessage(final String freeSignName, final String moblie, final String temlateCode, 
            final String code)
    {
        TaobaoClient client = new DefaultTaobaoClient(ToolUtils.url, ToolUtils.appkey, ToolUtils.secret);
        AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
        req.setExtend("123456");
        req.setSmsType("normal");
        req.setSmsFreeSignName(freeSignName);
        req.setSmsParamString("{\"code\":\"" + code + "\",\"product\":\"蚂蚁收呗\"}");
        req.setRecNum(moblie);
        req.setSmsTemplateCode(temlateCode);
        AlibabaAliqinFcSmsNumSendResponse rsp;
        try
        {
            rsp = client.execute(req);
            System.out.println(rsp.getBody());
        }
        catch (ApiException e)
        {
            e.printStackTrace();
        }
    }
    
    /**
     * 
     * <p>Created on 2017年8月14日</p>
     * <p>Description:[发送接单短信]</p>
     * @author:[王灿] 
     * @update:[日期YYYY-MM-DD] [更改人姓名]
     * @param freeSignName : 服务商名称
     * @param moblie : 向具体某个手机号发送短信
     * @param temlateCode : SMS_142151759
     * @param recyclerName : 回收人员名称
     * @param phone : 回收人员手机号
     * @param companyName : 企业名称
     */
    public static void sendOrder(final String freeSignName, final String moblie, final String temlateCode, 
            final String recyclerName,final String phone,final String companyName)
    {
        TaobaoClient client = new DefaultTaobaoClient(ToolUtils.url, ToolUtils.appkey, ToolUtils.secret);
        AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
        req.setExtend("123456");
        req.setSmsType("normal");
        req.setSmsFreeSignName(freeSignName);
        req.setSmsParamString("{\"name\":\"" + recyclerName + "\",\"phone\":\""+phone+"\",\"company\":\""+companyName+"\"}");
        req.setRecNum(moblie);
        req.setSmsTemplateCode(temlateCode);
        AlibabaAliqinFcSmsNumSendResponse rsp;
        try
        {
            rsp = client.execute(req);
            System.out.println(rsp.getBody());
        }
        catch (ApiException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 
     * <p>Created on 2017年8月14日</p>
     * <p>Description:[随机生成一个验证码]</p>
     * @author:[杨欢][yanghuan1937@aliyun.com] 
     * @update:[日期YYYY-MM-DD] [更改人姓名]
     * @return String
     */
    public static String generateMessageCode()
    {
        int messagecode = (int) ((Math.random() * 9 + 1) * 100000);
        return String.valueOf(messagecode);
    }

    
    public static void main(String[] args)
    {
    	AlidayuService.sendOrder("蚂蚁收呗", "18375336389", "SMS_142151759", "龙建","15777555577","龙建集团");
    }
}
