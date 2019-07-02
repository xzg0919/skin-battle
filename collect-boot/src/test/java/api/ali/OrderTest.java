package api.ali;

import com.alibaba.fastjson.JSON;
import com.tzj.collect.api.ali.param.*;
import com.tzj.collect.api.app.param.OrderPayParam;
import com.tzj.collect.entity.OrderPic;
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
                String token= JwtUtils.generateToken("8401", ALI_API_EXPRIRE,ALI_API_TOKEN_SECRET_KEY);
                String securityToken=JwtUtils.generateEncryptToken(token,ALI_API_TOKEN_CYPTO_KEY);
                System.out.println("token是 : "+securityToken);

                String tokenCyptoKey = ALI_API_TOKEN_CYPTO_KEY;
                String key = CipherTools.initKey(tokenCyptoKey);
                String decodeToken = CipherTools.decrypt(securityToken, key);
                Claims claims = JwtUtils.getClaimByToken(decodeToken, ALI_API_TOKEN_SECRET_KEY);
                String subjectStr = claims.getSubject();
                System.out.println("反向編譯 token是："+subjectStr);

                //String api="http://open.mayishoubei.com/ali/api";
                String api="http://dog.mayishoubei.com:9090/ali/api";

                String  location = "121.446438,30.915836";

            MapAddressBean mapAddressBean = new MapAddressBean();
            mapAddressBean.setId("92477");
            mapAddressBean.setAdcCode("330103");
            mapAddressBean.setAddress("浙江省杭州市下城区朝晖街道环球中心西湖文化广场");
            mapAddressBean.setCity("杭州市");
            mapAddressBean.setCityCode("0571");
            mapAddressBean.setDistrict("下城区");
            mapAddressBean.setLocation("121.451897,31.229806");
            mapAddressBean.setName("环球中心");
            mapAddressBean.setProvince("浙江省");
            mapAddressBean.setTownCode("330103006000");
            mapAddressBean.setTownShip("朝晖街道");
            mapAddressBean.setIsSelected("1");
            mapAddressBean.setUserName("王先hao");
            mapAddressBean.setTel("13252525252");
            mapAddressBean.setHouseNumber("测试101");


                HashMap<String,Object> param=new HashMap<>();
                param.put("name","memberAddress.saveMemberAddressdByMap");
                param.put("version","1.0");
                param.put("format","json");
                param.put("app_key","app_id_1");
                param.put("timestamp", Calendar.getInstance().getTimeInMillis());
                param.put("token",securityToken);
                //param.put("sign","111");
                param.put("nonce", UUID.randomUUID().toString());
                param.put("data",mapAddressBean);

                String jsonStr=JSON.toJSONString(param);
                String sign= ApiUtil.buildSign(JSON.parseObject(jsonStr),"sign_key_11223344");
                param.put("sign",sign);

                String s = "{\"app_key\":\"app_id_1\",\"data\":{\"cityId\":745},\"name\":\"memberAddress.memberAddressList\",\"format\":\"json\",\"sign\":\"FC9A826D26B306554FA6EEA9F5633520\",\"version\":\"1.0\",\"nonce\":1236131394899.91299,\"token\":\"3F3TEMH74565Q5QORHNPE76UZM6VT4JPWVV4OPUNTGAXLLRLC6B5GYU3LW34YHVNOEFL2LXPVT24U65CVPQU32QS6WCOW4OQQ3AURAVX5JCW3DOJFI7QONQRCOYHSMZITFGRL7NE5YTTFKOD4CHYYW5XCU546HJVXIOTWEUH553LW7Q5I5HMBELSQBTBTYOQZEB2JBJEI7CERPJ5GXZGKOKLSCQFMEABHE2O75VTH57K7GHNNLDWI3HIJKS743QNBZCVVCNMPXWADKD4T3M3QCMWPAM7SRGIBEE27W26GDOMZF2JEZLQ\",\"timestamp\":\""+Calendar.getInstance().getTimeInMillis()+"\"}";

                System.out.println("请求的参数是 ："+JSON.toJSONString(param));
                Response response= FastHttpClient.post().url(api).body(JSON.toJSONString(param)).build().execute();
                String resultJson=response.body().string();
                System.out.println("返回的参数是 ："+resultJson);
        }
}
