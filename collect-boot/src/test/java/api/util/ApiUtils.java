package api.util;

import com.alibaba.fastjson.JSON;
import com.tzj.collect.api.param.TokenBean;
import io.itit.itf.okhttp.FastHttpClient;
import io.itit.itf.okhttp.Response;
import org.apache.commons.lang3.StringUtils;

import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;

import static com.alibaba.fastjson.serializer.SerializerFeature.MapSortField;
import static com.alibaba.fastjson.serializer.SerializerFeature.SortField;

public class ApiUtils {
    public static String createCommonParam(String appid,String gateway,String apiName, Object data, String token, String signKey) throws Exception {
        HashMap<String, Object> param = new HashMap<>();
        param.put("app_key", appid);
        param.put("name", apiName);
        param.put("version", "1.0");
        param.put("format", "json");
        param.put("timestamp", Calendar.getInstance().getTimeInMillis());
        param.put("nonce", UUID.randomUUID());

        if (StringUtils.isNotBlank(token)) {
            param.put("token", token);
        }

        if (data != null) {
            param.put("data", data);
        }else{
            param.put("data",new HashMap<>());
        }

        if (StringUtils.isNotBlank(signKey)) {
            String jsonStr = JSON.toJSONString(param,MapSortField,SortField);
            String sign = SignUtil.buildSign(JSON.parseObject(jsonStr), signKey);
            param.put("sign", sign);
        }

        Response response = FastHttpClient.post().url(gateway).body(JSON.toJSONString(param,MapSortField,SortField)).build().execute();
        String resultJson = response.body().string();
        return resultJson;
    }

    public static String createCommonParam(String appid,String gateway,String apiName, Object data, TokenBean tokenBean) throws Exception {
        if(tokenBean==null){
            throw new RuntimeException("tokenBean 不能为空！！！");
        }
        return createCommonParam(appid,gateway,apiName,data,tokenBean.getToken(),tokenBean.getSignKey());
    }
}
