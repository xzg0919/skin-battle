import java.math.BigDecimal;
import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;

import com.alibaba.fastjson.JSON;
import com.tzj.collect.api.ali.param.CategoryAttrBean;
import com.tzj.collect.api.ali.param.CategoryBean;
import com.tzj.collect.api.ali.param.OrderBean;
import com.tzj.collect.api.ali.param.OrderItemBean;
import com.tzj.collect.api.ali.param.PageBean;
import com.tzj.collect.entity.OrderPic;
import com.tzj.module.easyopen.util.ApiUtil;

import io.itit.itf.okhttp.FastHttpClient;
import io.itit.itf.okhttp.Response;

public class CategoryTest {
	/*public static void main(String[] args) throws Exception {
    String api="http://localhost:8080/ali/api";
    PageBean pageBean=new PageBean();
    pageBean.setPageNumber(1);
    pageBean.setPageSize(20);
    OrderBean orderbean = new OrderBean();
    orderbean.setCompanyId(1);
    orderbean.setRecyclerId(1);
    orderbean.setAreaId(1);
    orderbean.setCommunityId(1);
    orderbean.setAddress("地址");
    orderbean.setTel("电话");
    orderbean.setCategoryId(2);
    orderbean.setPrice(new BigDecimal("555").setScale(2, BigDecimal.ROUND_HALF_UP));
    orderbean.setUnit("计量单位");
    orderbean.setQty(9999);
    orderbean.setLevel("0");
    OrderItemBean  orderItemBean = new OrderItemBean();
    orderItemBean.setCategoryAttrOppIds("1,2,3,4,5");
    OrderPic  orderPic = new OrderPic();
    orderPic.setPicUrl("http://b.tingzhijun.com/coupons/upload/201706/4a6628b1b48b41b5a31ad3b337836959/9c819dc8ca254914ae0c62a1e99e2b10.jpg");
    orderPic.setSmallPic("http://b.tingzhijun.com/coupons/upload/201706/4a6628b1b48b41b5a31ad3b337836959/9c819dc8ca254914ae0c62a1e99e2b10.jpg");
    orderPic.setOrigPic("http://b.tingzhijun.com/coupons/upload/201706/4a6628b1b48b41b5a31ad3b337836959/9c819dc8ca254914ae0c62a1e99e2b10.jpg");
    orderbean.setOrderItemBean(orderItemBean);
    orderbean.setOrderPic(orderPic);

    HashMap<String,Object> param=new HashMap<>();
    param.put("name","order.create");
    param.put("version","1.0");  
    param.put("format","json");
    param.put("app_key","app_id_1");
    param.put("timestamp", Calendar.getInstance().getTimeInMillis());
    param.put("token","3F3TEMH74565Q5QORHNPE76UZM6VT4JPWVV4OPUNTGAXLLRLC6B5GYU3LW34YHVNOEFL2LXPVT24UAJWCBI7NJ42KSYJ2KXG2OVQSA6ZMU4VMMCLQUKIRXAWTX2BD3K6MDOZDBJ4Q62CYGOB7DVAUP4CYQAHL3JSQRIG7P2UO77IZBN7W3E4RZK42VEEUWCHGAZLS7LGRB4EVIIYSQVYYSGAETEUZC4JUVVV2UDRKIOBGXURUGYCOGKTBVFLZYU2QFPF2G4I7DVNKBWCOFWBQDLZLJYEDSPIL6T46KLPZ4O2ZIFJROTQ");
    //param.put("sign","111");
    param.put("nonce", UUID.randomUUID().toString());
    param.put("data",orderbean);

    String jsonStr=JSON.toJSONString(param);
    String sign= ApiUtil.buildSign(JSON.parseObject(jsonStr),"sign_key_11223344");
    param.put("sign",sign);

    Response response= FastHttpClient.post().url(api).body(JSON.toJSONString(param)).build().execute();
    String resultJson=response.body().string();
    System.out.println(resultJson);
}*/
	public static void main(String[] args) throws Exception {
        String api="http://localhost:8080/ali/api";
        CategoryAttrBean categoryAttrBean = new CategoryAttrBean(); 
        categoryAttrBean.setCategoryId((long)2);
        categoryAttrBean.setCategoryAttrOptionId("1,2,3,4,5");
        CategoryBean categoryBean = new CategoryBean();
        categoryBean.setId(1);
        OrderBean orderbean = new OrderBean();
        orderbean.setId(13);
        orderbean.setCancelReason("取消原因，测试");
        PageBean pageBean = new PageBean();
        pageBean.setPageNumber(1);
        pageBean.setPageSize(20);
        
        HashMap<String,Object> param=new HashMap<>();
        param.put("name","order.cancel");
        param.put("version","1.0");  
        param.put("format","json");
        param.put("app_key","app_id_1");
        param.put("timestamp", Calendar.getInstance().getTimeInMillis());
        param.put("token","3F3TEMH74565Q5QORHNPE76UZM6VT4JPWVV4OPUNTGAXLLRLC6B5GYU3LW34YHVNOEFL2LXPVT24UAJWCBI7NJ42KSYJ2KXG2OVQSA6ZMU4VMMCLQUKIRXAWTX2BD3K6MDOZDBJ4Q62CYGOB7DVAUP4CYQAHL3JSQRIG7P2UO77IZBN7W3E4RZK42VEEUWCHGAZLS7LGRB4EVIIYSQVYYSGAETEUZC4JUVVV2UDRKIOBGXURUGYCOGKTBVFLZYU2QFPF2G4I7DVNKBWCOFWBQDLZLJYEDSPIL6T46KLPZ4O2ZIFJROTQ");
        //param.put("sign","111");
        param.put("nonce", UUID.randomUUID().toString());
        param.put("data",orderbean);

        String jsonStr=JSON.toJSONString(param);
        String sign= ApiUtil.buildSign(JSON.parseObject(jsonStr),"sign_key_11223344");
        param.put("sign",sign);

        Response response= FastHttpClient.post().url(api).body(JSON.toJSONString(param)).build().execute();
        String resultJson=response.body().string();
        System.out.println(resultJson);
    }
}
