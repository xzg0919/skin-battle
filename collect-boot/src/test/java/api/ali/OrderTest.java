package api.ali;

import com.alibaba.fastjson.JSON;
import com.tzj.collect.api.ali.param.MemberBean;
import com.tzj.collect.api.ali.param.OrderBean;
import com.tzj.collect.api.ali.param.PageBean;
import com.tzj.module.api.utils.JwtUtils;
import com.tzj.module.common.utils.security.CipherTools;
import com.tzj.module.easyopen.util.ApiUtil;
import io.itit.itf.okhttp.FastHttpClient;
import io.itit.itf.okhttp.Response;
import io.jsonwebtoken.Claims;

import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;

import static com.tzj.collect.common.constant.TokenConst.*;

public class OrderTest {

        /**
         * @param args
         * @throws Exception
         */
        public static void main(String[] args) throws Exception {
                String token= JwtUtils.generateToken("22920", ALI_API_EXPRIRE,ALI_API_TOKEN_SECRET_KEY);
                String securityToken=JwtUtils.generateEncryptToken(token,ALI_API_TOKEN_CYPTO_KEY);
                System.out.println("token是 : "+securityToken);

                String tokenCyptoKey = ALI_API_TOKEN_CYPTO_KEY;
                String key = CipherTools.initKey(tokenCyptoKey);
                String decodeToken = CipherTools.decrypt(securityToken, key);
                Claims claims = JwtUtils.getClaimByToken(decodeToken, ALI_API_TOKEN_SECRET_KEY);
                String subjectStr = claims.getSubject();
                System.out.println("反向編譯 token是："+subjectStr);

                //String api="http://open.mayishoubei.com/ali/api";
                String api="http://localhost:9090/ali/api";
//
                OrderBean orderBean = new OrderBean();
                orderBean.setStatus("0");
                orderBean.setPagebean(new PageBean());

                HashMap<String,Object> param=new HashMap<>();
                param.put("name","member.getAuthCode");
                param.put("version","1.0");
                param.put("format","json");
                param.put("app_key","app_id_1");
                param.put("timestamp", Calendar.getInstance().getTimeInMillis());
                param.put("token",securityToken);
                //param.put("sign","111");
                param.put("nonce", UUID.randomUUID().toString());
                param.put("data",new MemberBean());

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
