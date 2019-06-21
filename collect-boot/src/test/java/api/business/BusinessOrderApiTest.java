
/**
 * @Title: BusinessOrderApiTest.java
 * @date 2018年3月20日 下午3:20:45
 * @version V1.0
 * @author:[王池]
 */

package api.business;

import static com.tzj.collect.common.constant.TokenConst.BUSINESS_API_EXPRIRE;
import static com.tzj.collect.common.constant.TokenConst.BUSINESS_API_TOKEN_CYPTO_KEY;
import static com.tzj.collect.common.constant.TokenConst.BUSINESS_API_TOKEN_SECRET_KEY;

import java.util.*;

import com.alibaba.fastjson.JSON;
import com.tzj.collect.api.admin.param.CompanyBean;
import com.tzj.collect.api.ali.param.AreaBean;
import com.tzj.collect.api.business.param.BOrderBean;
import com.tzj.collect.api.business.param.CommunityBean;
import com.tzj.collect.api.business.param.RecyclersServiceRangeBean;
import com.tzj.module.api.utils.JwtUtils;
import com.tzj.module.common.utils.security.CipherTools;
import com.tzj.module.easyopen.util.ApiUtil;

import io.itit.itf.okhttp.FastHttpClient;
import io.itit.itf.okhttp.Response;
import io.jsonwebtoken.Claims;

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
		recyclersServiceRangeBean.setRecycleId("1");
//		recyclersServiceRangeBean.setCityId("737");
		recyclersServiceRangeBean.setTitle("4");
//		List<AreaBean> areaBeanList = new ArrayList<>();
//		AreaBean areaBean1 = new AreaBean();
//		areaBean1.setAreaId("246");
//		areaBean1.setStreeId("568");
//		areaBean1.setSaveOrDelete("0");
//		List<CommunityBean> communityList1 = new ArrayList<>();
//		CommunityBean communityBean1 = new CommunityBean();
//		communityBean1.setCommunityId("20141");
//		communityBean1.setSaveOrDelete("1");
//		CommunityBean communityBean2 = new CommunityBean();
//		communityBean2.setCommunityId("20145");
//		communityBean2.setSaveOrDelete("0");
//		communityList1.add(communityBean1);
//		communityList1.add(communityBean2);
//		areaBean1.setCommunityIdList(communityList1);
//
//		AreaBean areaBean2 = new AreaBean();
//		areaBean2.setAreaId("246");
//		areaBean2.setStreeId("569");
//		areaBean2.setSaveOrDelete("0");
//		List<CommunityBean> communityList2 = new ArrayList<>();
//		CommunityBean communityBean3 = new CommunityBean();
//		communityBean3.setCommunityId("20142");
//		communityBean3.setSaveOrDelete("0");
//		CommunityBean communityBean4 = new CommunityBean();
//		communityBean4.setCommunityId("20146");
//		communityBean4.setSaveOrDelete("0");
//		communityList2.add(communityBean3);
//		communityList2.add(communityBean4);
//		areaBean2.setCommunityIdList(communityList2);
//
//		AreaBean areaBean3 = new AreaBean();
//		areaBean3.setAreaId("253");
//		areaBean3.setStreeId("673");
//		areaBean3.setSaveOrDelete("0");
//		List<CommunityBean> communityList3 = new ArrayList<>();
//		CommunityBean communityBean5 = new CommunityBean();
//		communityBean5.setCommunityId("20143");
//		communityBean5.setSaveOrDelete("0");
//		CommunityBean communityBean6 = new CommunityBean();
//		communityBean6.setCommunityId("20147");
//		communityBean6.setSaveOrDelete("0");
//		communityList3.add(communityBean5);
//		communityList3.add(communityBean6);
//		areaBean3.setCommunityIdList(communityList3);
//
//
//		AreaBean areaBean4 = new AreaBean();
//		areaBean4.setAreaId("253");
//		areaBean4.setStreeId("670");
//		areaBean4.setSaveOrDelete("0");
//		List<CommunityBean> communityList4 = new ArrayList<>();
//		CommunityBean communityBean7 = new CommunityBean();
//		communityBean7.setCommunityId("20144");
//		communityBean7.setSaveOrDelete("0");
//		CommunityBean communityBean8 = new CommunityBean();
//		communityBean8.setCommunityId("20148");
//		communityBean8.setSaveOrDelete("0");
//		communityList4.add(communityBean7);
//		communityList4.add(communityBean8);
//		areaBean4.setCommunityIdList(communityList4);
//
//		areaBeanList.add(areaBean1);
//		areaBeanList.add(areaBean2);
//		areaBeanList.add(areaBean3);
//		areaBeanList.add(areaBean4);
//		recyclersServiceRangeBean.setAreaList(areaBeanList);

		BOrderBean bOrderBean = new BOrderBean();
		bOrderBean.setOrderId("2741");




		HashMap<String,Object> param=new HashMap<>();
		param.put("name","business.recycle.getRecycleRangeByTitle");
		param.put("version","1.0");
		param.put("format","json");
		param.put("app_key","app_id_3");
		param.put("timestamp", Calendar.getInstance().getTimeInMillis());
		param.put("token",securityToken);
		//param.put("sign","111");
		param.put("nonce", UUID.randomUUID().toString());
		param.put("data",recyclersServiceRangeBean);

		String jsonStr = JSON.toJSONString(param);
		String sign = ApiUtil.buildSign(JSON.parseObject(jsonStr), "sign_key_99aabbcc");
		param.put("sign", sign);

		System.out.println("请求的参数是 ："+JSON.toJSONString(param));
		Response response= FastHttpClient.post().url(api).body(JSON.toJSONString(param)).build().execute();
		String resultJson=response.body().string();
		System.out.println("返回的参数是 ："+resultJson);
	}

}
