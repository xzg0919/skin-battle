package com.tzj.green.serviceImpl;

import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;

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
        req.setSmsParamString("{\"code\":\"" + code + "\",\"product\":\"实体卡\"}");
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
        AlidayuService.sendMessage("蚂蚁收呗", "15979138004", "SMS_59045026", "123654");
    }
}
