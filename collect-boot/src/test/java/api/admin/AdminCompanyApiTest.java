
/**
* @Title: AdminApiTest.java
* @date 2018年3月20日 下午12:52:44
* @version V1.0
* @author:[王池]
*/

package api.admin;

import com.alibaba.fastjson.JSON;
import com.tzj.collect.core.param.admin.CategoryBean;
import com.tzj.collect.core.param.admin.CompanyBean;
import com.tzj.collect.core.param.ali.PageBean;
import com.tzj.collect.core.param.business.BOrderBean;
import com.tzj.module.api.utils.JwtUtils;
import com.tzj.module.common.utils.security.CipherTools;
import com.tzj.module.easyopen.util.ApiUtil;
import io.itit.itf.okhttp.FastHttpClient;
import io.itit.itf.okhttp.Response;
import io.jsonwebtoken.Claims;

import java.util.*;

import static com.tzj.collect.common.constant.TokenConst.*;

/**
* @ClassName: AdminApiTest
* @date 2018年3月20日 下午12:52:44
* @author:[王池]
*/

public class AdminCompanyApiTest {
	 public static void main(String[] args) throws Exception {
		 String token= JwtUtils.generateToken("1", ADMIN_API_EXPRIRE,ADMIN_API_TOKEN_SECRET_KEY);
		 String securityToken=JwtUtils.generateEncryptToken(token,ADMIN_API_TOKEN_CYPTO_KEY);
		 System.out.println("token是 : "+securityToken);

		 String tokenCyptoKey = ADMIN_API_TOKEN_CYPTO_KEY;
		 String key = CipherTools.initKey(tokenCyptoKey);
		 String decodeToken = CipherTools.decrypt(securityToken, key);
		 Claims claims = JwtUtils.getClaimByToken(decodeToken, ADMIN_API_TOKEN_SECRET_KEY);
		 String subjectStr = claims.getSubject();
		 System.out.println("反向編譯 token是："+subjectStr);


	        String api="http://localhost:9090/admin/api";
//		 RecyclersServiceRangeBean recyclersServiceRangeBean = new RecyclersServiceRangeBean();
//		 recyclersServiceRangeBean.setTitle("1");
//		 recyclersServiceRangeBean.setCompanyId(1);
//		 List<AreaBean> areaList = new ArrayList<>();
//		 AreaBean areaBean = new AreaBean();
//		 areaBean.setAreaId("4282");
//		 areaBean.setStreeId("4622");
//		 areaBean.setSaveOrDelete("0");
//		 AreaBean areaBean1 = new AreaBean();
//		 areaBean1.setAreaId("4282");
//		 areaBean1.setStreeId("4621");
//		 areaBean1.setSaveOrDelete("1");
//		 AreaBean areaBean2 = new AreaBean();
//		 areaBean2.setAreaId("4282");
//		 areaBean2.setStreeId("4623");
//		 areaBean2.setSaveOrDelete("0");
//		 AreaBean areaBean3 = new AreaBean();
//		 areaBean3.setAreaId("4282");
//		 areaBean3.setStreeId("4624");
//		 areaBean3.setSaveOrDelete("0");
//		 areaList.add(areaBean);
//		 areaList.add(areaBean1);
//		 areaList.add(areaBean2);
//		 areaList.add(areaBean3);
//
//		 recyclersServiceRangeBean.setAreaList(areaList);

		 CategoryBean categoryBean = new CategoryBean();
		 categoryBean.setCityId("2237");
		 categoryBean.setCompanyId("1");
		 categoryBean.setIsOpen("1");
		 List<String> list = new ArrayList<>();
		 list.add("26");
		 list.add("30");
		 categoryBean.setParamList(list);

	        HashMap<String,Object> param=new HashMap<>();
	        param.put("name","category.saveOrDeleteCategoryById");
	        param.put("version","1.0");  
	        param.put("format","json");
	        param.put("app_key","app_id_4");
	        param.put("timestamp", Calendar.getInstance().getTimeInMillis());
	        param.put("token",securityToken);
	        //param.put("sign","111");
	        param.put("nonce", UUID.randomUUID().toString());
	        param.put("data",categoryBean);

	        String jsonStr=JSON.toJSONString(param);
	        String sign= ApiUtil.buildSign(JSON.parseObject(jsonStr),"sign_key_998877");
	        param.put("sign",sign);
			 System.out.println("请求的参数是："+JSON.toJSONString(param));
	        Response response= FastHttpClient.post().url(api).body(JSON.toJSONString(param)).build().execute();
	        String resultJson=response.body().string();
	        System.out.println("返回的参数是："+resultJson);
	    }
}
