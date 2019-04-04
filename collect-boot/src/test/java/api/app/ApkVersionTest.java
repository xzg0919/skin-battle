package api.app;

import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;

import com.alibaba.fastjson.JSON;
import com.tzj.collect.api.app.param.VersionBean;
import com.tzj.module.easyopen.util.ApiUtil;

import io.itit.itf.okhttp.FastHttpClient;
import io.itit.itf.okhttp.Response;

public class ApkVersionTest {

	  public static void main(String[] args) throws Exception {
	        String api = "http://localhost:8080/app/api";
	        VersionBean v = new VersionBean();
	        v.setApkType("3");
	        HashMap<String, Object> param = new HashMap<>();
	        param.put("name", "app.apk.appVersion");
	        param.put("version", "1.0");
	        param.put("format", "json");
	        param.put("app_key", "app_id_2");
	        param.put("timestamp", Calendar.getInstance().getTimeInMillis());
	        param.put("token","F7AHNFQOKPRQTKYHDWUKCR2X5IP7P4IQNNCPRN6VQNVN6NHTTULOLHZS5OTDCQQBOOX3LCUSO4NFA2KG3P2LEE7CERH5SHVK5VFUCPOXZQCI3LOIHV2DWXWGK4CCO5LLCEWKAF5VYVHGZQ5AZZYLVIXULUXEI3NYVOQTCH4DDMPDNGNAOBXW5E5J4FMJAMFEFL4TPERVUJA7UQMU3ZZPDWIHM4WXW6KONU6AZE3ZTUW7GGLIIBEE4R53BUTXZIWYRFUZE3SSHF6DG3IPG5BLB3RPVBXVOYQ5VLZSFZNAX7BHZQBR6LLA");
	        //param.put("sign","111");
	        param.put("nonce", UUID.randomUUID().toString());
	        param.put("data", v);

	        String jsonStr = JSON.toJSONString(param);
	        String sign = ApiUtil.buildSign(JSON.parseObject(jsonStr), "sign_key_55667788");
	        param.put("sign", sign);

	        Response response = FastHttpClient.post().url(api).body(JSON.toJSONString(param)).build().execute();
	        String resultJson = response.body().string();
	        System.out.println(resultJson);
	  }
}
