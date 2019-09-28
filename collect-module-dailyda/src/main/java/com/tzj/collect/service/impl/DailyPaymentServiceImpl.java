package com.tzj.collect.service.impl;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayFundTransToaccountTransferModel;
import com.alipay.api.domain.AlipayOpenAppMiniTemplatemessageSendModel;
import com.alipay.api.request.*;
import com.alipay.api.response.*;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.api.commom.constant.AlipayConst;
import com.tzj.collect.common.constant.RocketMqConst;
import com.tzj.collect.entity.*;
import com.tzj.collect.mapper.DailyMemberMapper;
import com.tzj.collect.mapper.DailyPaymentMapper;
import com.tzj.collect.module.common.shard.ShardTableHelper;
import com.tzj.collect.service.DailyMemberService;
import com.tzj.collect.service.DailyPaymentService;
import com.tzj.collect.service.DailyPointService;
import com.tzj.module.common.notify.dingtalk.DingTalkNotify;
import com.tzj.module.easyopen.exception.ApiException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import static com.tzj.collect.common.constant.Const.*;
/**
 * 答答答memberService
 *
 * @author sgmark
 * @create 2019-08-29 9:35
 **/
@Service
@Transactional(readOnly = true)
public class DailyPaymentServiceImpl extends ServiceImpl<DailyPaymentMapper, Payment> implements DailyPaymentService {

    @Resource
    private DailyPointService dailyPointService;
    @Resource
    private DailyMemberService dailyMemberService;

    public  AlipayFundTransToaccountTransferResponse dailyDaTransfer(String aliUserId, String price, String outBizNo){
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", ALI_APPID, ALI_PAY_KEY, "json", "UTF-8", ALI_PUBLIC_KEY, "RSA2");
        AlipayFundTransToaccountTransferRequest request = new AlipayFundTransToaccountTransferRequest();

        AlipayFundTransToaccountTransferModel model = new AlipayFundTransToaccountTransferModel();
        model.setOutBizNo(outBizNo);
        model.setPayeeType("ALIPAY_USERID");
        model.setPayeeAccount(aliUserId);
        model.setAmount(price);
        model.setPayerShowName("答答答红包");
        model.setRemark("答答答红包(补发)");
        request.setBizModel(model);
        AlipayFundTransToaccountTransferResponse response =null;
        try {
            response  = alipayClient.execute(request);
//            System.out.println(response);
        }catch (AlipayApiException e){
            throw new ApiException("系统异常：" + e.getErrMsg());
        }
        return response;
    }

    /**
     * 查询转账信息
     */
    public AlipayFundTransOrderQueryResponse getTransfer(String paymentId) {
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", ALI_APPID, ALI_PAY_KEY, "json", "UTF-8", ALI_PUBLIC_KEY, "RSA2");
        AlipayFundTransOrderQueryRequest request = new AlipayFundTransOrderQueryRequest();
        request.setBizContent("{" +
                "\"out_biz_no\":\""+paymentId+"\"" +
                //"\"order_id\":\"20160627110070001502260006780837\"" +
                "  }");
        AlipayFundTransOrderQueryResponse response = null;
        try {
            response = alipayClient.execute(request);
        } catch (AlipayApiException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if(response.isSuccess()){
            System.out.println("调用成功");
        } else {
            System.out.println("调用失败");
        }
        return response;
    }


    public static void main(String[] args) {
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", ALI_APPID, ALI_PAY_KEY, "json", "UTF-8", ALI_PUBLIC_KEY, "RSA2");

        AlipayFundTransUniTransferRequest request = new AlipayFundTransUniTransferRequest();
        request.setBizContent("{" +
                "\"out_biz_no\":\""+UUID.randomUUID()+"\"," +
                "\"trans_amount\":0.1," +
                "\"product_code\":\"TRANS_ACCOUNT_NO_PWD\"," +
                "\"biz_scene\":\"DIRECT_TRANSFER\"," +
                "\"order_title\":\"答答答红包\"," +
                "\"payee_info\":{" +
                "\"identity\":\"131011501636136699\"," +
                "\"identity_type\":\"ALIPAY_USER_ID\"," +
                "    }," +
                "\"remark\":\"答答答红包\"," +
                "\"business_params\":\"{\\\"sub_biz_scene\\\":\\\"REDPACKET\\\"}\"" +
                "  }");
        AlipayFundTransUniTransferResponse response = null;
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
//        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", ALI_APPID, ALI_PAY_KEY, "json", "UTF-8", ALI_PUBLIC_KEY, "RSA2");
//        AlipayFundTransOrderQueryRequest request = new AlipayFundTransOrderQueryRequest();
//        request.setBizContent("{" +
//                "\"out_biz_no\":\""+"fd2860b2-edb2-4ab9-85dc-d89054a4d365"+"\"" +
////                "\"order_id\":\"20190827110070001506050010013865\"" +
//                "  }");
//        AlipayFundTransOrderQueryResponse response = null;
//        try {
//            response = alipayClient.execute(request);
//        } catch (AlipayApiException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        if(response.isSuccess()){
//            System.out.println("调用成功");
//        } else {
//            System.out.println("调用失败");
//        }

    }

    /**
     * @param OrderNo:订单编号
     * @author 王灿
     */
    @Transactional(readOnly = false)
    public void updateMemberPoint(String aliUserId, String OrderNo, double amount,String descrb) {
        DecimalFormat df   = new DecimalFormat("######0.00");
        amount = Double.parseDouble(df.format(amount));

        Point points = dailyPointService.getPoint(aliUserId);
        if (points == null) {
            points = new Point();
            points.setAliUserId(aliUserId);
            points.setPoint(amount);
            points.setRemainPoint(amount);
            dailyPointService.insert(points);
        } else {
            points.setPoint(points.getPoint() + amount);
            points.setRemainPoint(points.getRemainPoint() + amount);
            dailyPointService.updateById(points);
        }
        //给用户增加会员卡积分
        try {
            Member member = dailyMemberService.selectMemberByAliUserId(aliUserId);
            System.out.println("给用户增加的积分是 ：" + amount + "----point: " +Double.parseDouble(df.format(points.getRemainPoint()))+ "");
            this.updatePoint(member.getAliCardNo(), member.getOpenCardDate(), Double.parseDouble(df.format(points.getRemainPoint())) + "", null, member.getAppId());
        } catch (Exception e) {
            System.out.println("给用户增加积分失败---------------");
        }
        PointList pointList = new PointList();
        pointList.setAliUserId(aliUserId);
        pointList.setPoint(amount + "");
        pointList.setType("0");
        pointList.setDocumentNo(OrderNo);
        pointList.setDescrb(descrb);
        dailyPointService.insertPointList(pointList);
    }


    /**
     * <p>Discription:[更改会员积分]</p>
     * @author:[王灿]
     * @update:[日期YYYY-MM-DD] [更改人姓名]
     */
    public String updatePoint(String targetCardNo, Date openDate, String point, String vip, String appId)
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
            AlipayClient alipayClient = new DefaultAlipayClient(AlipayConst.serverUrl, appId, AlipayConst.private_key, AlipayConst.format, AlipayConst.input_charset, AlipayConst.ali_public_key, AlipayConst.sign_type);
            AlipayMarketingCardUpdateResponse response = alipayClient.execute(request);
            if(!response.isSuccess())
            {
                System.out.println("更新会员卡积分失败");
                DingTalkNotify.sendAliErrorMessage(Thread.currentThread().getStackTrace()[1].getClassName()
                        ,Thread.currentThread().getStackTrace()[1].getMethodName(),"更新会员卡积分失败",
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
