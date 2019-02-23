
/**
 * @Title: BusinessOrderApiTest.java
 * @date 2018年3月20日 下午3:20:45
 * @version V1.0
 * @author:[王池]
 */

package api.business;

import com.alibaba.fastjson.JSON;
import com.tzj.collect.api.admin.param.CompanyBean;
import com.tzj.collect.api.ali.param.AreaBean;
import com.tzj.collect.api.business.param.RecyclersServiceRangeBean;
import com.tzj.collect.api.business.param.TitleBean;
import com.tzj.module.api.utils.JwtUtils;
import com.tzj.module.common.utils.security.CipherTools;
import com.tzj.module.easyopen.util.ApiUtil;
import io.itit.itf.okhttp.FastHttpClient;
import io.itit.itf.okhttp.Response;
import io.jsonwebtoken.Claims;

import java.util.*;

import static com.tzj.collect.common.constant.TokenConst.*;

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

		String api="http://localhost:9090/business/api";
		RecyclersServiceRangeBean recyclersServiceRangeBean = new RecyclersServiceRangeBean();
		recyclersServiceRangeBean.setRecycleId("115");
		recyclersServiceRangeBean.setCityId("737");
		recyclersServiceRangeBean.setIsEnable("0");

		List<AreaBean> areaList = new ArrayList<>();
		AreaBean areaBean1 = new AreaBean();
		areaBean1.setAreaId("241");
		areaBean1.setStreeId("513");
		AreaBean areaBean2 = new AreaBean();
		areaBean2.setAreaId("241");
		areaBean2.setStreeId("514");
		AreaBean areaBean3 = new AreaBean();
		areaBean3.setAreaId("242");
		areaBean3.setStreeId("521");
		AreaBean areaBean4 = new AreaBean();
		areaBean4.setAreaId("242");
		areaBean4.setStreeId("522");
		areaList.add(areaBean1);
		areaList.add(areaBean2);
		areaList.add(areaBean3);
		areaList.add(areaBean4);

		List<TitleBean> TitleList = new ArrayList<>();
		TitleBean titleBean1 = new TitleBean();
		titleBean1.setTitleId("1");
		titleBean1.setTitleName("家电数码");
		TitleBean titleBean2 = new TitleBean();
		titleBean2.setTitleId("2");
		titleBean2.setTitleName("生活垃圾");
		TitleBean titleBean3 = new TitleBean();
		titleBean3.setTitleId("3");
		titleBean3.setTitleName("废纺衣物5公斤起收");
		TitleList.add(titleBean1);
		TitleList.add(titleBean2);
		TitleList.add(titleBean3);

		recyclersServiceRangeBean.setTitleList(TitleList);
		recyclersServiceRangeBean.setAreaList(areaList);

		RecyclersServiceRangeBean recyclersServiceRangeBeans = new RecyclersServiceRangeBean();
		recyclersServiceRangeBeans.setRecycleId("115");



		HashMap<String,Object> param=new HashMap<>();
		param.put("name","business.title.getRecyclerTitleList");
		param.put("version","1.0");
		param.put("format","json");
		param.put("app_key","app_id_3");
		param.put("timestamp", Calendar.getInstance().getTimeInMillis());
		param.put("token",securityToken);
		//param.put("sign","111");
		param.put("nonce", UUID.randomUUID().toString());
		param.put("data",recyclersServiceRangeBeans);

		String jsonStr = JSON.toJSONString(param);
		String sign = ApiUtil.buildSign(JSON.parseObject(jsonStr), "sign_key_99aabbcc");
		param.put("sign", sign);

		System.out.println("请求的参数是 ："+JSON.toJSONString(param));
		Response response= FastHttpClient.post().url(api).body(JSON.toJSONString(param)).build().execute();
		String resultJson=response.body().string();
		System.out.println("返回的参数是 ："+resultJson);
	}

}
