
/**
 * @Title: BusinessOrderApiTest.java
 * @date 2018年3月20日 下午3:20:45
 * @version V1.0
 * @author:[王池]
 */

package companyTest;

import com.alibaba.fastjson.JSON;
import com.tzj.green.entity.CommunityHouseName;
import com.tzj.green.entity.Goods;
import com.tzj.green.param.*;
import com.tzj.module.api.utils.JwtUtils;
import com.tzj.module.api.utils.SignUtils;
import com.tzj.module.common.utils.security.CipherTools;
import com.tzj.module.easyopen.util.ApiUtil;
import io.itit.itf.okhttp.FastHttpClient;
import io.itit.itf.okhttp.Response;
import io.jsonwebtoken.Claims;

import java.util.*;

import static com.tzj.green.common.content.TokenConst.*;

/**
 * @ClassName: BusinessOrderApiTest
 * @date 2018年3月20日 下午3:20:45
 * @author:[王池]
 */

public class BusinessOrderApiTest {
	public static void main(String[] args) throws Exception {
		String token = JwtUtils.generateToken("1", BUSINESS_API_EXPRIRE, BUSINESS_API_TOKEN_SECRET_KEY);
		String securityToken = JwtUtils.generateEncryptToken(token, BUSINESS_API_TOKEN_CYPTO_KEY);
		System.out.println("生成的token是："+securityToken);

		String tokenCyptoKey = BUSINESS_API_TOKEN_CYPTO_KEY;
		String key = CipherTools.initKey(tokenCyptoKey);
		String decodeToken = CipherTools.decrypt(securityToken, key);
		Claims claims = JwtUtils.getClaimByToken(decodeToken, BUSINESS_API_TOKEN_SECRET_KEY);
		String subjectStr = claims.getSubject();
		System.out.println("反向編譯 token是："+subjectStr);


		String api="http://localhost:9090/app/api";
		Map<String, Object> paramMap = new HashMap<>();
//		paramMap.put("pointType", 0);
//		List<Map<String, Object>> mapList = new ArrayList<>();
//		Map<String, Object> map = new HashMap<>();
//		map.put("amount", 2);
//		map.put("point", 1);
//		map.put("categoryId", 5);
//		map.put("categoryName", "废纸");
//		map.put("parentId", 4);
//		map.put("parentName", "可回收垃圾");
//		mapList.add(map);
		paramMap.put("id", "2");
//		paramMap.put("userName", "郑东东");
//		paramMap.put("aliUserId", "21312313");
//		paramMap.put("points", 2);
//		paramMap.put("pointList", mapList);
		HashMap<String,Object> param=new HashMap<>();
		param.put("name","app.goods.list.id");
		param.put("version","1.0");
		param.put("format","json");
		param.put("app_key","app_id_2");
		param.put("timestamp", Calendar.getInstance().getTimeInMillis());
		param.put("token", "F7AHNFQOKPRQTKYHDWUKCR2X5IP7P4IQNNCPRN6VQNVN6NHTTULOLHZS5OTDCQQBOOX3LCUSO4NFA2KG3P2LEE7CER4EQ6GQIWG5SXSKTY4TV6P26Y544X3OXL5UQXLGKKUYB6T5QIB34FOTP77UXKPHU4NIKRIOJOIT2R4SVG2CWJ5HJEMFFJ6KEX66QBY3EBG5EHUIGUSSE6DABOHKDTMFZQOO3KFBC2F6UTKMM5EPKY3YAM6TTQHBCJSVNR2SPG7OENK2KT62DWRYYEH7F5SUG74O4D7FXXUPQ3FAX7BHZQBR6LLA");
		//param.put("sign","111");
		param.put("nonce", UUID.randomUUID().toString());
		param.put("data",paramMap);

		String signKey = SignUtils.produceSignKey(token, "sign_key_55667788");
		System.out.println(signKey);
		String jsonStr = JSON.toJSONString(param);
		String sign = ApiUtil.buildSign(JSON.parseObject(jsonStr), "sign_key_55667788");
		System.out.println(sign);
		param.put("sign", sign);

		System.out.println("请求的参数是 ："+JSON.toJSONString(param));
		Response response= FastHttpClient.post().url(api).body(JSON.toJSONString(param)).build().execute();
		String resultJson=response.body().string();
		System.out.println("返回的参数是 ："+resultJson);
	}

}
