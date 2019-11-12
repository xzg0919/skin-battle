
/**
 * @Title: BusinessOrderApiTest.java
 * @date 2018年3月20日 下午3:20:45
 * @version V1.0
 * @author:[王池]
 */

package api.business;

import com.alibaba.fastjson.JSON;
import com.tzj.collect.core.param.ali.CategoryBean;
import com.tzj.collect.core.param.ali.IdAmountListBean;
import com.tzj.collect.core.param.ali.OrderBean;
import com.tzj.collect.core.param.ali.OrderItemBean;
import com.tzj.module.api.utils.JwtUtils;
import com.tzj.module.api.utils.SignUtils;
import com.tzj.module.common.utils.security.CipherTools;
import com.tzj.module.easyopen.util.ApiUtil;
import io.itit.itf.okhttp.FastHttpClient;
import io.itit.itf.okhttp.Response;
import io.jsonwebtoken.Claims;
import org.springframework.data.annotation.Id;

import java.util.*;

import static com.tzj.collect.common.constant.TokenConst.*;

/**
 * @ClassName: BusinessOrderApiTest
 * @date 2018年3月20日 下午3:20:45
 * @author:[王池]
 */

public class BusinessOrderApiTest {
	public static void main(String[] args) throws Exception {
		String token = JwtUtils.generateToken("41", BUSINESS_API_EXPRIRE, BUSINESS_API_TOKEN_SECRET_KEY);
		String securityToken = JwtUtils.generateEncryptToken(token, BUSINESS_API_TOKEN_CYPTO_KEY);
		System.out.println("生成的token是："+securityToken);

		String tokenCyptoKey = BUSINESS_API_TOKEN_CYPTO_KEY;
		String key = CipherTools.initKey(tokenCyptoKey);
		String decodeToken = CipherTools.decrypt(securityToken, key);
		Claims claims = JwtUtils.getClaimByToken(decodeToken, BUSINESS_API_TOKEN_SECRET_KEY);
		String subjectStr = claims.getSubject();
		System.out.println("反向編譯 token是："+subjectStr);

		String api="http://localhost:9090/business/api";

//		OrderBean orderBean = new OrderBean();
//			orderBean.setId(70240);
//			orderBean.setAchPrice("20");
//		List<IdAmountListBean> list = new ArrayList<>();
//		IdAmountListBean idAmountListBean = new IdAmountListBean();
//		idAmountListBean.setCategoryParentId(25);
//		idAmountListBean.setCategoryParentName("废纸");
//		List<OrderItemBean> idAndAmount = new ArrayList<>();
//		OrderItemBean orderItemBean = new OrderItemBean();
//		orderItemBean.setCategoryId(26);
//		orderItemBean.setCategoryName("书本杂志");
//		orderItemBean.setAmount(3.0);
//		idAndAmount.add(orderItemBean);
//		idAmountListBean.setIdAndAmount(idAndAmount);
//		IdAmountListBean idAmountListBean1 = new IdAmountListBean();
//		idAmountListBean1.setCategoryParentId(45);
//		idAmountListBean1.setCategoryParentName("废旧衣物");
//		List<OrderItemBean> idAndAmount1 = new ArrayList<>();
//		OrderItemBean orderItemBean1 = new OrderItemBean();
//		orderItemBean1.setCategoryId(46);
//		orderItemBean1.setCategoryName("衣服");
//		orderItemBean1.setAmount(4.0);
//		idAndAmount1.add(orderItemBean1);
//		OrderItemBean orderItemBean2 = new OrderItemBean();
//		orderItemBean2.setCategoryId(47);
//		orderItemBean2.setCategoryName("裤子");
//		orderItemBean2.setAmount(5.0);
//		idAndAmount1.add(orderItemBean2);
//		idAmountListBean1.setIdAndAmount(idAndAmount1);
//		list.add(idAmountListBean1);
//		list.add(idAmountListBean);
//		orderBean.setIdAndListList(list);
		OrderBean orderBean = new OrderBean();
		orderBean.setId(70120);


		HashMap<String,Object> param=new HashMap<>();
		param.put("name","order.getOrderAchItemDatail");
		param.put("version","1.0");
		param.put("format","json");
		param.put("app_key","app_id_3");
		param.put("timestamp", Calendar.getInstance().getTimeInMillis());
		param.put("token",securityToken);
		//param.put("sign","111");
		param.put("nonce", UUID.randomUUID().toString());
		param.put("data",orderBean);

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
