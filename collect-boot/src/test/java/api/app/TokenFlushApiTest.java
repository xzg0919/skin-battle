package api.app;

import com.alibaba.fastjson.JSON;
import com.tzj.module.easyopen.util.ApiUtil;
import io.itit.itf.okhttp.FastHttpClient;
import io.itit.itf.okhttp.Response;

import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;

/**
 *
 * app api 刷新 token
 *
 * F7AHNFQOKPRQTKYHDWUKCR2X5IP7P4IQNNCPRN6VQNVN6NHTTULOLHZS5OTDCQQBOOX3LCUSO4NFA2KG3P2LEE7CERH5SHVK5VFUCPMGKBSVPHXPJX7LNXPNJLJJOWWPOFKMADQBRNAD2SYI72HA3BE3BYHD7V37JFHJFMPYID63OKP3HAEZPIT6H5WJXUTSYH4HGSKQ3Q3ENN2TIPP4YN6TARWO4W4DY3FTBYUV4R4RACG6DD2H3IM5WZOH5CHGROK6ZZIXTY7XE35OCBWCHNADCCJNBQ4KJU5GUEVAX7BHZQBR6LLA
 * @Author 胡方明（12795880@qq.com）
 **/
public class TokenFlushApiTest {
    public static void main(String[] args) throws Exception {
        String api="http://localhost:8080/app/api";


        HashMap<String,Object> param=new HashMap<>();
        param.put("name","app.token.flush");
        param.put("version","1.0");
        param.put("format","json");
        param.put("token","F7AHNFQOKPRQTKYHDWUKCR2X5IP7P4IQNNCPRN6VQNVN6NHTTULOLHZS5OTDCQQBOOX3LCUSO4NFA2KG3P2LEE7CERH5SHVK5VFUCPMGKBSVPHXPJX7LNXPNJLJJOWWPOFKMADQBRNAD2SYI72HA3BE3BYHD7V37JFHJFMPYID63OKP3HAEZPIT6H5WJXUTSYH4HGSKQ3Q3ENN2TIPP4YN6TARWO4W4DY3FTBYUV4R4RACG6DD2H3IM5WZOH5CHGROK6ZZIXTY7XE35OCBWCHNADCCJNBQ4KJU5GUEVAX7BHZQBR6LLA");
        param.put("app_key", "app_id_2");
        param.put("timestamp", Calendar.getInstance().getTimeInMillis());
        param.put("nonce", UUID.randomUUID().toString());

        String jsonStr = JSON.toJSONString(param);
        String sign = ApiUtil.buildSign(JSON.parseObject(jsonStr), "sign_key_55667788");
        param.put("sign", sign);

        Response response= FastHttpClient.post().url(api).body(JSON.toJSONString(param)).build().execute();
        String resultJson=response.body().string();
        System.out.println(resultJson);
    }
}
