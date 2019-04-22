package com.tzj.collect.service.impl;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.*;
import com.alipay.api.request.*;
import com.alipay.api.response.*;
import com.tzj.collect.common.constant.AlipayConst;
import com.tzj.collect.common.constant.RocketMqConst;
import com.tzj.collect.entity.Category;
import com.tzj.collect.entity.Order;
import com.tzj.collect.service.*;
import com.tzj.module.common.notify.dingtalk.DingTalkNotify;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class AliPayServiceImpl implements AliPayService{

    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderItemAchService orderItemAchService;
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private CategoryService categoryService;

	 /**
     * 根据用户授权的具体authCode查询是用户的userid和token 
     * @author 王灿
     * @return
     */
    public AlipaySystemOauthTokenResponse selectUserToken(String userCode,String appId) {
    	System.out.println("-------hua用户信息接口 userCode是："+userCode);
		AlipayClient alipayClient = new DefaultAlipayClient(AlipayConst.serverUrl, appId, AlipayConst.private_key, AlipayConst.format, AlipayConst.input_charset, AlipayConst.ali_public_key, AlipayConst.sign_type);
		AlipaySystemOauthTokenRequest request = new AlipaySystemOauthTokenRequest();
		request.setCode(userCode);
		request.setGrantType("authorization_code");
		 AlipaySystemOauthTokenResponse response=null;
		try {
			response = alipayClient.execute(request);
			//用户的授权的token
		    System.out.println(response.getAccessToken());
		    //用户的唯一userId
		    System.out.println(response.getUserId());
		} catch (AlipayApiException e) {
		    //处理异常
		    e.printStackTrace();
		}
		if(response.isSuccess()){
			System.out.println("调用用户查询token接口成功");
		} else {
			System.out.println("调用用户查询token接口失败");
		}
		return response;
	}
    /**
     * 调用接口查询用户的详细信息
     * @author 王灿
     * @return
     */
    public AlipayUserInfoShareResponse selectUser(String userToken,String appId) {
    	System.out.println("-------进入了查询用户信息接口 token是："+userToken);
		AlipayClient alipayClient = new DefaultAlipayClient(AlipayConst.serverUrl, appId, AlipayConst.private_key, AlipayConst.format, AlipayConst.input_charset, AlipayConst.ali_public_key, AlipayConst.sign_type);
		AlipayUserInfoShareRequest userinfoRequest = new AlipayUserInfoShareRequest();
			AlipayUserInfoShareResponse response = null;
			try {
				response = alipayClient.execute(userinfoRequest,userToken);
			} catch (AlipayApiException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(response.isSuccess())
            {
                System.out.println("调用查询用户信息的接口成功");
            }else {
            	System.out.println("调用查询用户信息的接口失败");
            	
            }
			return response;
	}

    /**
     * 
     * <p>Discription:[发放会员卡]</p>
     * @author:[王灿] 
     * @update:[日期YYYY-MM-DD] [更改人姓名]
     * @return Map<String,Object>
     */
    @Override
    public Map<String,Object> send(String accessToken,String userId,
            String cardNo,String point,String templateId,String balance,String vip,String appId)
    {
        Map<String,Object> returnMap = new HashMap<String,Object>();
        if(null == point || "".equals(point) || point.contains("-"))
        {
            point = "0";
        }
        if(StringUtils.isBlank(balance))
        {
            balance = "0";
        }
        String usefullDate[] = getUsefullDate(null);
        String bizContent = "{";
        bizContent = bizContent 
                   + "\"out_serial_no\":\""
                   + UUID.randomUUID().toString().replaceAll("-", "")
                   + "\",\"card_template_id\":\""
                   + templateId 
                   + "\",\"card_user_info\":{"      
                   + "\"user_uni_id\":\""     
                   + userId                         
                   + "\",\"user_uni_id_type\":\"UID\"}," 
                   + "\"card_ext_info\":{" 
                   + " \"external_card_no\":\""
                   + cardNo
                   + "\"," 
                   +"\"open_date\":\""
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
                   + "\"balance\":\""
                   + balance
                   + "\"" 
                   + "}}" ;
        try
        {
            AlipayMarketingCardOpenRequest request = new AlipayMarketingCardOpenRequest();
            request.setBizContent(bizContent);
            AlipayClient alipayClient = new DefaultAlipayClient(AlipayConst.serverUrl, appId, AlipayConst.private_key, AlipayConst.format, AlipayConst.input_charset, AlipayConst.ali_public_key, AlipayConst.sign_type);
            AlipayMarketingCardOpenResponse response =  alipayClient.execute(request,accessToken);
            if(null != response)
            {
                if(response.isSuccess())
                {
                    returnMap.put("bizCardNo", response.getCardInfo().getBizCardNo());
                    returnMap.put("openDate", response.getCardInfo().getOpenDate());
                }
                else 
                {
                    System.out.println("发放会员卡失败-------------------");
                    returnMap.put("msg",response.getSubMsg());
                }
            }
        }
        catch (AlipayApiException e)
        {
            e.printStackTrace();
        }
        return returnMap;
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
    
    /**
     * <p>Discription:[更改会员积分]</p>
     * @author:[王灿]
     * @update:[日期YYYY-MM-DD] [更改人姓名]
     */
    @Override
    public String updatePoint(String targetCardNo, Date openDate, String point,String vip,String appId)
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
     * <p>芝麻认证初始化接口</p>
     * @author:[王灿]
     * @update:[日期YYYY-MM-DD] [更改人姓名]
     */
    public ZhimaCustomerCertificationInitializeResponse initialize(String certName, String certNo){
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConst.serverUrl, AlipayConst.TZJ_appId, AlipayConst.TZJ_private_key, AlipayConst.format, AlipayConst.input_charset, AlipayConst.TZJ_ali_public_key, AlipayConst.sign_type);
        ZhimaCustomerCertificationInitializeRequest request = new ZhimaCustomerCertificationInitializeRequest();
        ZhimaCustomerCertificationInitializeModel model = new ZhimaCustomerCertificationInitializeModel();
        model.setTransactionId(new Date().getTime()+""+(new Random().nextInt(999999)+100000));
        model.setProductCode("w1010100000000002978");
        model.setBizCode("FACE");
        Map<String,Object> map =new HashMap<String,Object>();
        map.put("identity_type","CERT_INFO");
        map.put("cert_type","IDENTITY_CARD");
        map.put("cert_name",certName);
        map.put("cert_no",certNo);
        model.setIdentityParam(JSON.toJSONString(map));
        model.setExtBizParam(null);
        request.setBizModel(model);

        ZhimaCustomerCertificationInitializeResponse response = null;
        try{
            response = alipayClient.execute(request);
        }catch(Exception e ){
        }
        System.out.println(response.getBody());
        if (response.isSuccess()) {
            System.out.println("调用芝麻认证初始化接口成功");
        } else {
            System.out.println("调用芝麻认证初始化接口失败");
        }
        return response;
    }
    /**
     * <p>芝麻认证初返回URL</p>
     * @author:[王灿]
     * @update:[日期YYYY-MM-DD] [更改人姓名]
     */
    public ZhimaCustomerCertificationCertifyResponse getInitializeUrl(String bizNo){
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConst.serverUrl, AlipayConst.TZJ_appId, AlipayConst.TZJ_private_key, AlipayConst.format, AlipayConst.input_charset, AlipayConst.TZJ_ali_public_key, AlipayConst.sign_type);
        ZhimaCustomerCertificationCertifyRequest request1 = new ZhimaCustomerCertificationCertifyRequest();
        ZhimaCustomerCertificationCertifyModel model1 = new ZhimaCustomerCertificationCertifyModel();
        model1.setBizNo(bizNo);
        request1.setBizModel(model1);
        request1.setReturnUrl("xl://goods:8888/goodsDetail?goodsId=10011002");
        // 设置业务参数,必须要biz_no
        //request1.setBizContent("{\"biz_no\":\"ZM201611103000000888800000733621\"}");
        // 设置回调地址,必填. 如果需要直接在支付宝APP里面打开回调地址使用alipay协议
        // alipay://www.taobao.com 或者 alipays://www.taobao.com,分别对应http和https请求
        //request1.setReturnUrl("alipays://www.taobao.com");

        // 这里一定要使用GET模式
        ZhimaCustomerCertificationCertifyResponse response1=null;
        try{
            response1 = alipayClient.pageExecute(request1, "GET");

        }catch(Exception e ){

        }
        // 从body中获取URL
        String url = response1.getBody();
        System.out.println("generateCertifyUrl url:" + url);
        return response1;
    }
    /**
     * <p>芝麻认证初开始接口</p>
     * @author:[王灿]
     * @update:[日期YYYY-MM-DD] [更改人姓名]
     */
    public ZhimaCustomerCertificationQueryResponse  certify(String bizNo){
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConst.serverUrl, AlipayConst.TZJ_appId, AlipayConst.TZJ_private_key, AlipayConst.format, AlipayConst.input_charset, AlipayConst.TZJ_ali_public_key, AlipayConst.sign_type);
        ZhimaCustomerCertificationQueryRequest  request = new ZhimaCustomerCertificationQueryRequest ();
        ZhimaCustomerCertificationQueryModel model = new ZhimaCustomerCertificationQueryModel();
        model.setBizNo(bizNo);
        request.setBizModel(model);
        ZhimaCustomerCertificationQueryResponse  response = null;
       try{
            response = alipayClient.execute(request);
       }catch(Exception e){
            e.printStackTrace();
       }
        if(response.isSuccess()){
            System.out.println("调用芝麻认证初开始接口成功");
        } else {
            System.out.println("调用芝麻认证初开始接口失败");
        }
        return response;
    }
    /**
     * <p>蚂蚁森林绿色能量接口</p>
     * @author:[王灿]
     * @update:[日期YYYY-MM-DD] [更改人姓名]
     */
    @Override
    public AntMerchantExpandTradeorderSyncResponse  updateForest(String orderId){
        try{
            Order order = orderService.selectById(orderId);
            Map<String, Object> digitalMap = null;
            List<Map<String, Object>> houseList  = null;
            //判断订单是否是电器
            if((Order.TitleType.DIGITAL.getValue()+"").equals(order.getTitle().getValue()+"")){
                digitalMap = orderItemService.selectItemOne(Integer.parseInt(orderId));
            }else if((Order.TitleType.HOUSEHOLD.getValue()+"").equals(order.getTitle().getValue()+"")||(Order.TitleType.FIVEKG.getValue()+"").equals(order.getTitle().getValue()+"")){
                houseList = orderItemAchService.selectItemSumAmount(Integer.parseInt(orderId));
            }else {
                return null;
            }
            AlipayClient alipayClient = new DefaultAlipayClient("https://openapipre.alipay.com/gateway.do",AlipayConst.XappId,AlipayConst.private_key,"json","GBK",AlipayConst.ali_public_key,"RSA2");
            AntMerchantExpandTradeorderSyncRequest request = new AntMerchantExpandTradeorderSyncRequest();
            //request.putOtherTextParam("ws_service_url","mrchorder-eu95-3.rz00b.dev.alipay.net:12200");
            AntMerchantExpandTradeorderSyncModel model = new AntMerchantExpandTradeorderSyncModel();
            model.setBuyerId(order.getAliUserId());
            model.setSellerId(AlipayConst.SellerId);
            model.setOutBizType("RECYCLING");
            model.setOutBizNo(order.getOrderNo());
            List<ItemOrder> orderItemList = new ArrayList<ItemOrder>();
            //如果是电器
            if((Category.CategoryType.DIGITAL.getValue()+"").equals(order.getTitle().getValue()+"")){
                ItemOrder itemOrder = new ItemOrder();
                itemOrder.setItemName(digitalMap.get("name").toString());
                itemOrder.setQuantity((long)1);
                List<OrderExtInfo> extInfo = new ArrayList<>();
                OrderExtInfo orderExtInfo = new OrderExtInfo();
                orderExtInfo.setExtKey("ITEM_TYPE");
                orderExtInfo.setExtValue(digitalMap.get("aliItemType").toString());
                extInfo.add(orderExtInfo);
                itemOrder.setExtInfo(extInfo);
                orderItemList.add(itemOrder);
            }else if((Order.TitleType.HOUSEHOLD.getValue()+"").equals(order.getTitle().getValue()+"")||(Order.TitleType.FIVEKG.getValue()+"").equals(order.getTitle().getValue()+"")){
                for (Map<String, Object> itemMap:houseList) {
                    if (null==itemMap.get("aliItemType")){
                        continue;
                    }
                    ItemOrder itemOrder = new ItemOrder();
                    itemOrder.setItemName(itemMap.get("parentName").toString());
                    itemOrder.setQuantity((long)Math.floor((double)itemMap.get("amount")));
                    List<OrderExtInfo> extInfo = new ArrayList<>();
                    OrderExtInfo orderExtInfo = new OrderExtInfo();
                    orderExtInfo.setExtKey("ITEM_TYPE");
                    orderExtInfo.setExtValue(itemMap.get("aliItemType").toString());
                    extInfo.add(orderExtInfo);
                    itemOrder.setExtInfo(extInfo);
                    orderItemList.add(itemOrder);
                }
            }
            if (null == orderItemList || orderItemList.isEmpty() ){
                return null;
            }
            model.setItemOrderList(orderItemList);
            request.setBizModel(model);
            AntMerchantExpandTradeorderSyncResponse response = null;
            try {
                response = alipayClient.execute(request);
            }catch (Exception e){
                e.printStackTrace();
            }
            if(response.isSuccess()){
                System.out.println("调⽤成功");
            } else {
                System.out.println("调⽤失败");
                DingTalkNotify.sendAliErrorMessage(Thread.currentThread().getStackTrace()[1].getClassName()
                        ,Thread.currentThread().getStackTrace()[1].getMethodName(),"增加蚂蚁深林能量异常,订单Id是："+orderId,
                        RocketMqConst.DINGDING_ERROR,response.getBody());
            }
            return response;
        }catch (Exception e) {
            DingTalkNotify.sendAliErrorMessage(Thread.currentThread().getStackTrace()[1].getClassName()
                    ,Thread.currentThread().getStackTrace()[1].getMethodName(),"增加蚂蚁深林能量异常,订单Id是："+orderId,
                    RocketMqConst.DINGDING_ERROR,e.toString());
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {

        AlipayClient alipayClient = new DefaultAlipayClient("https://openapipre.alipay.com/gateway.do",AlipayConst.XappId,AlipayConst.private_key,"json","GBK",AlipayConst.ali_public_key,"RSA2");
        AntMerchantExpandTradeorderSyncRequest request = new AntMerchantExpandTradeorderSyncRequest();
        AntMerchantExpandTradeorderSyncModel model = new AntMerchantExpandTradeorderSyncModel();
        model.setBuyerId("2088212854989662");
        model.setSellerId(AlipayConst.SellerId);
        model.setOutBizType("RECYCLING");
        model.setOutBizNo(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+(new Random().nextInt(899999)+100000));
        List<ItemOrder> orderItemList = new ArrayList<ItemOrder>();
        ItemOrder itemOrder = new ItemOrder();
        itemOrder.setItemName("废纸一堆");
        itemOrder.setQuantity((long)1);
        List<OrderExtInfo> extInfo = new ArrayList<>();
        OrderExtInfo orderExtInfo = new OrderExtInfo();
        orderExtInfo.setExtKey("ITEM_TYPE");
        orderExtInfo.setExtValue("paper");
        extInfo.add(orderExtInfo);
        itemOrder.setExtInfo(extInfo);
        orderItemList.add(itemOrder);
        model.setItemOrderList(orderItemList);
        request.setBizModel(model);
        AntMerchantExpandTradeorderSyncResponse response = null;
        try {
            response = alipayClient.execute(request);
        }catch (Exception e){
            e.printStackTrace();
        }
        if(response.isSuccess()){
            System.out.println("调⽤成功");
        } else {
            System.out.println("调⽤失败");
        }
        System.out.println(response.getBody()); ;

    }
}
