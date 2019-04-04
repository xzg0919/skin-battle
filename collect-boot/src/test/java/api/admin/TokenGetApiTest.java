package api.admin;

import com.alibaba.fastjson.JSON;
import com.tzj.collect.api.admin.param.AdminBean;
import com.tzj.collect.api.ali.param.CategoryBean;
import com.tzj.collect.api.ali.param.MemberBean;
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
        String api="http://localhost:8080/admin/api";
        AdminBean bean= new AdminBean();
        bean.setUsername("admin");
        bean.setPassword("admin");
        HashMap<String,Object> param=new HashMap<>();
        param.put("name","admin.token.get");
        param.put("version","1.0");  
        param.put("format","json");
        param.put("app_key","app_id_4");
        param.put("timestamp", Calendar.getInstance().getTimeInMillis());
        param.put("token","3F3TEMH74565Q5QORHNPE76UZM6VT4JPWVV4OPUNTGAXLLRLC6B5GYU3LW34YHVNOEFL2LXPVT24UAJWCBI7NJ42KSYJ2KXG2OVQSAZAZYR4BLDF6Z2YRXAWTX2BD3K6R5IJ6KH3W7J2V2CMWD35IJSN67Y2AN6MNDYI2GYDKK5O6L3VGVJ22JWGBNL7FUMBVYY3NZNCCZQJBUJPBBE4CVJ6WK47Y2JGGF3JWQD4RTP3GBIGWDVHNJFKA5NV7L3X3JQ2OW7PKUYF6WIEQMM6HUZNT2NF7TLFBJWASA3PZ4O2ZIFJROTQ");
        //param.put("sign","111");
        param.put("nonce", UUID.randomUUID().toString());
        param.put("data",bean);

        String jsonStr=JSON.toJSONString(param);
        String sign= ApiUtil.buildSign(JSON.parseObject(jsonStr),"sign_key_998877");
        param.put("sign",sign);

        Response response= FastHttpClient.post().url(api).body(JSON.toJSONString(param)).build().execute();
        String resultJson=response.body().string();
        System.out.println(resultJson);
    }
	
}
