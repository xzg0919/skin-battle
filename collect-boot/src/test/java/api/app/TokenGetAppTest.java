package api.app;

import com.alibaba.fastjson.JSON;
import com.tzj.collect.api.ali.param.CategoryBean;
import com.tzj.collect.api.ali.param.OrderBean;
import com.tzj.collect.api.ali.param.PageBean;
import com.tzj.collect.api.app.param.RecyclersBean;
import com.tzj.collect.api.app.param.RecyclersLoginBean;
import com.tzj.collect.api.app.result.AppCompany;
import com.tzj.collect.entity.Category;
import com.tzj.module.api.utils.JwtUtils;
import com.tzj.module.common.utils.security.CipherTools;
import com.tzj.module.easyopen.file.FileBase64Param;
import com.tzj.module.easyopen.util.ApiUtil;
import io.itit.itf.okhttp.FastHttpClient;
import io.itit.itf.okhttp.Response;
import io.jsonwebtoken.Claims;

import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;

import static com.tzj.collect.common.constant.TokenConst.*;

/**
 *
 * APP 端 token api 测试代码
 *
 * @Author 胡方明（12795880@qq.com）
 **/
public class TokenGetAppTest {
	 public static void main(String[] args) throws Exception {

		 String token = JwtUtils.generateToken("408", APP_API_EXPRIRE, APP_API_TOKEN_SECRET_KEY);
		 String securityToken = JwtUtils.generateEncryptToken(token, APP_API_TOKEN_CYPTO_KEY);
		 System.out.println("token : "+securityToken);

		 String tokenCyptoKey = APP_API_TOKEN_CYPTO_KEY;
		 String key = CipherTools.initKey(tokenCyptoKey);
		 String decodeToken = CipherTools.decrypt(securityToken, key);
		 Claims claims = JwtUtils.getClaimByToken(decodeToken, APP_API_TOKEN_SECRET_KEY);
		 String subjectStr = claims.getSubject();
		 System.out.println("反向編譯 token是："+subjectStr);
		 // String api="http://localhost:9000/app/api";
		  String api="http://localhost:9090/app/api";

		 	OrderBean orderBean = new OrderBean();
				 orderBean.setId(8941);
		 orderBean.setAchPrice("0.0");

		 CategoryBean categoryBean = new CategoryBean();
		 categoryBean.setIsCash("1");
		 categoryBean.setId(68);




	        HashMap<String,Object> param=new HashMap<>();
	        param.put("name", "app.category.getTowCategoryList");
	        param.put("version", "1.0");
	        param.put("format", "json");
	        param.put("app_key", "app_id_2");
	        param.put("timestamp", Calendar.getInstance().getTimeInMillis());
	        param.put("token",securityToken);
	        //param.put("sign","111");
	        param.put("nonce", UUID.randomUUID().toString());
	        param.put("data", categoryBean);

	        String jsonStr = JSON.toJSONString(param);
	        String sign = ApiUtil.buildSign(JSON.parseObject(jsonStr), "sign_key_55667788");
	        param.put("sign", sign);


		    System.out.println("请求的参数是: "+JSON.toJSONString(param));
		    Response response= FastHttpClient.post().url(api).body(JSON.toJSONString(param)).build().execute();
	        String resultJson=response.body().string();
	        System.out.println("返回的结果是: "+resultJson);
	    }
}
