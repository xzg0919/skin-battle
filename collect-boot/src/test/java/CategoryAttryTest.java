import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;

import com.alibaba.fastjson.JSON;
import com.tzj.collect.api.ali.param.CategoryBean;
import com.tzj.collect.api.ali.param.PageBean;
import com.tzj.module.easyopen.util.ApiUtil;

import io.itit.itf.okhttp.FastHttpClient;
import io.itit.itf.okhttp.Response;

public class CategoryAttryTest {
	public static void main(String[] args) throws Exception {
        String api="http://localhost:8080/ali/api";
        PageBean pageBean=new PageBean();
        pageBean.setPageNumber(1);
        pageBean.setPageSize(20);
        CategoryBean categoryBean = new CategoryBean();
        categoryBean.setId(2);
        categoryBean.setPageBean(pageBean);

        HashMap<String,Object> param=new HashMap<>();
        param.put("name","categoryAttr.listCategoryAttrs");
        param.put("version","1.0");  
        param.put("format","json");
        param.put("app_key","app_id_1");
        param.put("timestamp", Calendar.getInstance().getTimeInMillis());
        param.put("token","3F3TEMH74565Q5QORHNPE76UZM6VT4JPWVV4OPUNTGAXLLRLC6B5GYU3LW34YHVNOEFL2LXPVT24UAJWCBI7NJ42KSYJ2KXG2OVQSA6ZMU4VMMCLQUKIRXAWTX2BD3K6MDOZDBJ4Q62CYGOB7DVAUP4CYQAHL3JSQRIG7P2UO77IZBN7W3E4RZK42VEEUWCHGAZLS7LGRB4EVIIYSQVYYSGAETEUZC4JUVVV2UDRKIOBGXURUGYCOGKTBVFLZYU2QFPF2G4I7DVNKBWCOFWBQDLZLJYEDSPIL6T46KLPZ4O2ZIFJROTQ");
        //param.put("sign","111");
        param.put("nonce", UUID.randomUUID().toString());
        param.put("data",categoryBean);

        String jsonStr=JSON.toJSONString(param);
        String sign= ApiUtil.buildSign(JSON.parseObject(jsonStr),"sign_key_11223344");
        param.put("sign",sign);

        Response response= FastHttpClient.post().url(api).body(JSON.toJSONString(param)).build().execute();
        String resultJson=response.body().string();
        System.out.println(resultJson);
    }
}
