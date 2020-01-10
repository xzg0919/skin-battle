
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

//		String api = "http://test.tcompanypoint.mayishoubei.com/company/api";

		String api="http://localhost:9090/company/api";
//		CompanyCommunityBean companyCommunityBean= new CompanyCommunityBean();
//		companyCommunityBean.setProvinceId("1");
//		companyCommunityBean.setCityId("2");
//		companyCommunityBean.setAreaId("3");
//		companyCommunityBean.setStreetId("4");
//		companyCommunityBean.setId("3");
//		companyCommunityBean.setCommunityName("运河湾居委");
//		companyCommunityBean.setHouseNum("311");
//		companyCommunityBean.setPointsNum("2");
//		companyCommunityBean.setPutType("1");
//		companyCommunityBean.setIsDry("0");
//		companyCommunityBean.setDryTime("00:00:01-12:00:00,12:00:01-23:59:59");
//		companyCommunityBean.setIsWet("0");
//		companyCommunityBean.setWetTime("00:00:01-12:00:00,12:00:01-23:59:59");
//		companyCommunityBean.setIsHarmful("0");
//		companyCommunityBean.setHarmfulTime("00:00:01-12:00:00,12:00:01-23:59:59");
//		companyCommunityBean.setIsRecovery("0");
//		companyCommunityBean.setRecoveryTime("00:00:01-12:00:00,12:00:01-23:59:59");
//		companyCommunityBean.setRecoveryWeek("1,2,7");
//		List<CommunityHouseName> houseNameList = new ArrayList<>();
//		CommunityHouseName communityHouseName = new CommunityHouseName();
//		communityHouseName.setHouseName("测试小区3");
//		communityHouseName.setAddress("我是测试地址1");
//		communityHouseName.setId((long)3);
//		CommunityHouseName communityHouseName2 = new CommunityHouseName();
//		communityHouseName2.setHouseName("测试小区4");
//		communityHouseName2.setAddress("我是测试地址1");
//		communityHouseName2.setId((long)4);
//		houseNameList.add(communityHouseName);
//		houseNameList.add(communityHouseName2);
//		companyCommunityBean.setHouseNameList(houseNameList);

		CompanyCommunityBean companyCommunityBean = new CompanyCommunityBean();
		companyCommunityBean.setCommunityId("3");


		CompanyBean companyBean = new CompanyBean();
		companyBean.setTel("111111");
		companyBean.setPassword("111111");

		GoodsBean goodsBean = new GoodsBean();
		goodsBean.setId("1");

		ProductBean productBean = new ProductBean();
//
//		productBean.setName("活动名称");
//		productBean.setId("1");
//		productBean.setPickStartDate("2020-01-01");
//		productBean.setPickEndDate("2021-01-01");
//		productBean.setHouseNameId("5");
//		productBean.setDetail("我是活动说明");
//
//		List<String> recyclerIds = new ArrayList<>();
//		recyclerIds.add("1");
//		recyclerIds.add("2");
//		productBean.setRecyclerIds(recyclerIds);
//		List<ProductGoodsBean> productGoodsBeanList = new ArrayList<>();
//		ProductGoodsBean productGoodsBean1 = new ProductGoodsBean();
//		productGoodsBean1.setGoodsId("2");
//		productGoodsBean1.setTotalNum("99");
//		ProductGoodsBean productGoodsBean2 = new ProductGoodsBean();
//		productGoodsBean2.setGoodsId("3");
//		productGoodsBean2.setTotalNum("88");
//		productGoodsBeanList.add(productGoodsBean1);
//		productGoodsBeanList.add(productGoodsBean2);
//		productBean.setProductGoodsBeanList(productGoodsBeanList);

		productBean.setId("1");


		HashMap<String,Object> param=new HashMap<>();
		param.put("name","company.getProductOrderDetail");
		param.put("version","1.0");
		param.put("format","json");
		param.put("app_key","app_id_3");
		param.put("timestamp", Calendar.getInstance().getTimeInMillis());
		param.put("token",securityToken);
		//param.put("sign","111");
		param.put("nonce", UUID.randomUUID().toString());
		param.put("data",productBean);

		String signKey = SignUtils.produceSignKey(token, BUSINESS_API_TOKEN_SIGN_KEY);
		System.out.println(signKey);
		String jsonStr = JSON.toJSONString(param);
		String sign = ApiUtil.buildSign(JSON.parseObject(jsonStr), signKey);
		param.put("sign", sign);

		System.out.println("请求的参数是 ："+JSON.toJSONString(param));
		Response response= FastHttpClient.post().url(api).body(JSON.toJSONString(param)).build().execute();
		String resultJson=response.body().string();
		System.out.println("返回的参数是 ："+resultJson);
	}

}
