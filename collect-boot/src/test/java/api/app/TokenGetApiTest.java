package api.app;

import com.alibaba.fastjson.JSON;
import com.tzj.collect.api.app.param.RecyclersLoginBean;
import com.tzj.module.easyopen.util.ApiUtil;
import io.itit.itf.okhttp.FastHttpClient;
import io.itit.itf.okhttp.Response;

import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;

/**
 *
 * APP 端 token api 测试代码
 *  appSecretStore.put("app_id_2", "sign_key_55667788");
 * @Author 胡方明（12795880@qq.com）
 **/
public class TokenGetApiTest {
    public static void main(String[] args) throws Exception {
        String api = "http://localhost:8080/app/api";
        RecyclersLoginBean recyclersLoginBean = new RecyclersLoginBean();
        recyclersLoginBean.setMobile("1339155555");
        recyclersLoginBean.setCaptcha("123456");

        HashMap<String, Object> param = new HashMap<>();
        param.put("name", "app.token.get");
        param.put("version", "1.0");
        param.put("format", "json");
        param.put("app_key", "app_id_2");
        param.put("timestamp", Calendar.getInstance().getTimeInMillis());
        //param.put("token","111");
        //param.put("sign","111");
        param.put("nonce", UUID.randomUUID().toString());
        param.put("data", recyclersLoginBean);

        String jsonStr = JSON.toJSONString(param);
        String sign = ApiUtil.buildSign(JSON.parseObject(jsonStr), "sign_key_55667788");
        param.put("sign", sign);

        Response response = FastHttpClient.post().url(api).body(JSON.toJSONString(param)).build().execute();
        String resultJson = response.body().string();
        System.out.println(resultJson);
    }
}
