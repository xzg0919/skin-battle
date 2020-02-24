
/**
 * @Title: BusinessOrderApiTest.java
 * @date 2018年3月20日 下午3:20:45
 * @version V1.0
 * @author:[王池]
 */

package app;

import com.alibaba.fastjson.JSON;
import com.tzj.green.param.*;
import com.tzj.module.api.utils.JwtUtils;
import com.tzj.module.api.utils.SignUtils;
import com.tzj.module.common.utils.security.CipherTools;
import com.tzj.module.easyopen.util.ApiUtil;
import io.itit.itf.okhttp.FastHttpClient;
import io.itit.itf.okhttp.Response;
import io.jsonwebtoken.Claims;

import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;

import static com.tzj.green.common.content.TokenConst.*;

/**
 * @ClassName: BusinessOrderApiTest
 * @date 2018年3月20日 下午3:20:45
 * @author:[王池]
 */

public class AppTest {
	public static void main(String[] args) throws Exception {
		String token = JwtUtils.generateToken("1", APP_API_EXPRIRE, APP_API_TOKEN_SECRET_KEY);
		String securityToken = JwtUtils.generateEncryptToken(token, APP_API_TOKEN_CYPTO_KEY);
		System.out.println("生成的token是："+securityToken);

		String tokenCyptoKey = APP_API_TOKEN_CYPTO_KEY;
		String key = CipherTools.initKey(tokenCyptoKey);
		String decodeToken = CipherTools.decrypt(securityToken, key);
		Claims claims = JwtUtils.getClaimByToken(decodeToken, APP_API_TOKEN_SECRET_KEY);
		String subjectStr = claims.getSubject();
		System.out.println("反向編譯 token是："+subjectStr);

//		String api = "http://test.tcompanypoint.mayishoubei.com/company/api";

		String api="http://localhost:9090/app/api";

		RecyclersLoginBean recyclersLoginBean =new RecyclersLoginBean();

		recyclersLoginBean.setMobile("18375336389");

		HashMap<String,Object> param=new HashMap<>();
		param.put("name","app.getCompanyList");
		param.put("version","1.0");
		param.put("format","json");
		param.put("app_key","app_id_2");
		param.put("timestamp", Calendar.getInstance().getTimeInMillis());
		param.put("token",securityToken);
		//param.put("sign","111");
		param.put("nonce", UUID.randomUUID().toString());
		param.put("data",null);

		String signKey = SignUtils.produceSignKey(token, APP_API_TOKEN_SIGN_KEY);
		System.out.println(signKey);
		String jsonStr = JSON.toJSONString(param);
		String sign = ApiUtil.buildSign(JSON.parseObject(jsonStr), "sign_key_55667788");
		param.put("sign", sign);

		System.out.println("请求的参数是 ："+JSON.toJSONString(param));
		Response response= FastHttpClient.post().url(api).body(JSON.toJSONString(param)).build().execute();
		String resultJson=response.body().string();
		System.out.println("返回的参数是 ："+resultJson);
	}

}
