package api.app;

import com.alibaba.fastjson.JSON;
import com.tzj.collect.core.param.admin.TransStationBean;
import com.tzj.collect.core.param.ali.*;
import com.tzj.collect.core.result.app.AppCompany;
import com.tzj.collect.entity.OrderPic;
import com.tzj.module.api.utils.JwtUtils;
import com.tzj.module.common.utils.security.CipherTools;
import com.tzj.module.easyopen.util.ApiUtil;
import io.itit.itf.okhttp.FastHttpClient;
import io.itit.itf.okhttp.Response;
import io.jsonwebtoken.Claims;

import java.util.*;

import static com.tzj.collect.common.constant.TokenConst.*;

/**
 *
 * APP 端 token api 测试代码
 *
 * @Author 胡方明（12795880@qq.com）
 **/
public class TokenGetAppTest {
	 public static void main(String[] args) throws Exception {

		 String token = JwtUtils.generateToken("2205", APP_API_EXPRIRE, APP_API_TOKEN_SECRET_KEY);
		 String securityToken = JwtUtils.generateEncryptToken(token, APP_API_TOKEN_CYPTO_KEY);
		 System.out.println("token : "+securityToken);

		 String tokenCyptoKey = APP_API_TOKEN_CYPTO_KEY;
		 String key = CipherTools.initKey(tokenCyptoKey);
		 String decodeToken = CipherTools.decrypt(securityToken, key);
		 Claims claims = JwtUtils.getClaimByToken(decodeToken, APP_API_TOKEN_SECRET_KEY);
		 String subjectStr = claims.getSubject();
		 System.out.println("反向編譯 token是："+subjectStr);
//		 String api="http://localhost:9090/app/api";
		 String api="http://localhost:9090/app/api";

		 OrderBean orderBean = new OrderBean();

		 orderBean.setId(70380);
		 orderBean.setAchRemarks("完成描述");
		 orderBean.setSignUrl("dhuasidhiashda");
		 orderBean.setCleanUp(2);
		 orderBean.setCompanyId(41);
		 orderBean.setTitle("HOUSEHOLD");
		 List<IdAmountListBean> orderItemList = new ArrayList<>();
		 IdAmountListBean  idAmountListBean = new IdAmountListBean();
		 idAmountListBean.setCategoryParentId(25);
		 idAmountListBean.setCategoryParentName("废纸");
		 List<OrderItemBean> idAmount = new ArrayList<>();
		 OrderItemBean orderItemBean = new OrderItemBean();
		 orderItemBean.setCategoryId(26);
		 orderItemBean.setCategoryName("shubenzhazhi");
		 orderItemBean.setAmount(2.1);
		 idAmount.add(orderItemBean);
		 idAmountListBean.setIdAndAmount(idAmount);
		 orderItemList.add(idAmountListBean);
		 orderBean.setIdAndListList(orderItemList);
		 OrderPic orderPic = new OrderPic();
		 orderPic.setOrigPic("www.baidu.com,www.baidu.com");
		 orderPic.setPicUrl("www.baidu.com,www.baidu.com");
		 orderPic.setSmallPic("www.baidu.com,www.baidu.com");
		 orderBean.setOrderPic(orderPic);

	        HashMap<String,Object> param=new HashMap<>();
	        param.put("name", "app.order.savebyrecy");
	        param.put("version", "1.0");
	        param.put("format", "json");
	        param.put("app_key", "app_id_2");
	        param.put("timestamp", Calendar.getInstance().getTimeInMillis());
	        param.put("token",securityToken);
	        //param.put("sign","111");
	        param.put("nonce", UUID.randomUUID().toString());
	        param.put("data", orderBean);

	        String jsonStr = JSON.toJSONString(param);
	        String sign = ApiUtil.buildSign(JSON.parseObject(jsonStr), "sign_key_55667788");
	        param.put("sign", sign);


		    System.out.println("请求的参数是: "+JSON.toJSONString(param));
		    Response response= FastHttpClient.post().url(api).body(JSON.toJSONString(param)).build().execute();
	        String resultJson=response.body().string();
	        System.out.println("返回的结果是: "+resultJson);
	    }
}
