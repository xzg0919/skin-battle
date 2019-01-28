package api.admin;

import com.alibaba.fastjson.JSON;
import com.tzj.collect.api.admin.param.AdminCommunityBean;
import com.tzj.module.easyopen.util.ApiUtil;
import io.itit.itf.okhttp.FastHttpClient;
import io.itit.itf.okhttp.Response;

import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;

/**
 * 
 * @author zhangqiang
 *
 */
public class AreaApiTest {
    public static void main(String[] args) throws Exception {
        String api="http://localhost:8080/admin/api";
        AdminCommunityBean adminAreaBean = new AdminCommunityBean();
        
        adminAreaBean.setCountyId("7");
        adminAreaBean.setStreetId("77");
        adminAreaBean.setCommunityId("3");
        HashMap<String,Object> param=new HashMap<>();
        param.put("name","admin.area.getcomlist");
        param.put("version","1.0");
        param.put("format","json");
        param.put("token","F7AHNFQOKPRQTKYHDWUKCR2X5IP7P4IQNNCPRN6VQNVN6NHTTULOLHZS5OTDCQQBOOX3LCUSO4NFA2KG3P2LEE7CERH5SHVK5VFUCPIWVF7FTVZNCPSETCYWSIQUPXLGOFKMADQBRNAD3WA5QFHLLJYVHVHWSUDMQVNDVCVRZE4RUOLVUQBC63GQI4DTF5WKH7UBKDWL4LFJSOLLQZCZPOSELKKAZNC6HPFVSBUHIJPEBR2CYXGOWNFEBSK7U6QDXV3ZGNUG5G776EEZFAQNUU2B5LGKS42BZE7BIVVAX7BHZQBR6LLA");
        param.put("app_key", "app_id_4");
        param.put("timestamp", Calendar.getInstance().getTimeInMillis());
        param.put("nonce", UUID.randomUUID().toString());
        param.put("data", adminAreaBean);
        String jsonStr = JSON.toJSONString(param);
        String sign = ApiUtil.buildSign(JSON.parseObject(jsonStr), "sign_key_998877");
        param.put("sign", sign);
        System.out.println(jsonStr);
        Response response= FastHttpClient.post().url(api).body(JSON.toJSONString(param)).build().execute();
        String resultJson=response.body().string();
        System.out.println(resultJson);
    }
}
