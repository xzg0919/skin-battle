
/**
* @Title: AdminApiTest.java
* @date 2018年3月20日 下午12:52:44
* @version V1.0
* @author:[王池]
*/

package api.admin;

import com.alibaba.fastjson.JSON;
import com.tzj.collect.core.param.admin.AdminAddressBean;
import com.tzj.collect.core.param.admin.AdminShareCodeBean;
import com.tzj.collect.core.param.admin.CompanyBean;
import com.tzj.collect.core.param.admin.TransStationBean;
import com.tzj.collect.core.param.ali.AreaBean;
import com.tzj.collect.core.param.ali.CategoryBean;
import com.tzj.collect.core.param.ali.OrderBean;
import com.tzj.collect.core.param.ali.PageBean;
import com.tzj.collect.core.param.business.BOrderBean;
import com.tzj.collect.entity.LineQrCode;
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


		 //String api="http://open.mayishoubei.com/admin/api";
		 //String api =  "http://api.station.mayishoubei.com/app/api";
		 //String api =  "http://172.19.182.84:9001/admin/api";
		 String api =  "http://localhost:9090/admin/api";
		 AdminShareCodeBean adminShareCodeBean = new AdminShareCodeBean();
//		 adminShareCodeBean.setQrType(LineQrCode.QrType.OFFLINE);
//		 adminShareCodeBean.setQrName("测试");
		 adminShareCodeBean.setQrCode("45dad314-af73-4686-8d39-5a91ad5fa6d9");
//		 adminShareCodeBean.setQrName("测试");
//		 adminShareCodeBean.setQrCodeInfo("测试说明");
//		 AdminAddressBean adminAddressBean = new AdminAddressBean();
//		 adminAddressBean.setAreaId(37492L);
//		 adminAddressBean.setCityId(37117L);
//		 adminAddressBean.setStreetId(3L);
		 adminShareCodeBean.setProvinceId(1L);
//		 adminAddressBean.setProvinceName("四川省");
//		 adminAddressBean.setCityName("凉山彝族自治州");
//		 adminAddressBean.setAreaName("冕宁县");
//		 adminAddressBean.setStreetName("测试街道");
		 PageBean pageBean = new PageBean();
		 pageBean.setPageNumber(1);
		 pageBean.setPageSize(10);
//		 List<AdminAddressBean> newArrayList = new ArrayList<>();
//		 newArrayList.add(adminAddressBean);
//		 adminShareCodeBean.setAdminCityList(newArrayList);
//		 adminShareCodeBean.setPageBean(pageBean);
	        HashMap<String,Object> param=new HashMap<>();
	        param.put("name","admin.getTransStationList");
	        param.put("version","1.0");  
	        param.put("format","json");
	        param.put("app_key","app_id_4");
	        param.put("timestamp", Calendar.getInstance().getTimeInMillis());
	        param.put("token",securityToken);
	        //param.put("sign","111");
	        param.put("nonce", UUID.randomUUID().toString());
	        param.put("data","{name: \"\", pageNum: 1, pageSize: 10, tel: \"\"}");

	        String jsonStr=JSON.toJSONString(param);
	        String sign= ApiUtil.buildSign(JSON.parseObject(jsonStr),"sign_key_998877");
	        param.put("sign",sign);
			 System.out.println("请求的参数是："+JSON.toJSONString(param));
	        Response response= FastHttpClient.post().url(api).body(JSON.toJSONString(param)).build().execute();
	        String resultJson=response.body().string();
	        System.out.println("返回的参数是："+resultJson);
	    }
}
