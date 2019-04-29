package api.ali;

import com.alibaba.fastjson.JSON;
import com.tzj.collect.api.ali.param.CategoryBean;
import com.tzj.collect.api.ali.param.MemberBean;
import com.tzj.collect.api.ali.param.OrderBean;
import com.tzj.collect.api.ali.param.PageBean;
import com.tzj.module.easyopen.util.ApiUtil;
import io.itit.itf.okhttp.FastHttpClient;
import io.itit.itf.okhttp.Response;

import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;

/**
 * @Author 胡方明（12795880@qq.com）
 **/
public class TokenGetApiTest {
    public static void main(String[] args) throws Exception {
        String api="http://localhost:9090/ali/api";
        MemberBean memberBean=new MemberBean();
        memberBean.setAliMemberId("2088212384105273");
        System.out.println("A".equalsIgnoreCase("a"));
////        memberBean.setCertNo("310XXXXXXXXX");
////        memberBean.setGreenSn("130123456789");
////        //memberBean.setNickName("测试用户");
////        memberBean.setUserName("测试用户");
////
        HashMap<String,Object> param=new HashMap<>();
//        param.put("name","token.get");
//        param.put("version","1.0");  
//        param.put("format","json");
//        param.put("app_key","app_id_1");
//        param.put("timestamp", Calendar.getInstance().getTimeInMillis());
//        //param.put("token","111");
//        param.put("sign","111");
//        param.put("nonce", UUID.randomUUID());
//        param.put("data",memberBean);
//
//        String jsonStr=JSON.toJSONString(param);
//        String sign= ApiUtil.buildSign(JSON.parseObject(jsonStr),"sign_key_11223344");
//        param.put("sign",sign);
//
//        Response response= FastHttpClient.post().url(api).body(JSON.toJSONString(param)).build().execute();
//        String resultJson=response.body().string();
//        System.out.println(resultJson);
        
//        CategoryBean cb = new CategoryBean();
        CategoryBean categoryBean = new CategoryBean();
        categoryBean.setId(4);
        //orderBean.setStreetId(5);
        categoryBean.setCommunityId(1910);
        categoryBean.setTitle("HOUSEHOLD");
        PageBean page = new PageBean();
        page.setPageNumber(1);
        page.setPageSize(6);
        page.setMemberId(1);
       // orderBean.setPagebean(page);
        param.put("name","token.get");
        param.put("version","1.0");  
        param.put("format","json");
//        cb.setTitle("DIGITAL");
//        cb.setLevel(0);
        
        param.put("timestamp", Calendar.getInstance().getTimeInMillis());
//        param.put("token","F7AHNFQOKPRQTKYHDWUKCR2X5IP7P4IQNNCPRN6VQNVN6NHTTULOLHZS5OTDCQQBOOX3LCUSO4NFA2KG3P2LEE7CEQWJEMAIRCFOEPCSCWP6N5VXZUE5JRSLSAGIQADK5GV347M7FDSGP5FKG34NFYA2ULTVDEQJOCRU3CNTT6GOE3ATM4MXDWFNR5OJAVBXD2263AY7GYCIR2A3IESXHNQST3OFOHEYPYR3NTNYRR3GTPJCYX7TONQ6HYROE75NLAKN66ZED4IBJVO3TYE3IAMZWW4XBZDC4JSEA65AX7BHZQBR6LLA");
        //param.put("sign","111");
        param.put("nonce", UUID.randomUUID());
        param.put("data",memberBean);
        param.put("app_key", "app_id_1");

        String jsonStr2=JSON.toJSONString(param);
        String sign= ApiUtil.buildSign(JSON.parseObject(jsonStr2),"sign_key_11223344");
        param.put("sign",sign);
        System.out.println(JSON.toJSONString(param));
        Response response2= FastHttpClient.post().url(api).body(JSON.toJSONString(param)).build().execute();
        String resultJson2=response2.body().string();
        System.out.println(resultJson2);
    }
	
}
