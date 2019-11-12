package api.enterprise;

import com.alibaba.fastjson.JSON;
import com.tzj.collect.core.param.enterprise.EnterpriseTerminalBean;
import com.tzj.module.api.utils.JwtUtils;
import com.tzj.module.common.utils.security.CipherTools;
import com.tzj.module.easyopen.util.ApiUtil;
import io.itit.itf.okhttp.FastHttpClient;
import io.itit.itf.okhttp.Response;
import io.jsonwebtoken.Claims;

import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;

import static com.tzj.common.constant.TokenConst.*;


public class EnterpriseTest {

    public static void main(String[] args) throws Exception{
        String token = JwtUtils.generateToken("1",ENTERPRISE_API_EXPRIRE, ENTERPRISE_API_TOKEN_SECRET_KEY);
        String securityToken = JwtUtils.generateEncryptToken(token, ENTERPRISE_API_TOKEN_CYPTO_KEY);
        System.out.println("生成的token是："+securityToken);

        String tokenCyptoKey = ENTERPRISE_API_TOKEN_CYPTO_KEY;
        String key = CipherTools.initKey(tokenCyptoKey);
        String decodeToken = CipherTools.decrypt(securityToken, key);
        Claims claims = JwtUtils.getClaimByToken(decodeToken, ENTERPRISE_API_TOKEN_SECRET_KEY);
        String subjectStr = claims.getSubject();
        System.out.println("反向編譯 token是："+subjectStr);

        String api="http://localhost:9000/enterprise/api";

        EnterpriseTerminalBean enterpriseTerminalBean = new EnterpriseTerminalBean();
        enterpriseTerminalBean.setContacts("龙健的店");
        enterpriseTerminalBean.setAddress("测试地址");
        enterpriseTerminalBean.setName("龙建");
        enterpriseTerminalBean.setTel("15721494216");
        enterpriseTerminalBean.setPassword("1111112");

        HashMap<String,Object> param=new HashMap<>();
        param.put("name","enterprise.updateEnterpriseTerminal");
        param.put("version","1.0");
        param.put("format","json");
        param.put("app_key","app_id_5");
        param.put("timestamp", Calendar.getInstance().getTimeInMillis());
        param.put("token",securityToken);
        param.put("nonce", UUID.randomUUID().toString());
        param.put("data",enterpriseTerminalBean);

        String jsonStr= JSON.toJSONString(param);
        String sign= ApiUtil.buildSign(JSON.parseObject(jsonStr),"sign_key_99bbccdd");
        param.put("sign",sign);


        System.out.println("请求的参数是 ："+JSON.toJSONString(param));
        Response response= FastHttpClient.post().url(api).body(JSON.toJSONString(param)).build().execute();
        String resultJson=response.body().string();
        System.out.println("返回的参数是 ："+resultJson);

    }
}
