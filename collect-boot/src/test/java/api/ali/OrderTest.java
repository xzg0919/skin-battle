package api.ali;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tzj.collect.core.param.ali.*;
import com.tzj.collect.core.param.app.OrderPayParam;
import com.tzj.collect.core.param.business.CategoryBean;
import com.tzj.collect.core.param.iot.IotPostParamBean;
import com.tzj.module.api.utils.JwtUtils;
import com.tzj.module.common.utils.security.CipherTools;
import com.tzj.module.easyopen.util.ApiUtil;
import io.itit.itf.okhttp.FastHttpClient;
import io.itit.itf.okhttp.Response;
import io.jsonwebtoken.Claims;

import java.math.BigDecimal;
import java.util.*;

import static com.tzj.collect.common.constant.TokenConst.*;

public class OrderTest {

        /**
         * @param args
         * @throws Exception
         */
        public static void main(String[] args) throws Exception {
                String token= JwtUtils.generateToken("3020072915491038", ALI_API_EXPRIRE,ALI_API_TOKEN_SECRET_KEY);
//                String token= JwtUtils.generateToken(userId, ALI_API_EXPRIRE,ALI_API_TOKEN_SECRET_KEY);
                String securityToken=JwtUtils.generateEncryptToken(token,ALI_API_TOKEN_CYPTO_KEY);
                System.out.println("token是 : "+securityToken);
                //String api="http://shoubeics.mayishoubei.com/ali/api";
                String api="http://localhost:8080/ali/api";

                MapAddressBean mapAddressBean = new MapAddressBean();
                mapAddressBean.setProvinceId(1);
                mapAddressBean.setCityId(2);
                mapAddressBean.setAreaId(3);
                mapAddressBean.setStreetId(4);
                mapAddressBean.setId("163451");
                mapAddressBean.setIsSelected("1");
                mapAddressBean.setUserName("王先生");
                mapAddressBean.setTel("18375669651");
                mapAddressBean.setAddress("伟业金景苑二村38栋201");


                HashMap<String,Object> param=new HashMap<>();
                param.put("name", "memberAddress.saveMemberAddressByHand");
                param.put("version","1.0");
                param.put("format","json");
                param.put("app_key","app_id_1");
                param.put("timestamp",  Calendar.getInstance().getTimeInMillis());
                param.put("token", securityToken);
                param.put("nonce", UUID.randomUUID().toString());
                param.put("data", mapAddressBean);

                String jsonStr= JSON.toJSONString(param);
                System.out.println(jsonStr);
                String sign= ApiUtil.buildSign(JSON.parseObject(jsonStr),"sign_key_11223344");
                param.put("sign",sign);
                Long i = new Date().getTime();
                System.out.println("请求的参数是 ："+JSON.toJSONString(param));
                Response response= FastHttpClient.post().url(api).body(JSON.toJSONString(param)).build().execute();
                String resultJson=response.body().string();
                Long ii = new Date().getTime();
                System.out.println(ii-i);
                System.out.println("返回的参数是 ："+resultJson);
        }
}
